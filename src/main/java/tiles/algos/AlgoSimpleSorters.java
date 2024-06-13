package tiles.algos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import tiles.dom.DoublePair;
import tiles.dom.ITile;

/**
 * The {@code AlgoSimpleSorters} class is responsible for processing tiles in a tile-based approach to calculating
 * Kendall's tau correlation. 
 * The algorithm uses simple tiles, and involves comparing every tile with 
 * (1) itself
 * (2) a subset of its cross, i.e., its "band of neighbors" at East, South, 
 * (3) its "corner" rectangles of tiles at SouthEast, SouthWest) 
 * to count concordant[c], discordant[d], and tied [t] pairs of observations. 
 * These counts contribute to the calculation of the Kendall's tau correlation coefficient.
 * <p>
 * We use a very different algo than the simple algo's so far. We sort with a very precise order.
 * 
 * ADD SORTER INFO
 * 
 * Within each tile, *** We use a mergesort algo, based on knight's algo and apache implementation
 * We practically copy-paste the code of apache for this
 *    https://github.com/apache/commons-math/blob/master/commons-math-legacy/src/main/java/org/apache/commons/math4/legacy/stat/correlation/KendallsCorrelation.java
 * <p>   
 * With respect to the cross tiles, you know in advance that the points of the band tiles have larger values
 * in one dimension, already. So, you need to check only the other dimension to determine [c,d,t]
 * The corners are straightforward: you do not check point-wise, but you immediately count the entire tile as c or d
 * <p>
 * This class is an improvement over the idea of computing every tile with (2') all the cross and (3') all the corners.
 *
 * @author pvassil
 * 
 */
public class AlgoSimpleSorters {

    private final ITile[][] tiles;
    private final CorrelationStatistics correlationStats;
    private final int maxColumns;
    private final int maxRows;

    public AlgoSimpleSorters(ITile[][] tiles, CorrelationStatistics correlationStats) {
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
                	processNonCrossAndSortByX(tile);
                }
            }
        }//@end: SW, SE, sortBy X
        for (ITile[] rowOfTiles : tiles) {
            for (ITile tile : rowOfTiles) {
                if (!tile.isEmpty()) {
                    int tileRow = tile.getRow();
                    int tileColumn = tile.getColumn();
                    //int tilePairsCount = (int)tile.getCount();
                    List<DoublePair> tilePairs = tile.getValuePairs();             	
                	compareTileWithSouthTiles(tilePairs, tileRow, tileColumn);
                	compareTileWithSelf(tile);
                	//compareTileWithSelf(tilePairs, tilePairsCount);
//System.err.println(tilePairs.toString());              	
                }
            }
        }//@end: South, self => sorted by Y
        for (ITile[] rowOfTiles : tiles) {
            for (ITile tile : rowOfTiles) {
                if (!tile.isEmpty()) {
                    int tileRow = tile.getRow();
                    int tileColumn = tile.getColumn();
//                    int tilePairsCount = (int)tile.getCount();
                    List<DoublePair> tilePairs = tile.getValuePairs();
                	compareTileWithEastTiles(tilePairs, tileRow, tileColumn);
                }
            }
        }//@end: east, still sortedBy Y        
        
    }//end processAllTiles
    
    protected void processNonCrossAndSortByX(ITile tile) {
        int tileRow = tile.getRow();
        int tileColumn = tile.getColumn();
        int tilePairsCount = (int)tile.getCount();
        List<DoublePair> tilePairs = tile.getValuePairs();
 
        if(tilePairs.size() != tilePairsCount) {
        	System.err.println("Tileprocessor.processTile error, parCount and list do not match: " +tilePairsCount + "\t" + tilePairs.size());
        	System.err.println("TileInMemSimple: " +tile.toString());
        }
    	
        processSouthEastTiles(tilePairsCount, tileRow, tileColumn);
        processSouthWestTiles(tilePairsCount, tileRow, tileColumn);
        tilePairs.sort(Comparator.comparingDouble(DoublePair::getX));
//System.err.println(tilePairs.toString());        
        
    } // end processNonCrossAndSortByX
    
