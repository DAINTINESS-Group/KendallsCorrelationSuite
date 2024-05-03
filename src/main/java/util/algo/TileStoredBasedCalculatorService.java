package util.algo;


import util.tilemgr.TilesManagerSparkBased;
import util.tiles.TileStored;

public class TileStoredBasedCalculatorService {
	protected static final boolean DEBUG_FLAG = true;

	private TilesManagerSparkBased tilesManager;


	public TileStoredBasedCalculatorService(TilesManagerSparkBased tilesManager) {
		this.tilesManager = tilesManager;
	}

	public double calculateKendallTauCorrelation() throws IllegalArgumentException {
		CorrelationStatistics statistics = new CorrelationStatistics();
		CalculationTimer timer = new CalculationTimer();

		TileStored[][] tiles = tilesManager.createTilesArray();

		AlgoSimpleTilesAndPointComparison processorSimpleSpark = new AlgoSimpleTilesAndPointComparison(tiles, statistics);
		processorSimpleSpark.processAllTiles();

		if(DEBUG_FLAG) {
			System.out.println(statistics);
			System.out.println(timer);
		}
		return statistics.calculateCorrelationResult();
	}
}


