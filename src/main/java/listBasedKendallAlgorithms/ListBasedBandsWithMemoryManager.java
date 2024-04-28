package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import util.algo.TileXBasedCalculatorService;
import util.tilemgr.TilesManagerListBasedTilesWithCounters;

public class ListBasedBandsWithMemoryManager implements IListBasedKendallCalculator{

	@Override
	public double calculateKendall(ColumnPair pair) {
		
		TilesManagerListBasedTilesWithCounters tilesManager = new TilesManagerListBasedTilesWithCounters(pair); 
//		TilesManagerListBasedTilesWithCounters tilesManager = new TilesManagerListBasedTilesWithCounters(pair, 
//				TilesManagerListBasedTilesWithCounters.RangeMakerMethodEnum.FIXED);
        TileXBasedCalculatorService calculatorService = new TileXBasedCalculatorService(tilesManager);
        return calculatorService.calculateKendallTauCorrelation();	}

}