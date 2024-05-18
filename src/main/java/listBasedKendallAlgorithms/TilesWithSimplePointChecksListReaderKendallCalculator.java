package listBasedKendallAlgorithms;

import common.ColumnPair;
import tiles.algos.AlgoSimpleTilesAndPointComparison;
import tiles.algos.CalculationTimer;
import tiles.algos.CorrelationStatistics;
import tiles.dom.ITile;
import tiles.tilemgr.ITilesManager;
import tiles.tilemgr.TilesManagerListReaderTilesInMemSimple;
import util.TileConstructionParameters;

/**
 * Tiles: Simple (InMem with Simple structure)
 * TilesManager: List reader _  InMemSimpleTiles
 * Tiles Processing Algo: Simple points and checks
 * 
 * 
 * @author pvassil
 *
 */
public class TilesWithSimplePointChecksListReaderKendallCalculator implements IListBasedKendallCalculator{
	protected static final boolean DEBUG_FLAG = false;
	protected TileConstructionParameters parameters;
	//	private ITilesManager tilesManager;

	public TilesWithSimplePointChecksListReaderKendallCalculator(TileConstructionParameters parameters) {
		this.parameters = parameters;
	}

	@Override
	public double calculateKendall(ColumnPair pair) {
		CorrelationStatistics statistics = new CorrelationStatistics();
		CalculationTimer timer = new CalculationTimer();

		ITilesManager tilesManager = new TilesManagerListReaderTilesInMemSimple(pair, parameters);
		ITile[][] tiles = tilesManager.createTilesArray();

		AlgoSimpleTilesAndPointComparison processorSimple = new AlgoSimpleTilesAndPointComparison(tiles, statistics);
		processorSimple.processAllTiles();
		if(DEBUG_FLAG) {
			System.out.println(statistics);
			System.out.println(timer);
		}
		return statistics.calculateCorrelationResult();
	}

}

