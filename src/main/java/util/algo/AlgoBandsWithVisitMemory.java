package util.algo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
//import java.util.TreeMap;

import util.common.DoublePair;
import util.tileband.TileBandSimple;
import util.tiles.TileWithCounters;

/**
 * The {@code AlgoBandsWithVisitMemory} class is responsible for processing tiles in a tile-based approach to calculating Kendall's tau correlation.
 * TO FIX TO FIX TO FIX
 * It involves comparing tiles with themselves and with neighboring tiles (East, South, SouthEast, SouthWest) to count
 * concordant, discordant, and tied pairs of observations. These counts contribute to the calculation of the
 * Kendall's tau correlation coefficient.
 * <p>
 * EXPLAIN THE LOGIC !!!
 *
 * @author pvassil
 */

public class AlgoBandsWithVisitMemory {
	protected static final boolean DEBUG_FLAG = false;

	//TODO FIX ALL!!!!!!!
	private final TileWithCounters[][] tiles;
	private final CorrelationStatistics correlationStats;
	private final int maxColumns;
	private final int maxRows;
	private final List<TileBandSimple> rowMetadata;
	private final List<TileBandSimple> columnMetadata;

	public AlgoBandsWithVisitMemory(TileWithCounters[][] tiles, CorrelationStatistics correlationStats) {
		//System.out.println("\n -- \n creating AlgoBandsWithVisitMemory");
		this.tiles = tiles;
		this.correlationStats = correlationStats;
		maxRows = tiles.length;
		maxColumns = tiles[0].length;

		this.rowMetadata = new ArrayList<TileBandSimple>();
		for(int i = 0; i < maxRows; i++) {        	
			this.rowMetadata.add(new TileBandSimple(TileBandSimple.BandType.ROW, i));
		}
		this.columnMetadata = new ArrayList<TileBandSimple>();
		for(int j = 0; j < maxColumns; j++) {        	
			this.columnMetadata.add(new TileBandSimple(TileBandSimple.BandType.COLUMN, j));
		}
		
		preprocessAllTiles();
	}//end constructor

	protected void preprocessAllTiles() {
		for (TileWithCounters[] rowOfTiles : tiles) {
			
			for (TileWithCounters tile : rowOfTiles) {
				if (!tile.isEmpty()) {
	if(DEBUG_FLAG) {
		System.err.println(tile.toStringDetailed());
	}
					int tileRow = tile.getRow();
					int tileColumn = tile.getColumn();
					SortedMap<Double, Integer> tileOccurrenceMapForYValues = tile.getOccurencesPerY();
					SortedMap<Double, Integer> rowOccurrenceMapY = rowMetadata.get(tileRow).getOccurencesPerValue();
					mergeAll(rowOccurrenceMapY, tileOccurrenceMapForYValues);
					
					SortedMap<Double, Integer> tileOccurrenceMapForXValues= tile.getOccurencesPerX();
					SortedMap<Double, Integer> columnOccurrenceMapX = columnMetadata.get(tileColumn).getOccurencesPerValue();
					mergeAll(columnOccurrenceMapX, tileOccurrenceMapForXValues);
				}
			}
		}
		if(DEBUG_FLAG) {
			shortReportBandMetadataStatus();
		}
	}//end preprocess
	
	protected void mergeAll(SortedMap<Double, Integer> host, SortedMap<Double, Integer> provider) {
		Set<Double> providerKeys = provider.keySet(); //ordered asc
		for(Double d: providerKeys) {
			Integer newValue = provider.get(d);
			if(host.containsKey(d))
				newValue += host.get(d); 
			host.put(d, newValue);
		}
	}

	protected void shortReportBandMetadataStatus() {
		for(TileBandSimple rmd: rowMetadata) {
			System.out.println(rmd.toString());
		}
		for(TileBandSimple rmd: columnMetadata) {
			System.out.println(rmd.toString());
		}
	}

	public void processAllTiles() {
		CalculationTimer.reset();
		for (TileWithCounters[] rowOfTiles : tiles) {
			for (TileWithCounters tile : rowOfTiles) {
				if (!tile.isEmpty()) {
					processTile(tile);
				}
			}
		}
		// all  counters must have been reduced to zeros
		//by continuously subtracting with the counters of this tile !!!!!!!!!!!!!!!!!!!!!
		if(DEBUG_FLAG) {
			shortReportBandMetadataStatus();
		}
	}//end processAllTiles

