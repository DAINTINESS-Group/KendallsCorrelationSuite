package tileBasedKendallAlgorithms.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import tileBasedKendallAlgorithms.tiles.Tile;
import tileBasedKendallAlgorithms.tiles.TilesManager;

public class TileBasedCalculatorService {

    private final IBinCalculator binCalculator;
    private Dataset<Row> dataset;
    private final String column1;
    private final String column2;
    private Tile[][] tiles;
    private final CorrelationStatistics statistics = new CorrelationStatistics();

    public TileBasedCalculatorService(Dataset<Row> dataset, IBinCalculator binCalculator, String column1, String column2) {
        this.binCalculator = binCalculator;
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
        prepareDataset();
        setupTiles();
    }

    private void prepareDataset() {
        convertColumnsToDouble();
    }

    private void convertColumnsToDouble() {
        dataset = dataset.select(
                dataset.col(column1).cast("double").as(column1),
                dataset.col(column2).cast("double").as(column2)
        );
    }

    private void setupTiles() {
        TilesManager tilesManager = new TilesManager(dataset, column1, column2, binCalculator);
        tiles = tilesManager.createTilesArray();
    }

    public double calculateKendallTauCorrelation() {
        TileProcessor processor = new TileProcessor(tiles, statistics);
        CalculationTimer timer = new CalculationTimer();

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
