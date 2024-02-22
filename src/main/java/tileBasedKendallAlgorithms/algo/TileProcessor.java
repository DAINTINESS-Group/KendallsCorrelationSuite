package tileBasedKendallAlgorithms.algo;

import tileBasedKendallAlgorithms.tiles.DoublePair;
import tileBasedKendallAlgorithms.tiles.Tile;

import java.util.Comparator;
import java.util.List;

public class TileProcessor {

    private final Tile[][] tiles;
    private final CorrelationStatistics correlationStats;
    private final int maxColumns;
    private final int maxRows;

    public TileProcessor(Tile[][] tiles, CorrelationStatistics correlationStats) {
        this.tiles = tiles;
        this.correlationStats = correlationStats;
        maxRows = tiles.length;
        maxColumns = tiles[0].length;
    }

    public void processTile(Tile tile) {
        int tileRow = tile.getRow();
        int tileColumn = tile.getColumn();
        double tilePairsCount = tile.getCount();
        List<DoublePair> tilePairs = tile.getValuePairs();

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

    private void compareTileWithSelf(List<DoublePair> tilePairs, double tilePairsCount) {
        for (int i = 0; i < tilePairsCount - 1; i++) {
            DoublePair pair1 = tilePairs.get(i);
            for (int j = i + 1; j < tilePairsCount; j++) {
                DoublePair pair2 = tilePairs.get(j);
                compareValuePairs(pair1, pair2);
            }
        }
    }

    private void compareTileWithEastTiles(List<DoublePair> tilePairs, int tileRow, int tileColumn) {
        for (int column = tileColumn + 1; column < maxColumns; column++) {
            Tile eastTile = tiles[tileRow][column];
            if (!eastTile.isEmpty()) {
                List<DoublePair> eastTilePairs = eastTile.getValuePairs();
                compareWithEastTile(tilePairs, eastTilePairs);
            }
        }
    }

    private void compareTileWithSouthTiles(List<DoublePair> tilePairs, int tileRow, int tileColumn) {
        for (int row = tileRow + 1; row < maxRows; row++) {
            Tile southTile = tiles[row][tileColumn];
            if (!southTile.isEmpty()) {
                List<DoublePair> southTilePairs = southTile.getValuePairs();
                compareWithSouthTile(tilePairs, southTilePairs);
            }
        }
    }

    private void compareWithSouthTile(List<DoublePair> tilePairs, List<DoublePair> southTilePairs) {
        southTilePairs.sort(Comparator.comparingDouble(DoublePair::getX));
        double southPairsCount = southTilePairs.size();

        for (DoublePair referencePair : tilePairs) {
            double concordant = 0, discordant = 0, tiedOnX = 0;
            double referenceTileXValue = referencePair.getX();

            for (DoublePair southPair : southTilePairs) {
                double southTileXValue = southPair.getX();

                if (referenceTileXValue < southTileXValue)
                    break; // Break for the rest are concordant
                else if (referenceTileXValue > southTileXValue)
                    discordant++;
                else
                    tiedOnX++;

            }
            concordant = southPairsCount - discordant - tiedOnX;

            correlationStats.incrementConcordantCount(concordant);
            correlationStats.incrementDiscordantCount(discordant);
            correlationStats.incrementTiedXCount(tiedOnX);
        }
    }

    private void compareWithEastTile(List<DoublePair> tilePairs, List<DoublePair> eastTilePairs) {
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

    private void compareValuePairs(DoublePair pair1, DoublePair pair2) {
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

    private void processNonCrossTiles(double tilePairCount, int tileRow, int tileColumn) {
        processSouthEastTiles(tilePairCount, tileRow, tileColumn);
        processSouthWestTiles(tilePairCount, tileRow, tileColumn);
    }

    private void processSouthEastTiles(double tilePairsCount, int tileRow, int tileColumn) {
        double southEastTilesPairsCount;

        for (int row = tileRow + 1; row < maxRows; row++) {
            for (int column = tileColumn + 1; column < maxColumns; column++) {
                if (tiles[row][column].isEmpty()) {
                    continue;  // Skip empty tile
                }
                southEastTilesPairsCount = tiles[row][column].getCount();
                correlationStats.incrementConcordantCount(tilePairsCount * southEastTilesPairsCount);
            }
        }
    }

    private void processSouthWestTiles(double tilePairCount, int tileRow, int tileColumn) {
        double southWestTilePairsCount;

        for (int row = tileRow + 1; row < maxRows; row++) {
            for (int column = tileColumn - 1; column >= 0; column--) {
                if (tiles[row][column].isEmpty()) {
                    continue;  // Skip empty tile
                }

                southWestTilePairsCount = tiles[row][column].getCount();
                correlationStats.incrementDiscordantCount(tilePairCount * southWestTilePairsCount);
            }
        }
    }
}