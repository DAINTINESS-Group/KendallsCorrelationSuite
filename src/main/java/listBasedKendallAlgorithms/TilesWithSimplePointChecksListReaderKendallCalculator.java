package listBasedKendallAlgorithms;

import common.ColumnPair;
import tiles.algos.AlgoSimpleTilesAndPointComparison;
import tiles.algos.CalculationTimer;
import tiles.algos.CorrelationStatistics;
import tiles.dom.ITile;
import tiles.tilemgr.ITilesManager;
import tiles.tilemgr.TilesManagerListReaderTilesInMemSimple;

/**
 * Tiles: Simple (InMem with Simple structure)
 * TilesManager: List reader _  InMemSimpleTiles
 * Tiles Processing Algo: Simple points and checks
 * 
 * 
 * @author pvassil
 *
 */
public class TilesWithSimplePointChecksListReaderKendallCalculator implements IListBasedKendallCalculator{
	protected static final boolean DEBUG_FLAG = false;
	//	private ITilesManager tilesManager;

	@Override
	public double calculateKendall(ColumnPair pair) {
		ITilesManager tilesManager = new TilesManagerListReaderTilesInMemSimple(pair);
		//        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(tilesManager);
		//        return calculatorService.calculateKendallTauCorrelation(AlgoEnum.SIMPLE_TILES_AND_POINT_COMPARISONS);	
		//		return calculateKendallTauCorrelation();
		//	}
		//
		//
		//
		//public double calculateKendallTauCorrelation() {
		CorrelationStatistics statistics = new CorrelationStatistics();
		CalculationTimer timer = new CalculationTimer();

		ITile[][] tiles = tilesManager.createTilesArray();


		AlgoSimpleTilesAndPointComparison processorSimple = new AlgoSimpleTilesAndPointComparison(tiles, statistics);
		processorSimple.processAllTiles();
		if(DEBUG_FLAG) {
			System.out.println(statistics);
			System.out.println(timer);
		}
		return statistics.calculateCorrelationResult();
	}

}

