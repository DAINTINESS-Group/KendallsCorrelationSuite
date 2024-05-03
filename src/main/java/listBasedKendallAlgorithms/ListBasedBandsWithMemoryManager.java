package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import util.algo.TilesWithCountersBandsWithMemoryCalculatorService;
import util.tilemgr.TilesManagerListBasedTilesWithCounters;

public class ListBasedBandsWithMemoryManager implements IListBasedKendallCalculator{

	@Override
	public double calculateKendall(ColumnPair pair) {
		
		TilesManagerListBasedTilesWithCounters tilesManager = new TilesManagerListBasedTilesWithCounters(pair); 
//		TilesManagerListBasedTilesWithCounters tilesManager = new TilesManagerListBasedTilesWithCounters(pair, 
//				TilesManagerListBasedTilesWithCounters.RangeMakerMethodEnum.FIXED);
        TilesWithCountersBandsWithMemoryCalculatorService calculatorService = new TilesWithCountersBandsWithMemoryCalculatorService(tilesManager);
        return calculatorService.calculateKendallTauCorrelation();	}

}