	protected void processTile(TileWithCounters tile) {    	
		int tileRow = tile.getRow();
		int tileColumn = tile.getColumn();
		int tilePairsCount = tile.getCount();
		List<DoublePair> tilePairs = tile.getValuePairs();
//System.err.printf("@@@@PROCESSING [%d][%d]\n", tileRow,tileColumn);

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
		compareTileWithEastTiles(tile);
		//compareTileWithEastTiles(tilePairs, tileRow, tileColumn);
			endTime = System.currentTimeMillis();
			elapsedTimeSeconds = (endTime - startTime) / 1000.0;
			CalculationTimer.incrementCompareWithEastTime(elapsedTimeSeconds);

			startTime = System.currentTimeMillis();
		compareTileWithSouthTiles(tile);
		//compareTileWithSouthTiles(tilePairs, tileRow, tileColumn);
			endTime = System.currentTimeMillis();
			elapsedTimeSeconds = (endTime - startTime) / 1000.0;
			CalculationTimer.incrementCompareWithSouthTime(elapsedTimeSeconds);

			startTime = System.currentTimeMillis();
		processNonCrossTiles(tilePairsCount, tileRow, tileColumn);
			endTime = System.currentTimeMillis();
			elapsedTimeSeconds = (endTime - startTime) / 1000.0;
			CalculationTimer.incrementCompareWithNonCrossTime(elapsedTimeSeconds);
	
		//must have zero values
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

	
	protected void maintainMetadataCounters(SortedMap<Double, Integer> host, SortedMap<Double, Integer> testedTile) {
		Set<Double> testedTileKeys = testedTile.keySet(); //ordered asc
		for(Double d: testedTileKeys) {
			Integer hostOccurrences = (host.get(d) == null ? 0 : host.get(d));
			Integer testedTileOccurrences = testedTile.get(d);
			Integer newValue = hostOccurrences - testedTileOccurrences;
	        boolean replacedFlag = host.replace(d, hostOccurrences, newValue);
	        if(!replacedFlag || newValue < 0) {
	        	System.err.println("AlgoBandsWithVisitMemory: Error in metadata maintainance");
	        	System.err.println("key-value: " + d + " host: " + hostOccurrences + " local: " + testedTileOccurrences);
	        	System.exit(-1);
	        }
		}
	}

	protected void compareTileWithSouthTiles(TileWithCounters testedTile) {
		//int tileRow = testedTile.getRow();
		int tileColumn = testedTile.getColumn();

		SortedMap<Double, Integer> tileOccurrenceMapForXValues = testedTile.getOccurencesPerX();
		SortedMap<Double, Integer> colOccurrenceMapX = columnMetadata.get(tileColumn).getOccurencesPerValue();
		Set<Double> tileKeys = tileOccurrenceMapForXValues.keySet();
		
		maintainMetadataCounters(colOccurrenceMapX, tileOccurrenceMapForXValues);
		
		for(Double d: tileKeys) {
			Integer localOccurrencesOfD = tileOccurrenceMapForXValues.get(d);
		//	Integer bandOccurrencesOfD = rowOccurrenceMapY.get(d);
			
			//strictly smaller than d are concordant
			SortedMap<Double, Integer> concordantKeys = colOccurrenceMapX.headMap(d);
			for(Double dd: concordantKeys.keySet()) {
				correlationStats.incrementConcordantCount(localOccurrencesOfD*colOccurrenceMapX.get(dd));
			}

			//ties
			Integer tiedOnXAtBand = colOccurrenceMapX.get(d);
			correlationStats.incrementTiedXCount(localOccurrencesOfD*tiedOnXAtBand);

			//greater or equal, remember to remove equal in the end
			SortedMap<Double, Integer> discordantKeys = colOccurrenceMapX.tailMap(d);
			for(Double dd: discordantKeys.keySet()) {
				correlationStats.incrementDiscordantCount(localOccurrencesOfD*colOccurrenceMapX.get(dd));
			}
			correlationStats.incrementDiscordantCount(-localOccurrencesOfD*colOccurrenceMapX.get(d));
						
		}//endFor
	}//end East
	
	
	protected void compareTileWithEastTiles(TileWithCounters testedTile) {
		int tileRow = testedTile.getRow();
		//int tileColumn = testedTile.getColumn();

		SortedMap<Double, Integer> tileOccurrenceMapForYValues = testedTile.getOccurencesPerY();
		SortedMap<Double, Integer> rowOccurrenceMapY = rowMetadata.get(tileRow).getOccurencesPerValue();
		Set<Double> tileKeys = tileOccurrenceMapForYValues.keySet();
		
		maintainMetadataCounters(rowOccurrenceMapY, tileOccurrenceMapForYValues);
		
		for(Double d: tileKeys) {
			Integer localOccurrencesOfD = tileOccurrenceMapForYValues.get(d);
		//	Integer bandOccurrencesOfD = rowOccurrenceMapY.get(d);
			
			//strictly smaller than d are discordant
			SortedMap<Double, Integer> discordantKeys = rowOccurrenceMapY.headMap(d);
			for(Double dd: discordantKeys.keySet()) {
				correlationStats.incrementDiscordantCount(localOccurrencesOfD*rowOccurrenceMapY.get(dd));
			}

			//ties
			Integer tiedOnYAtBand = rowOccurrenceMapY.get(d);
			correlationStats.incrementTiedYCount(localOccurrencesOfD*tiedOnYAtBand);

			//greater or equal, remember to remove equal in the end
			SortedMap<Double, Integer> concordantKeys = rowOccurrenceMapY.tailMap(d);
			for(Double dd: concordantKeys.keySet()) {
				correlationStats.incrementConcordantCount(localOccurrencesOfD*rowOccurrenceMapY.get(dd));
			}
			correlationStats.incrementConcordantCount(-localOccurrencesOfD*rowOccurrenceMapY.get(d));
						
		}//endFor
	}//end East
	
//	protected void compareTileWithEastTiles(TileWithCounters testedTile) {
//		int tileRow = testedTile.getRow();
//		//int tileColumn = testedTile.getColumn();
//		
//		SortedMap<Double, Integer> tileOccurrenceMapForYValues = testedTile.getOccurencesPerX();
//		SortedMap<Double, Integer> rowOccurrenceMapY = rowMetadata.get(tileRow).getOccurencesPerValue();
//		Set<Double> tileKeys = tileOccurrenceMapForYValues.keySet();
//		for(Double d: tileKeys) {
//			Integer localOccurrencesOfD = tileOccurrenceMapForYValues.get(d);
//			Integer bandOccurrencesOfD = rowOccurrenceMapY.get(d);
//			
//			//strictly smaller than d are discordant
//			SortedMap<Double, Integer> discordantKeys = rowOccurrenceMapY.headMap(d);
//			for(Double dd: discordantKeys.keySet()) {
//				correlationStats.incrementDiscordantCount(discordantKeys.get(dd));
//			}
//
//			//ties
//			Integer tiedOnYAtBand = bandOccurrencesOfD - localOccurrencesOfD;
//			correlationStats.incrementTiedYCount(tiedOnYAtBand);
//
//			//greater or equal
//			SortedMap<Double, Integer> concordantKeys = rowOccurrenceMapY.tailMap(d);
//			for(Double dd: concordantKeys.keySet()) {
//				correlationStats.incrementConcordantCount(concordantKeys.get(dd));
//			}
//			correlationStats.incrementConcordantCount(-bandOccurrencesOfD);
//			
//		}//endFor
//		
//		maintainMetadataCounters(rowOccurrenceMapY, tileOccurrenceMapForYValues);
//	}//end East
	
	
	
	@Deprecated
	protected void compareTileWithEastTiles(List<DoublePair> tilePairs, int tileRow, int tileColumn) {
		for (int column = tileColumn + 1; column < maxColumns; column++) {
			TileWithCounters eastTile = tiles[tileRow][column];
			if (!eastTile.isEmpty()) {
				List<DoublePair> eastTilePairs = eastTile.getValuePairs();
				compareWithEastTile(tilePairs, eastTilePairs);
			}
		}
	}

	@Deprecated
	protected void compareWithEastTile(List<DoublePair> tilePairs, List<DoublePair> eastTilePairs) {
		eastTilePairs.sort(Comparator.comparingDouble(DoublePair::getY));
		double eastPairsCount = eastTilePairs.size();

		for (DoublePair referencePair : tilePairs) {
			double concordant = 0, discordant = 0, tiedOnY = 0;
			double referenceTileYValue = referencePair.getY();

			for (DoublePair eastPair : eastTilePairs) {
				double eastTileYValue = eastPair.getY();

				if (referenceTileYValue < eastTileYValue) {
					break; // Break for the rest are concordant
				}
				else if (referenceTileYValue > eastTileYValue) {
					discordant++;
				}
				else {
					tiedOnY++;
//System.err.println("#########Same y: " + referenceTileYValue + "\t\t" + eastTileYValue);	
					
				}
			}
			concordant = eastPairsCount - discordant - tiedOnY;

			correlationStats.incrementConcordantCount(concordant);
			correlationStats.incrementDiscordantCount(discordant);
			correlationStats.incrementTiedYCount(tiedOnY);
		}
	}
	
	protected void compareTileWithSouthTiles(List<DoublePair> tilePairs, int tileRow, int tileColumn) {
		for (int row = tileRow + 1; row < maxRows; row++) {
			TileWithCounters southTile = tiles[row][tileColumn];
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



	protected void compareValuePairs(DoublePair pair1, DoublePair pair2) {
		double x1 = pair1.getX(), y1 = pair1.getY();
		double x2 = pair2.getX(), y2 = pair2.getY();

		if ((x1 < x2 && y1 < y2) || (x1 > x2 && y1 > y2)) {
			correlationStats.incrementConcordantCount();
		} else if ((x1 < x2 && y1 > y2) || (x1 > x2 && y1 < y2)) {
			correlationStats.incrementDiscordantCount();
		} else if (x1 == x2 && y1 != y2) {
			correlationStats.incrementTiedXCount();
//System.err.println("Same x: " + x1);			
		} else if (x1 != x2 && y1 == y2) {
			correlationStats.incrementTiedYCount();
//System.err.println("@@@@@@@@@@@@@@@@@@Same y: " + y1);	
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