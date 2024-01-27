package tileBasedKendallAlgorithms.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import tileBasedKendallAlgorithms.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class TileProcessor {

    private final Tile[][] tiles;
    private final Dataset<Row> dataset;
    private final String column1;
    private final String column2;
    private final CorrelationStatistics statistics;

    public TileProcessor(Tile[][] tiles, Dataset<Row> dataset, CorrelationStatistics statistics, String column1, String column2) {
        this.tiles = tiles;
        this.dataset = dataset;
        this.column1 = column1;
        this.column2 = column2;
        this.statistics = statistics;
    }

    public void processTile(Tile tile) {

        compareTileWithSelf(tile);
        compareTileWithEastTiles(tile);
        compareTileWithSouthTiles(tile);
        processNonCrossTiles(tile);
        tile.clearIdList();
    }

    private void compareTileWithSelf(Tile tile) {
        List<Long> idsTile1 = new ArrayList<>(tile.getPairIds());

        for (int i = 0; i < idsTile1.size(); i++) {
            for (int j = i + 1; j < idsTile1.size(); j++) {
                compareAndUpdateCounts(findRowById(idsTile1.get(i)), findRowById(idsTile1.get(j)));
            }
        }
    }

    private void compareTileWithEastTiles(Tile tile) {
        int tileColumn = tile.getCol();
        int row = tile.getRow();
        int maxColumns = tiles[0].length;

        for (int column = tileColumn + 1; column < maxColumns; column++) {
            Tile eastTile = tiles[row][column];
            if (!eastTile.isEmpty()) {
                compareTiles(tile, eastTile);
            }
        }
    }

    private void compareTileWithSouthTiles(Tile tile) {
        int tileRow = tile.getRow();
        int column = tile.getCol();
        int maxRows = tiles.length;

        for (int row = tileRow + 1; row < maxRows; row++) {
            Tile southTile = tiles[row][column];
            if (!southTile.isEmpty()) {
                compareTiles(tile, southTile);
            }
        }
    }

    private void compareTiles(Tile tile1, Tile tile2) {
        List<Long> idsTile1 = new ArrayList<>(tile1.getPairIds());
        List<Long> idsTile2 = new ArrayList<>(tile2.getPairIds());

        for (Long id1 : idsTile1) {
            for (Long id2 : idsTile2) {
                compareAndUpdateCounts(findRowById(id1), findRowById(id2));
            }
        }
    }

    private void processNonCrossTiles(Tile tile) {
        processSouthEastTiles(tile);
        processSouthWestTiles(tile);
    }

    private void processSouthEastTiles(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        long tile1IdCount = tile.getCount();

        for (int i = row + 1; i < tiles.length; i++) {
            for (int j = col + 1; j < tiles[row].length; j++) {
                if (tiles[i][j].isEmpty()) {
                    continue;  // Skip empty tile
                }
                long tile2IdCount = tiles[i][j].getCount();
                statistics.incrementConcordantCount(tile1IdCount * tile2IdCount);
            }
        }
    }

    private void processSouthWestTiles(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        long tile1IdCount = tile.getCount();

        for (int i = row + 1; i < tiles.length; i++) {
            for (int j = col - 1; j >= 0; j--) {
                if (tiles[i][j].isEmpty()) {
                    continue;  // Skip empty tile
                }

                long tile2IdCount = tiles[i][j].getCount();
                statistics.incrementDiscordantCount(tile1IdCount * tile2IdCount);
            }
        }
    }

    private void compareAndUpdateCounts(Row row1, Row row2) {
        double x1 = row1.getAs(column1);
        double x2 = row2.getAs(column1);
        double y1 = row1.getAs(column2);
        double y2 = row2.getAs(column2);

        if ((x1 < x2 && y1 < y2) || (x1 > x2 && y1 > y2)) {
            statistics.incrementConcordantCount();
        } else if ((x1 < x2 && y1 > y2) || (x1 > x2 && y1 < y2)) {
            statistics.incrementDiscordantCount();
        } else if (x1 == x2 && y1 != y2) {
            statistics.incrementTiedXCount();
        } else if (x1 != x2 && y1 == y2) {
            statistics.incrementTiedYCount();
        }
    }

    private Row findRowById(Long id) {
        return dataset.filter(functions.col("id").equalTo(id)).first();
    }
}