package util.algo;


import util.tilemgr.ITilesManager;
import util.tiles.ITile;

public class TileBasedCalculatorService {
	protected static final boolean DEBUG_FLAG = false;

    private ITilesManager tilesManager;


    public TileBasedCalculatorService(ITilesManager tilesManager) {
        this.tilesManager = tilesManager;
    }

    public double calculateKendallTauCorrelation(AlgoEnum algorithm) {
        CorrelationStatistics statistics = new CorrelationStatistics();
        CalculationTimer timer = new CalculationTimer();

        ITile[][] tiles = tilesManager.createTilesArray();
        
        //TODO FIX, REFACTOR, Produce iface
        switch(algorithm) {
        case SIMPLE_TILES_AND_POINT_COMPARISONS:
        	AlgoSimpleTilesAndPointComparison processorSimple = new AlgoSimpleTilesAndPointComparison(tiles, statistics);
        	processorSimple.processAllTiles();
        	break;
        case BANDS_WITH_VISIT_MEMORY:
            AlgoBandsWithVisitMemory processorWithMemory = new AlgoBandsWithVisitMemory(tiles, statistics);
            processorWithMemory.processAllTiles();
            break;
        case SPARK_TILES_ALGO:
        	AlgoSimpleTilesAndPointComparison processorSimpleSpark = new AlgoSimpleTilesAndPointComparison(tiles, statistics);
        	processorSimpleSpark.processAllTiles();
        	break;
        default:
//            throw new IllegalArgumentException(
//                    String.format("%s is not a supported calculation method.", algorithm));
			break;
        }

        if(DEBUG_FLAG) {
        	System.out.println(statistics);
        	System.out.println(timer);
        }
        return statistics.calculateCorrelationResult();
    }
}


