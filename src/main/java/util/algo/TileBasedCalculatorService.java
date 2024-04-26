package util.algo;


import util.tilemgr.ITilesManager;
import util.tiles.ITile;

public class TileBasedCalculatorService {
	protected static final boolean DEBUG_FLAG = false;

    private ITilesManager tilesManager;


    public TileBasedCalculatorService(ITilesManager tilesManager) {
        this.tilesManager = tilesManager;
    }

    public double calculateKendallTauCorrelation() {
        CorrelationStatistics statistics = new CorrelationStatistics();
        CalculationTimer timer = new CalculationTimer();

        ITile[][] tiles = tilesManager.createTilesArray();
        TileProcessor processor = new TileProcessor(tiles, statistics);
        processor.processAllTiles();

        if(DEBUG_FLAG) {
        	System.out.println(statistics);
        	System.out.println(timer);
        }
        return statistics.calculateCorrelationResult();
    }
}


