package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.reader.ColumnPair;
import util.algo.AlgoEnum;
import util.algo.TileBasedCalculatorService;
import util.tilemgr.ITilesManager;
import util.tilemgr.TilesManagerListReaderTilesInMemSimple;

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

	@Override
	public double calculateKendall(ColumnPair pair) {
		ITilesManager tilesManager = new TilesManagerListReaderTilesInMemSimple(pair);
        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(tilesManager);
        return calculatorService.calculateKendallTauCorrelation(AlgoEnum.SIMPLE_TILES_AND_POINT_COMPARISONS);	}

}
