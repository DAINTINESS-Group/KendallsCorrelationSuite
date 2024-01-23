package tileBasedKendallAlgorithms.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import tileBasedKendallAlgorithms.tiles.Tile;

import java.util.HashSet;
import java.util.Set;

public class TileProcessor {

    private final Tile[][] tiles;
    private final Dataset<Row> dataset;
    private final Set<Pair> comparedPairs = new HashSet<>();
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
        compareTiles(tile, tile);
    }

    private void compareTileWithEastTiles(Tile tile) {
        int tileColumn = tile.getCol();
        int maxColumns = tiles[0].length;

        for (int col = tileColumn + 1; col < maxColumns; col++) {
            Tile eastTile = tiles[tile.getRow()][col];
            if (!eastTile.isEmpty()) {
                compareTiles(tile, eastTile);
            }
        }
    }

    private void compareTileWithSouthTiles(Tile tile) {
        int tileRow = tile.getRow();
        int maxRows = tiles.length;

        for (int row = tileRow + 1; row < maxRows; row++) {
            Tile southTile = tiles[row][tile.getCol()];
            if (!southTile.isEmpty()) {
                compareTiles(tile, southTile);
            }
        }
    }

    private void compareTiles(Tile tile1, Tile tile2) {
        Set<Long> idsTile1 = tile1.getPairIds();
        Set<Long> idsTile2 = tile2.getPairIds();

        idsTile1.forEach(id1 ->
            idsTile2.forEach(id2 -> {
                if (!id1.equals(id2) && !comparedPairs.contains(new Pair(id1, id2))) {
                    comparedPairs.add(new Pair(id1, id2));
                    compareAndUpdateCounts(findRowById(id1), findRowById(id2));
                }
        }));
    }

    private void processNonCrossTiles(Tile tile) {
        processNorthEastTiles(tile);
        processSoutheastTiles(tile);
        processSouthwestTiles(tile);
    }

    private void processNorthEastTiles(Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        long tile1IdCount = tile.getCount();

        for (int i = row - 1; i >= 0; i--) {
            for (int j = col + 1; j < tiles[0].length; j++) {
                if (tiles[i][j].isEmpty()) {
                    continue;  // Skip empty tile
                }

                long tile2IdCount = tiles[i][j].getCount();
                statistics.incrementConcordantCount(tile1IdCount * tile2IdCount);
            }
        }
    }

    private void processSoutheastTiles(Tile tile) {
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

    private void processSouthwestTiles(Tile tile) {
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