package util.algo.deprecated;

import util.algo.AlgoBandsWithVisitMemory;
import util.algo.CalculationTimer;
import util.algo.CorrelationStatistics;
import util.tilemgr.TilesManagerListReaderTilesInMemWCounters;
import util.tiles.TileInMemWCounters;

@Deprecated
public class TilesWithCountersBandsWithMemoryCalculatorService {
	protected static final boolean DEBUG_FLAG = false;

	private TilesManagerListReaderTilesInMemWCounters tilesManager;


	public TilesWithCountersBandsWithMemoryCalculatorService(TilesManagerListReaderTilesInMemWCounters tilesManager) {
		this.tilesManager = tilesManager;
	}

	public double calculateKendallTauCorrelation() {
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


