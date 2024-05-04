package tiles.algos.deprecated;

import tiles.algos.AlgoBandsWithVisitMemory;
import tiles.algos.CalculationTimer;
import tiles.algos.CorrelationStatistics;
import tiles.dom.TileInMemWCounters;
import tiles.tilemgr.TilesManagerListReaderTilesInMemWCounters;

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


