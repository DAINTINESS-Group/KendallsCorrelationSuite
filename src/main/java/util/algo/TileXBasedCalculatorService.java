package util.algo;

import util.tilemgr.TilesManagerListBasedTilesWithCounters;
import util.tiles.TileWithCounters;

public class TileXBasedCalculatorService {
	protected static final boolean DEBUG_FLAG = false;

	private TilesManagerListBasedTilesWithCounters tilesManager;


	public TileXBasedCalculatorService(TilesManagerListBasedTilesWithCounters tilesManager) {
		this.tilesManager = tilesManager;
	}

	public double calculateKendallTauCorrelation() {
		CorrelationStatistics statistics = new CorrelationStatistics();
		CalculationTimer timer = new CalculationTimer();

		TileWithCounters[][] tiles = tilesManager.createTilesArray();

		AlgoBandsWithVisitMemory processorWithMemory = new AlgoBandsWithVisitMemory(tiles, statistics);
		processorWithMemory.processAllTiles();
		if(DEBUG_FLAG) {
			System.out.println(statistics);
			System.out.println(timer);
		}
		return statistics.calculateCorrelationResult();
	}
}


