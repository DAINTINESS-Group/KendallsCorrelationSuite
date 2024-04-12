package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import listBasedKendallAlgorithms.listBasedTiles.TileBasedCalculatorService;

public class ListBasedTileBasedKendallManager implements IListBasedKendallCalculator{

	@Override
	public double calculateKendall(ColumnPair pair) {
        TileBasedCalculatorService calculatorService = new TileBasedCalculatorService(pair);
        return calculatorService.calculateKendallTauCorrelation();	}

}
