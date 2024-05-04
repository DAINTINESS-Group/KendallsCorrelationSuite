package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.reader.ColumnPair;
//import util.algo.AlgoEnum;
import util.algo.AlgoSimpleTilesAndPointComparison;
import util.algo.CalculationTimer;
import util.algo.CorrelationStatistics;
//import util.algo.TileBasedCalculatorService;
import util.tilemgr.ITilesManager;
import util.tilemgr.TilesManagerListReaderTilesInMemSimple;
import util.tiles.ITile;

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

