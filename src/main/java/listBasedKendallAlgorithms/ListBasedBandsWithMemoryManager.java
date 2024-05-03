package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import util.algo.TilesWithCountersBandsWithMemoryCalculatorService;
import util.tilemgr.TilesManagerListReaderTilesInMemWCounters;

public class ListBasedBandsWithMemoryManager implements IListBasedKendallCalculator{

	@Override
	public double calculateKendall(ColumnPair pair) {
		
		TilesManagerListReaderTilesInMemWCounters tilesManager = new TilesManagerListReaderTilesInMemWCounters(pair); 
//		TilesManagerListReaderTilesInMemWCounters tilesManager = new TilesManagerListReaderTilesInMemWCounters(pair, 
//				TilesManagerListReaderTilesInMemWCounters.RangeMakerMethodEnum.FIXED);
        TilesWithCountersBandsWithMemoryCalculatorService calculatorService = new TilesWithCountersBandsWithMemoryCalculatorService(tilesManager);
        return calculatorService.calculateKendallTauCorrelation();	}

}
