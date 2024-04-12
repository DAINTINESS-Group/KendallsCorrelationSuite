package listBasedKendallAlgorithms.listBasedTiles;

import listBasedKendallAlgorithms.IListBasedKendallCalculator;
import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import listBasedKendallAlgorithms.listBasedTiles.algo.TileBasedCalculatorService;

public class ListBasedTileBasedKendallManager implements IListBasedKendallCalculator{

	@Override
	public double calculateKendall(ColumnPair pair) {
        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(pair);
        return calculatorService.calculateKendallTauCorrelation();	}

}
