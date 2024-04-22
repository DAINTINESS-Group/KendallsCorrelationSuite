package util.algo;


import util.tilemgr.ITilesManager;
import util.tiles.Tile;

public class TileBasedCalculatorService {
    private ITilesManager tilesManager;


    public TileBasedCalculatorService(ITilesManager tilesManager) {
        this.tilesManager = tilesManager;
    }

    public double calculateKendallTauCorrelation() {
        CorrelationStatistics statistics = new CorrelationStatistics();
        CalculationTimer timer = new CalculationTimer();

        Tile[][] tiles = tilesManager.createTilesArray();
        TileProcessor processor = new TileProcessor(tiles, statistics);

        for (Tile[] rowOfTiles : tiles) {
            for (Tile tile : rowOfTiles) {
                if (!tile.isEmpty()) {
                    processor.processTile(tile);
                }
            }
        }

        System.out.println(statistics);
        System.out.println(timer);

        return statistics.calculateCorrelationResult();
    }
}
