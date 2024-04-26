package util.algo;

import java.util.Comparator;
import java.util.List;

import util.common.DoublePair;
import util.tiles.ITile;

/**
 * The {@code AlgoSimpleTilesAndPointComparison} class is responsible for processing tiles in a tile-based approach to calculating
 * Kendall's tau correlation. 
 * The algorithm uses simple tiles, and involves comparing every tile with 
 * (1) itself
 * (2) a subset of its cross, i.e., its "band of neighbors" at East, South, 
 * (3) its "corner" rectangles of tiles at SouthEast, SouthWest) 
 * to count concordant[c], discordant[d], and tied [t] pairs of observations. 
 * These counts contribute to the calculation of the Kendall's tau correlation coefficient.
 * <p>
 * Within each tile, all points need to be compared to each other for [c,d,t] numbers
 * With respect to the cross tiles, you know in advance that the points of the band tiles have larger values
 * in one dimension, already. So, you need to check only the other dimension to determine [c,d,t]
 * The corners are straightforward: you do not check point-wise, but you immediately count the entire tile as c or d
 * <p>
 * This class is an improvement over the idea of computing every tile with (2') all the cross and (3') all the corners.
 *
 * @author Petros Karampas
 * @author pvassil
 */
public class AlgoSimpleTilesAndPointComparison {

    private final ITile[][] tiles;
    private final CorrelationStatistics correlationStats;
    private final int maxColumns;
    private final int maxRows;

    public AlgoSimpleTilesAndPointComparison(ITile[][] tiles, CorrelationStatistics correlationStats) {
    	//System.out.println("creating AlgoSimpleTilesAndPoints");

        this.tiles = tiles;
        this.correlationStats = correlationStats;
        maxRows = tiles.length;
        maxColumns = tiles[0].length;
    }

    public void processAllTiles() {
    	//System.out.println("running AlgoSimpleTilesAndPoints");
    	
    	CalculationTimer.reset();
        for (ITile[] rowOfTiles : tiles) {
            for (ITile tile : rowOfTiles) {
                if (!tile.isEmpty()) {
                    processTile(tile);
                }
            }
        }
    }
    
    protected void processTile(ITile tile) {    	
        int tileRow = tile.getRow();
        int tileColumn = tile.getColumn();
        int tilePairsCount = (int)tile.getCount();
        List<DoublePair> tilePairs = tile.getValuePairs();

        if(tilePairs.size() != tilePairsCount) {
        	System.err.println("Tileprocessor.processTile error, parCount and list do not match: " +tilePairsCount + "\t" + tilePairs.size());
        	System.err.println("TileSimple: " +tile.toString());
        }
        	long startTime = System.currentTimeMillis();
        compareTileWithSelf(tilePairs, tilePairsCount);
        	long endTime = System.currentTimeMillis();
        	double elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        	CalculationTimer.incrementCompareWithSelfTime(elapsedTimeSeconds);

        	startTime = System.currentTimeMillis();
        compareTileWithEastTiles(tilePairs, tileRow, tileColumn);
        	endTime = System.currentTimeMillis();
        	elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        	CalculationTimer.incrementCompareWithEastTime(elapsedTimeSeconds);

        	startTime = System.currentTimeMillis();
        compareTileWithSouthTiles(tilePairs, tileRow, tileColumn);
        	endTime = System.currentTimeMillis();
        	elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        	CalculationTimer.incrementCompareWithSouthTime(elapsedTimeSeconds);

        	startTime = System.currentTimeMillis();
        processNonCrossTiles(tilePairsCount, tileRow, tileColumn);
        	endTime = System.currentTimeMillis();
        	elapsedTimeSeconds = (endTime - startTime) / 1000.0;
        	CalculationTimer.incrementCompareWithNonCrossTime(elapsedTimeSeconds);
    }

    protected void compareTileWithSelf(List<DoublePair> tilePairs, int tilePairsCount) {    	
        for (int i = 0; i < tilePairsCount - 1; i++) {
            DoublePair pair1 = tilePairs.get(i);
            for (int j = i + 1; j < tilePairsCount; j++) {
                DoublePair pair2 = tilePairs.get(j);
                compareValuePairs(pair1, pair2);
            }
        }
    }

    protected void compareTileWithEastTiles(List<DoublePair> tilePairs, int tileRow, int tileColumn) {
        for (int column = tileColumn + 1; column < maxColumns; column++) {
            ITile eastTile = tiles[tileRow][column];
            if (!eastTile.isEmpty()) {
                List<DoublePair> eastTilePairs = eastTile.getValuePairs();
                compareWithEastTile(tilePairs, eastTilePairs);
            }
        }
    }

