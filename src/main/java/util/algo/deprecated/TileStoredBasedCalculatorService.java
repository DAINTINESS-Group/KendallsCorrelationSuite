package util.algo.deprecated;


import util.algo.AlgoSimpleTilesAndPointComparison;
import util.algo.CalculationTimer;
import util.algo.CorrelationStatistics;
import util.tilemgr.TilesManagerSparkReaderTilesStoredSimple;
import util.tiles.TileStoredSimple;

@Deprecated
public class TileStoredBasedCalculatorService {
	protected static final boolean DEBUG_FLAG = false;

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


