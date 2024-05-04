package listBasedKendallAlgorithms;

import common.ColumnPair;
import tiles.algos.AlgoBandsWithVisitMemory;
import tiles.algos.CalculationTimer;
import tiles.algos.CorrelationStatistics;
import tiles.dom.TileInMemWCounters;
import tiles.tilemgr.TilesManagerListReaderTilesInMemWCounters;

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
	protected static final boolean DEBUG_FLAG = false;
	
	@Override
	public double calculateKendall(ColumnPair pair) {
		
		TilesManagerListReaderTilesInMemWCounters tilesManager = new TilesManagerListReaderTilesInMemWCounters(pair); 

//        TilesWithCountersBandsWithMemoryCalculatorService calculatorService = new TilesWithCountersBandsWithMemoryCalculatorService(tilesManager);
//        return calculatorService.calculateKendallTauCorrelation();	}

	CorrelationStatistics statistics = new CorrelationStatistics();
	CalculationTimer timer = new CalculationTimer();

	TileInMemWCounters[][] tiles = tilesManager.createTilesArray();

	AlgoBandsWithVisitMemory processorWithMemory = new AlgoBandsWithVisitMemory(tiles, statistics);
	processorWithMemory.processAllTiles();
	if(DEBUG_FLAG) {
		System.out.println(statistics);
		System.out.println(timer);
	}
	return statistics.calculateCorrelationResult();
}
	
}
