package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import util.algo.AlgoEnum;
import util.algo.TileBasedCalculatorService;
import util.tilemgr.ITilesManager;
import util.tilemgr.TilesManagerListBasedSimpleTiles;

public class ListBasedBandsWithMemoryManager implements IListBasedKendallCalculator{

	@Override
	public double calculateKendall(ColumnPair pair) {
		ITilesManager tilesManager = new TilesManagerListBasedSimpleTiles(pair);
        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(tilesManager);
        return calculatorService.calculateKendallTauCorrelation(AlgoEnum.BANDS_WITH_VISIT_MEMORY);	}

}
