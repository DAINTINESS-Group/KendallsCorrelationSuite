package util.algo;


import util.tilemgr.TilesManagerSparkReaderTilesStoredSimple;
import util.tiles.TileStoredSimple;

public class TileStoredBasedCalculatorService {
	protected static final boolean DEBUG_FLAG = true;

	private TilesManagerSparkReaderTilesStoredSimple tilesManager;


	public TileStoredBasedCalculatorService(TilesManagerSparkReaderTilesStoredSimple tilesManager) {
		this.tilesManager = tilesManager;
	}

	public double calculateKendallTauCorrelation() throws IllegalArgumentException {
		CorrelationStatistics statistics = new CorrelationStatistics();
		CalculationTimer timer = new CalculationTimer();

		TileStoredSimple[][] tiles = tilesManager.createTilesArray();

		AlgoSimpleTilesAndPointComparison processorSimpleSpark = new AlgoSimpleTilesAndPointComparison(tiles, statistics);
		processorSimpleSpark.processAllTiles();

		if(DEBUG_FLAG) {
			System.out.println(statistics);
			System.out.println(timer);
		}
		return statistics.calculateCorrelationResult();
	}
}


