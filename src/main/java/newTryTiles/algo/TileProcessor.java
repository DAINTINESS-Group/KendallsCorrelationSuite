package newTryTiles.algo;

import org.apache.commons.math3.util.Pair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import newTryTiles.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

public class TileProcessor {

    private final Tile[][] tiles;
    private final Dataset<Row> dataset;
    private final String column1;
    private final String column2;
    private final CorrelationStatistics statistics;
    private final int maxColumns;
    private final int maxRows;
    
    public TileProcessor(Tile[][] tiles, Dataset<Row> dataset, CorrelationStatistics statistics, String column1, String column2) {
        this.tiles = tiles;
        this.dataset = dataset;
        this.column1 = column1;
        this.column2 = column2;
        this.statistics = statistics;
        maxRows = tiles.length;
        maxColumns = tiles[0].length;
    }

    public void processTile(Tile tile) {

        compareTileWithSelf(tile);
        compareTileWithEastTiles(tile);
        compareTileWithSouthTiles(tile);
        processNonCrossTiles(tile);
    }

    private void compareTileWithSelf(Tile tile) {
    	List<Pair<Double,Double>> tilePairs = new ArrayList<>(tile.getValuePairs());
    	int numPairs = tilePairs.size();
        for (int i = 0; i < numPairs; i++) {
            for (int j = i + 1; j < numPairs; j++) {
                double x1 =  tilePairs.get(i).getFirst().doubleValue();
                double y1 =  tilePairs.get(i).getSecond().doubleValue();
                double x2 =  tilePairs.get(j).getFirst().doubleValue();
                double y2 =  tilePairs.get(j).getSecond().doubleValue();
//System.err.println(x1 + " " + y1 + " " + x2 + " " + y2);                
            	compareValuePairs(x1, y1, x2, y2);
            }
        }
    }

    private void compareValuePairs(double x1, double y1, double x2, double y2) {

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
    
    private void compareTileWithEastTiles(Tile tile) {
        int tileColumn = tile.getCol();
        int tileRow = tile.getRow();
        //int maxColumns = tiles[0].length;

        for (int column = tileColumn + 1; column < maxColumns; column++) {
            Tile eastTile = tiles[tileRow][column];
            if (!eastTile.isEmpty()) {
                compareTiles(tile, eastTile);
            }
        }
    }

    private void compareTileWithSouthTiles(Tile tile) {
        int tileRow = tile.getRow();
        int tileColumn = tile.getCol();
        //int maxRows = tiles.length;

        for (int row = tileRow + 1; row < maxRows; row++) {
            Tile southTile = tiles[row][tileColumn];
            if (!southTile.isEmpty()) {
                compareTiles(tile, southTile);
            }
        }
    }

    private void compareTiles(Tile tile1, Tile tile2) {
    	List<Pair<Double,Double>> tilePairs1 = new ArrayList<>(tile1.getValuePairs());
    	List<Pair<Double,Double>> tilePairs2 = new ArrayList<>(tile2.getValuePairs());

    	for(Pair<Double,Double> p1: tilePairs1) {
    		for(Pair<Double,Double> p2: tilePairs2) {
                double x1 =  p1.getFirst().doubleValue();
                double y1 =  p1.getSecond().doubleValue();
                double x2 =  p2.getFirst().doubleValue();
                double y2 =  p2.getSecond().doubleValue();
//System.err.println(x1 + " " + y1 + " " + x2 + " " + y2);                
            	compareValuePairs(x1, y1, x2, y2);
    		}
    	}
 
    }


    private void processNonCrossTiles(Tile tile) {
        processSouthEastTiles(tile);
        processSouthWestTiles(tile);
    }

    private void processSouthEastTiles(Tile tile) {
        int tile1Row = tile.getRow();
        int tile2Col = tile.getCol();
        long tile1IdCount = tile.getCount();

        for (int i = tile1Row + 1; i < maxRows; i++) {
            for (int j = tile2Col + 1; j < maxColumns; j++) {
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

        for (int i = row + 1; i < maxRows; i++) {
            for (int j = col - 1; j >= 0; j--) {
                if (tiles[i][j].isEmpty()) {
                    continue;  // Skip empty tile
                }

                long tile2IdCount = tiles[i][j].getCount();
                statistics.incrementDiscordantCount(tile1IdCount * tile2IdCount);
            }
        }
    }


}