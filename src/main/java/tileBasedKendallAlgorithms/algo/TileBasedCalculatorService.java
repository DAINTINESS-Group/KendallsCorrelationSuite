package tileBasedKendallAlgorithms.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import tileBasedKendallAlgorithms.tiles.Tile;
import tileBasedKendallAlgorithms.tiles.TilesManager;

public class TileBasedCalculatorService {

    private final IBinCalculator binCalculator;
    private Dataset<Row> dataset;
    private final String column1;
    private final String column2;
    private TileProcessor processor;
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

    private void setupTiles() {
        int numBinsX = determineOptimalBins(column1);
        int numBinsY = determineOptimalBins(column2);

        double rangeSizeX = computeRangeSize(column1, numBinsX);
        double rangeSizeY = computeRangeSize(column2, numBinsY);

        TilesManager tilesManager = new TilesManager(dataset, numBinsX, numBinsY, column1, column2);
        tiles = tilesManager.createTilesArray(rangeSizeX, rangeSizeY);
    }

    private void prepareDataset() {
        assignUniqueRowIds();
        convertColumnsToDouble();
    }

    public double calculateKendallTauCorrelation() {
        long startTime = System.nanoTime();
        double correlationScore = computeCorrelation();
        long endTime = System.nanoTime();

        printElapsedTime(startTime, endTime);

        return correlationScore;
    }

    private double computeCorrelation() {
        processor = new TileProcessor(tiles, dataset, statistics, column1, column2);
        traverseTilesAndProcess();
        return statistics.calculateCorrelationResult();
    }

    private void traverseTilesAndProcess() {
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++) {
                if (!tiles[row][col].isEmpty()) {
                    processor.processTile(tiles[row][col]);
                }
            }
        }
    }

    private int determineOptimalBins(String columnName) {
        return binCalculator.calculateBins(dataset, columnName);
    }

    private double computeRangeSize(String columnName, int numBins) {
        double minValue = dataset.selectExpr("min(" + columnName + ")").first().getDouble(0);
        double maxValue = dataset.selectExpr("max(" + columnName + ")").first().getDouble(0);
        return Math.ceil((maxValue - minValue) / numBins);
    }

    private void assignUniqueRowIds() {
        dataset = dataset.withColumn("id", functions.monotonically_increasing_id());
    }

    private void convertColumnsToDouble() {
        dataset = dataset.select(
                dataset.col("id").as("id"),
                dataset.col(column1).cast("double").as(column1),
                dataset.col(column2).cast("double").as(column2)
        );
    }

    private void printElapsedTime(long startTime, long endTime) {
        double elapsedTimeSeconds = (endTime - startTime) / 1e9;
        System.out.println("\n --------- Tiles elapsed time: " + elapsedTimeSeconds + " seconds --------");
    }
}
