package listBasedKendallAlgorithms.listBasedTiles;


import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import tileUtil.algo.CalculationTimer;
import tileUtil.algo.CorrelationStatistics;
import tileUtil.algo.TileProcessor;
import tileUtil.tiles.Tile;

public class TileBasedCalculatorService {
    private final ColumnPair pair;

    public TileBasedCalculatorService(ColumnPair pair) {
        this.pair = pair;
    }

    public double calculateKendallTauCorrelation() {
        CorrelationStatistics statistics = new CorrelationStatistics();
        CalculationTimer timer = new CalculationTimer();

        TilesManager tilesManager = new TilesManager(pair);
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