//    protected void processTile(ITile tile) {    	
//        int tileRow = tile.getRow();
//        int tileColumn = tile.getColumn();
//        int tilePairsCount = (int)tile.getCount();
//        List<DoublePair> tilePairs = tile.getValuePairs();
// //System.err.println(tileRow + "\t" + tileColumn +":\t" + tilePairsCount);
// 
//        if(tilePairs.size() != tilePairsCount) {
//        	System.err.println("Tileprocessor.processTile error, parCount and list do not match: " +tilePairsCount + "\t" + tilePairs.size());
//        	System.err.println("TileInMemSimple: " +tile.toString());
//        }
//        	long startTime = System.currentTimeMillis();
//        compareTileWithSelf(tilePairs, tilePairsCount);
//        	long endTime = System.currentTimeMillis();
//        	double elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//        	CalculationTimer.incrementCompareWithSelfTime(elapsedTimeSeconds);
//
//        	startTime = System.currentTimeMillis();
//        compareTileWithEastTiles(tilePairs, tileRow, tileColumn);
//        	endTime = System.currentTimeMillis();
//        	elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//        	CalculationTimer.incrementCompareWithEastTime(elapsedTimeSeconds);
//
//        	startTime = System.currentTimeMillis();
//        compareTileWithSouthTiles(tilePairs, tileRow, tileColumn);
//        	endTime = System.currentTimeMillis();
//        	elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//        	CalculationTimer.incrementCompareWithSouthTime(elapsedTimeSeconds);
//
//        	startTime = System.currentTimeMillis();
//        processNonCrossTiles(tilePairsCount, tileRow, tileColumn);
//        	endTime = System.currentTimeMillis();
//        	elapsedTimeSeconds = (endTime - startTime) / 1000.0;
//        	CalculationTimer.incrementCompareWithNonCrossTime(elapsedTimeSeconds);
//    }

    protected void compareTileWithSouthTiles(List<DoublePair> tilePairs, int tileRow, int tileColumn) {
        for (int row = tileRow + 1; row < maxRows; row++) {
            ITile southTile = tiles[row][tileColumn];
            if (!southTile.isEmpty()) {
                List<DoublePair> southTilePairs = southTile.getValuePairs();
                //compareWithSouthTile(tilePairs, southTilePairs);
                compareSouthSMJ(tilePairs, southTilePairs);
            }
        }
    }

    protected void compareWithSouthTile(List<DoublePair> tilePairs, List<DoublePair> southTilePairs) {
    	//already sorted on X
    	//southTilePairs.sort(Comparator.comparingDouble(DoublePair::getX));
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
//System.err.println("South: " + concordant + "\t" + discordant + "\t" + (concordant - discordant ));
        }
    }

    public void compareSouthSMJ(List<DoublePair> tilePairs, List<DoublePair> southTilePairs) {
//        List<Pair<DoublePair,DoublePair>> larger = new ArrayList<>();
//        List<Pair<DoublePair,DoublePair>> smaller = new ArrayList<>();
//        List<Pair<DoublePair,DoublePair>> tied = new ArrayList<>();
        
        int cursor1 = 0, cursor2 = 0;
        int counterTieOnOtherAttribute = 0;
        int counterPairsWithOtherAttributeSmaller = 0;
        int counterPairsWithOtherAttributeLarger = 0;
        int numberTuplesTile1 = tilePairs.size();
        int numberTuplesTile2 = southTilePairs.size();
        
        // Merge phase
        while (cursor1 < numberTuplesTile1 && cursor2 < numberTuplesTile2) {
            double key1 = tilePairs.get(cursor1).getX();
            double key2 = southTilePairs.get(cursor2).getX();
//System.err.println(cursor1 + "\t" + cursor2);            
            if (key1 == key2) {
                // Find all matching rows in table1
                int startI = cursor1;
                while (cursor1 < tilePairs.size() && tilePairs.get(cursor1).getX() == key1) {
                    cursor1++;
                }
                
                // Find all matching rows in table2
                int startJ = cursor2;
                while (cursor2 < southTilePairs.size() && southTilePairs.get(cursor2).getX() == key2) {
                    cursor2++;
                }
                
                // Join each matching row from table1 with all matching rows from table2
                for (int m = startI; m < cursor1; m++) {
//                	for(int runner = cursor2; runner < numberTuplesTile2; runner++) {
//                		larger.add(new Pair<DoublePair, DoublePair>(tilePairs.get(m), southTilePairs.get(runner)));
//                	}
                    counterPairsWithOtherAttributeLarger += (numberTuplesTile2 - cursor2);
                    for (int n = startJ; n < cursor2; n++) {
//                        tied.add(new Pair<DoublePair, DoublePair>(tilePairs.get(m), southTilePairs.get(n)));
                        counterTieOnOtherAttribute++;
                    }
                }
                
                // Reset j to continue checking for more matches in table1
                cursor2 = startJ;
            } else if (key1 < key2) {
//            	for(int runner = cursor2; runner < numberTuplesTile2; runner++) {
//            		larger.add(new Pair<DoublePair, DoublePair>(tilePairs.get(cursor1), southTilePairs.get(runner)));
//            	}
                cursor1++;
                counterPairsWithOtherAttributeLarger += (numberTuplesTile2 - cursor2);
            } else {
//            	for(int runner = cursor1; runner < numberTuplesTile1; runner++) {
//            		smaller.add(new Pair<DoublePair, DoublePair>(tilePairs.get(runner), southTilePairs.get(cursor2)));
//            	}
                cursor2++;
                counterPairsWithOtherAttributeSmaller += (numberTuplesTile1 - cursor1);
            }
        }
        
//        System.err.println("\nTied Pairs:\t" + counterTieOnOtherAttribute +
//        		"\nPairs Smaller:\t" + counterPairsWithOtherAttributeSmaller +
//        		"\nPairs Larger:\t" + counterPairsWithOtherAttributeLarger + "\n");
        
        //System.err.println("\nDiscordant\t" + larger.toString());
        //System.err.println("\nConcordant\t" + smaller.toString());
        //System.err.println("\nTied\t\t" + tied.toString());
        //return tied;
        correlationStats.incrementConcordantCount(counterPairsWithOtherAttributeSmaller);
        correlationStats.incrementDiscordantCount(counterPairsWithOtherAttributeLarger);
        correlationStats.incrementTiedXCount(counterTieOnOtherAttribute);
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

    
    protected void compareWithEastTile(List<DoublePair> tilePairs, List<DoublePair> eastTilePairs) {
    	//not already sorted by self for some reason, so we sort
//System.err.println(eastTilePairs.toString());    	
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
//System.err.println("East: " + concordant + "\t" + discordant + "\t" + (concordant - discordant ));            
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
//System.err.println("SouthEast: " + (-tilePairsCount * southEastTilesPairsCount));                
            }
        }
    }

    protected void processSouthWestTiles(int tilePairsCount, int tileRow, int tileColumn) {
        int southWestTilePairsCount;

        for (int row = tileRow + 1; row < maxRows; row++) {
            for (int column = tileColumn - 1; column >= 0; column--) {
                if (tiles[row][column].isEmpty()) {
                    continue;  // Skip empty tile
                }
                southWestTilePairsCount = tiles[row][column].getCount();
                correlationStats.incrementConcordantCount(tilePairsCount * southWestTilePairsCount);
//System.err.println("SouthWest: " + (tilePairsCount * southWestTilePairsCount));                
            }
        }
    }
    
    /**
     * Directly adapting 
     *   https://github.com/apache/commons-math/blob/master/commons-math-legacy/src/main/java/org/apache/commons/math4/legacy/stat/correlation/KendallsCorrelation.java
     * Computes the Kendall's Tau rank correlation coefficient between the two arrays.
     *
     * @param tilePairs a list of DoublePair tuples of x and y measures
     * @param tilePairsCount the number of tuples, i.e., pairCounts, given
     * @return Returns Kendall's Tau rank correlation coefficient for the two arrays
     * @throws DimensionMismatchException if the arrays lengths do not match
     */
    protected void compareTileWithSelf(ITile tile){//List<DoublePair> tilePairs, int tilePairsCount) {    	
        
    	List<DoublePair> tilePairs = tile.getValuePairs();
    	int tilePairsCount = tilePairs.size();
    	
        final int n = tilePairsCount;
        final long numPairs = numOfAllPairs(n - 1);
        //if there is a single tuple in the tile, count it as a discordant
        if(1 == tilePairsCount) {	
//System.err.println("0\t0\t0\t\t1 tuple in tiles " + tilePairs.get(0).toString());
        	//correlationStats.incrementConcordantCount(-1);
        	return;
        }
        
        DoublePair [] pairs = //(DoublePair[]) tilePairs.toArray();
        		tilePairs.stream().toArray(DoublePair[] ::new);
        Arrays.sort(pairs, new Comparator<DoublePair>() {
            @Override
            public int compare(DoublePair pair1, DoublePair pair2) {
                int  compareFirst = (int) Math.signum(pair1.getX() - pair2.getX());
                return compareFirst != 0 ? compareFirst : (int)Math.signum(pair1.getY() - pair2.getY());
            }
        });
//System.err.println("NEW TILE");
//for(DoublePair p: pairs) {
//	System.err.println(p.toString());
//}
        long tiedXPairs = 0;
        long tiedXYPairs = 0;
        long consecutiveXTies = 1;
        long consecutiveXYTies = 1;
        DoublePair prev = pairs[0];
        for (int i = 1; i < n; i++) {
            final DoublePair curr = pairs[i];
            if (curr.getX() == prev.getX()) {
                consecutiveXTies++;
                if (curr.getY() == prev.getY()) {
                    consecutiveXYTies++;
                } else {
                    tiedXYPairs += numOfAllPairs(consecutiveXYTies - 1);
                    consecutiveXYTies = 1;
                }
            } else {
                tiedXPairs += numOfAllPairs(consecutiveXTies - 1);
                consecutiveXTies = 1;
                tiedXYPairs += numOfAllPairs(consecutiveXYTies - 1);
                consecutiveXYTies = 1;
            }
            prev = curr;
        }
        tiedXPairs += numOfAllPairs(consecutiveXTies - 1);
        tiedXYPairs += numOfAllPairs(consecutiveXYTies - 1);

        long swaps = 0;
        DoublePair[] pairsDestination = new DoublePair[n];
        for (int segmentSize = 1; segmentSize < n; segmentSize <<= 1) {
            for (int offset = 0; offset < n; offset += 2 * segmentSize) {
                int i = offset;
                final int iEnd = Math.min(i + segmentSize, n);
                int j = iEnd;
                final int jEnd = Math.min(j + segmentSize, n);

                int copyLocation = offset;
                while (i < iEnd || j < jEnd) {
                    if (i < iEnd) {
                        if (j < jEnd) {
                            if (pairs[i].getY() - pairs[j].getY() <= 0) {
                                pairsDestination[copyLocation] = pairs[i];
                                i++;
                            } else {
                                pairsDestination[copyLocation] = pairs[j];
//System.err.println("swaps: " + (iEnd - i) + "\t" + pairsDestination[copyLocation].toString() + "\t" + pairs[i]);
                                j++;
                                swaps += iEnd - i;
                            }
                        } else {
                            pairsDestination[copyLocation] = pairs[i];
                            i++;
                        }
                    } else {
                        pairsDestination[copyLocation] = pairs[j];
                        j++;
                    }
                    copyLocation++;
                }
            }
            final DoublePair[] pairsTemp = pairs;
            pairs = pairsDestination;
            pairsDestination = pairsTemp;
        }

        long tiedYPairs = 0;
        long consecutiveYTies = 1;
        prev = pairs[0];
        for (int i = 1; i < n; i++) {
            final DoublePair curr = pairs[i];
            if (curr.getY() == prev.getY()) {
                consecutiveYTies++;
            } else {
                tiedYPairs += numOfAllPairs(consecutiveYTies - 1);
                consecutiveYTies = 1;
            }
            prev = curr;
        }
        tiedYPairs += numOfAllPairs(consecutiveYTies - 1);

        //now tilePairs must be sorted by Y
        tilePairs = new ArrayList<>(Arrays.asList(pairs));
        
        //TODO FIX FIX FIX
        //ULTRA ISSUE: THE TILE NEVER LEARNS THE NEW LIST!
        //you cannot say 
        //		tile.setValuePairs(tilePairs);
        //this woudl mean ITile also has a setter => all its implementations should have
        //So, you have to re-sort later
        
        
        
        final long concordantMinusDiscordantAndTies = numPairs + tiedXYPairs - 2 * swaps - tiedXPairs - tiedYPairs;
//System.err.println(numPairs + "\t" + tiedXYPairs + "\t" + swaps + "\t" + concordantMinusDiscordantAndTies);
        	correlationStats.incrementConcordantCount(concordantMinusDiscordantAndTies);
        	correlationStats.incrementTiedXCount(tiedXPairs);
        	correlationStats.incrementTiedYCount(tiedYPairs);

    }

    /**
     * Returns the sum of the number from 1 .. n according to Gauss' summation formula:
     * \[ \sum\limits_{k=1}^n k = \frac{n(n + 1)}{2} \]
     *
     * @param n the summation end
     * @return the sum of the number from 1 to n
     */
    private static long numOfAllPairs(long n) {
        return n * (n + 1) / 2L;
    }
    
} //end class

