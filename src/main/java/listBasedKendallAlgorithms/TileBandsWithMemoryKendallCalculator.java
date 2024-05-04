package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.reader.ColumnPair;
import util.algo.TilesWithCountersBandsWithMemoryCalculatorService;
import util.tilemgr.TilesManagerListReaderTilesInMemWCounters;

/**
 * Tiles: InMem with Counters
 * TilesManager: ListReader _ InMemTiles
 * Tiles Processing Algo: Bands with Memory
 * 
 * 
 * @author pvassil
 *
 */
public class TileBandsWithMemoryKendallCalculator implements IListBasedKendallCalculator{

	@Override
	public double calculateKendall(ColumnPair pair) {
		
		TilesManagerListReaderTilesInMemWCounters tilesManager = new TilesManagerListReaderTilesInMemWCounters(pair); 
//		TilesManagerListReaderTilesInMemWCounters tilesManager = new TilesManagerListReaderTilesInMemWCounters(pair, 
//				TilesManagerListReaderTilesInMemWCounters.RangeMakerMethodEnum.FIXED);
        TilesWithCountersBandsWithMemoryCalculatorService calculatorService = new TilesWithCountersBandsWithMemoryCalculatorService(tilesManager);
        return calculatorService.calculateKendallTauCorrelation();	}

}