    protected void compareTileWithSouthTiles(List<DoublePair> tilePairs, int tileRow, int tileColumn) {
        for (int row = tileRow + 1; row < maxRows; row++) {
            ITile southTile = tiles[row][tileColumn];
            if (!southTile.isEmpty()) {
                List<DoublePair> southTilePairs = southTile.getValuePairs();
                compareWithSouthTile(tilePairs, southTilePairs);
            }
        }
    }

    protected void compareWithSouthTile(List<DoublePair> tilePairs, List<DoublePair> southTilePairs) {
        southTilePairs.sort(Comparator.comparingDouble(DoublePair::getX));
        double southPairsCount = southTilePairs.size();

        for (DoublePair referencePair : tilePairs) {
            double concordant = 0, discordant = 0, tiedOnX = 0;

            double referenceTileXValue = referencePair.getX();
            for (DoublePair southPair : southTilePairs) {
                double southTileXValue = southPair.getX();

                if (referenceTileXValue < southTileXValue)
                    break; // Break for the rest are discordant
                else if (referenceTileXValue > southTileXValue)
                    concordant++;
                else
                    tiedOnX++;
            }
            discordant = southPairsCount - concordant - tiedOnX;

            correlationStats.incrementConcordantCount(concordant);
            correlationStats.incrementDiscordantCount(discordant);
            correlationStats.incrementTiedXCount(tiedOnX);
        }
    }

    protected void compareWithEastTile(List<DoublePair> tilePairs, List<DoublePair> eastTilePairs) {
        eastTilePairs.sort(Comparator.comparingDouble(DoublePair::getY));
        double eastPairsCount = eastTilePairs.size();

        for (DoublePair referencePair : tilePairs) {
            double concordant = 0, discordant = 0, tiedOnY = 0;
            double referenceTileYValue = referencePair.getY();

            for (DoublePair eastPair : eastTilePairs) {
                double eastTileYValue = eastPair.getY();

                if (referenceTileYValue < eastTileYValue)
                    break; // Break for the rest are concordant
                else if (referenceTileYValue > eastTileYValue)
                    discordant++;
                else
                    tiedOnY++;
            }
           concordant = eastPairsCount - discordant - tiedOnY;

            correlationStats.incrementConcordantCount(concordant);
            correlationStats.incrementDiscordantCount(discordant);
            correlationStats.incrementTiedYCount(tiedOnY);
        }
    }

    protected void compareValuePairs(DoublePair pair1, DoublePair pair2) {
        double x1 = pair1.getX(), y1 = pair1.getY();
        double x2 = pair2.getX(), y2 = pair2.getY();

        if ((x1 < x2 && y1 < y2) || (x1 > x2 && y1 > y2)) {
            correlationStats.incrementConcordantCount();
        } else if ((x1 < x2 && y1 > y2) || (x1 > x2 && y1 < y2)) {
            correlationStats.incrementDiscordantCount();
        } else if (x1 == x2 && y1 != y2) {
            correlationStats.incrementTiedXCount();
        } else if (x1 != x2 && y1 == y2) {
            correlationStats.incrementTiedYCount();
        }
    }

    protected void processNonCrossTiles(int tilePairCount, int tileRow, int tileColumn) {
        processSouthEastTiles(tilePairCount, tileRow, tileColumn);
        processSouthWestTiles(tilePairCount, tileRow, tileColumn);
    }

    protected void processSouthEastTiles(int tilePairsCount, int tileRow, int tileColumn) {
        int southEastTilesPairsCount;

        for (int row = tileRow + 1; row < maxRows; row++) {
            for (int column = tileColumn + 1; column < maxColumns; column++) {
                if (tiles[row][column].isEmpty()) {
                    continue;  // Skip empty tile
                }
                southEastTilesPairsCount = tiles[row][column].getCount();
                correlationStats.incrementDiscordantCount(tilePairsCount * southEastTilesPairsCount);
            }
        }
    }

    protected void processSouthWestTiles(int tilePairCount, int tileRow, int tileColumn) {
        int southWestTilePairsCount;

        for (int row = tileRow + 1; row < maxRows; row++) {
            for (int column = tileColumn - 1; column >= 0; column--) {
                if (tiles[row][column].isEmpty()) {
                    continue;  // Skip empty tile
                }

                southWestTilePairsCount = tiles[row][column].getCount();
                correlationStats.incrementConcordantCount(tilePairCount * southWestTilePairsCount);
            }
        }
    }
}