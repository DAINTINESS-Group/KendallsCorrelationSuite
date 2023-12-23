package kendall;

import model.Tile;
import model.TilesManager;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

public class TileMethodCalculator {

    private final IBinCalculator binCalculator;

    public TileMethodCalculator(IBinCalculator binCalculator) {
        this.binCalculator = binCalculator;
    }

    public double calculateKendall(Dataset<Row> dataset, String column1, String column2) {
        dataset = dataset.withColumn("id", functions.monotonically_increasing_id());

        // Convert columns X and Y to doubles
        dataset = dataset.select(
                dataset.col("id").as("id"),
                dataset.col(column1).cast("double").as(column1),
                dataset.col(column2).cast("double").as(column2)
        );

        dataset.show(Integer.MAX_VALUE);

        // Calculate the best number of bins for columns X and Y
        int numBinsX = binCalculator.calculateNumberOfBins(dataset, column1);
        int numBinsY = binCalculator.calculateNumberOfBins(dataset, column2);

        TilesManager tilesManager = new TilesManager(numBinsX, numBinsY, column1, column2);

        // Calculate the range size for columns X and Y based on the number of bins
        double rangeSizeX = calculateRangeSize(dataset, column1, numBinsX);
        double rangeSizeY = calculateRangeSize(dataset, column2, numBinsY);

        System.out.println("RangeX: " + rangeSizeX);
        System.out.println("RangeY: " + rangeSizeY);
        System.out.println("numBinsX: " + numBinsX);
        System.out.println("numBinsY: " + numBinsY);

        // Create a two-dimensional array of Tiles
        Tile[][] tiles = tilesManager.createTilesArray(dataset, rangeSizeX, rangeSizeY);

        // Print the tiles array
        tilesManager.printTilesArray(tiles);

        // TODO Implement Tile cross calculation
        return calculateResult(tiles);
    }

    // TODO Implement Tile cross calculation
    private double calculateResult(Tile[][] tiles) {
        return 0.0;
    }

    private static double calculateRangeSize(Dataset<Row> dataset, String columnName, int numBins) {
        double minValue = dataset.selectExpr("min(" + columnName + ")").first().getDouble(0);
        double maxValue = dataset.selectExpr("max(" + columnName + ")").first().getDouble(0);

        // Ensure the range size is large enough to accommodate the maximum value
        return Math.ceil((maxValue - minValue) / numBins);

    }
}
