package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import util.algo.TileBasedCalculatorService;
import util.tilemgr.ITilesManager;
import util.tilemgr.TilesManagerListBased;

public class ListBasedTileBasedKendallManager implements IListBasedKendallCalculator{

	@Override
	public double calculateKendall(ColumnPair pair) {
		ITilesManager tilesManager = new TilesManagerListBased(pair);
        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(tilesManager);
        return calculatorService.calculateKendallTauCorrelation();	}

}
