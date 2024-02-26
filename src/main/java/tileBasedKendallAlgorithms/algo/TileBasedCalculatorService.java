package tileBasedKendallAlgorithms.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import tileBasedKendallAlgorithms.tiles.Tile;
import tileBasedKendallAlgorithms.tiles.TilesManager;

public class TileBasedCalculatorService {
    private final Dataset<Row> dataset;
    private final String column1;
    private final String column2;

    public TileBasedCalculatorService(Dataset<Row> dataset, String column1, String column2) {
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
    }

    public double calculateKendallTauCorrelation() {
        CorrelationStatistics statistics = new CorrelationStatistics();
        CalculationTimer timer = new CalculationTimer();

        TilesManager tilesManager = new TilesManager(dataset, column1, column2);
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
