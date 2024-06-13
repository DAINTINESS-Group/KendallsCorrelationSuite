package listBasedKendallAlgorithms;

import common.ColumnPair;
import tiles.algos.AlgoSimpleSorters;
import tiles.algos.CalculationTimer;
//import tiles.algos.CorrelationStatistics;
import tiles.algos.CorrelationStatisticsMergeSort;
import tiles.dom.ITile;
import tiles.tilemgr.ITilesManager;
import tiles.tilemgr.TilesManagerListReaderTilesInMemSimple;
import util.TileConstructionParameters;

/**
 * Tiles: Simple (InMem with Simple structure)
 * TilesManager: List reader _  InMemSimpleTiles
 * Tiles Processing Algo: AlgoSimpleSorters 
 * 
 * 
 * @author pvassil
 *
 */
public class TilesWithSimpleSortersListReaderKendallCalculator implements IListBasedKendallCalculator{
	protected static final boolean DEBUG_FLAG = false;
	protected TileConstructionParameters parameters;
	//	private ITilesManager tilesManager;

	public TilesWithSimpleSortersListReaderKendallCalculator(TileConstructionParameters parameters) {
		this.parameters = parameters;
	}

	@Override
	public double calculateKendall(ColumnPair pair) {
		ITilesManager tilesManager = new TilesManagerListReaderTilesInMemSimple(pair, parameters);
		int numTuples = pair.getSize();
		CorrelationStatisticsMergeSort statistics = new CorrelationStatisticsMergeSort(numTuples);
		CalculationTimer timer = new CalculationTimer();

		ITile[][] tiles = tilesManager.createTilesArray();

		AlgoSimpleSorters processorSimple = new AlgoSimpleSorters(tiles, statistics); 
		processorSimple.processAllTiles();
		if(DEBUG_FLAG) {
			System.out.println(statistics);
			System.out.println(timer);
		}
		return statistics.calculateCorrelationResult();
	}

}

