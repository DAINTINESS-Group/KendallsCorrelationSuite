package tileBasedKendallAlgorithms.tiles;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;

import static org.apache.spark.sql.functions.*;


public class TilesManager implements Serializable {
    private static final long serialVersionUID = 8765154256335271048L;
	private static Tile[][] tiles;
    private int rangeCountX;
    private int rangeCountY;
    private final String column1;
    private final String column2;
    private double rangeWidthX;
    private double rangeWidthY;
    private final Dataset<Row> dataset;
    private ColumnsStatistics columnsStatistics;

    public TilesManager(Dataset<Row> dataset, String column1, String column2) {
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
    }

    public Tile[][] createTilesArray() {
        setupTilesArrayMetadata();
        initializeTilesArray();
        double startTime = System.currentTimeMillis(); // Timing population
        populateTiles();
        double endTime = System.currentTimeMillis();
        double elapsed = (endTime - startTime) / 1000.0;
        System.out.println("Tiles Population took " + elapsed + " seconds\n");
        return tiles;
    }

    private void setupTilesArrayMetadata() {

        double start = System.currentTimeMillis();

        calculateMinMaxColumnValues();

        double end = System.currentTimeMillis();
        double elapsed = (end - start) / 1000.0;
        System.out.println("X,Y min and max and stddev took: " + elapsed + " seconds");

        double datasetRowCount = dataset.count();
        System.out.println("Dataset size: " + datasetRowCount);

        start = System.currentTimeMillis();

        rangeWidthX = calculateRangesWidth(columnsStatistics.getStdDevX(), datasetRowCount);
        rangeWidthY = calculateRangesWidth(columnsStatistics.getStdDevY(), datasetRowCount);
        rangeCountX = calculateRangesCount(rangeWidthX, columnsStatistics.getMinX(), columnsStatistics.getMaxX());
        rangeCountY = calculateRangesCount(rangeWidthY, columnsStatistics.getMinY(), columnsStatistics.getMaxY());

        end = System.currentTimeMillis();
        elapsed = (end - start) / 1000.0;
        System.out.println("Tiles bin number and binWidth calculations took: " + elapsed + " seconds");
        System.out.println("#RangesX: " + rangeCountX + "\n#RangesY: " + rangeCountY + "\nTotal tiles: " + rangeCountX * rangeCountY);

        tiles = new Tile[this.rangeCountY][this.rangeCountX];
    }

    private void initializeTilesArray() {
        double start = System.currentTimeMillis();
        for (int row = 0; row < rangeCountY; row++) {
            for (int col = 0; col < rangeCountX; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }
        double end = System.currentTimeMillis();
        double elapsed = (end - start) / 1000.0;
        System.out.println("Tiles initialization took: " + elapsed + " seconds");
    }

    private void populateTiles() {
        double minX = columnsStatistics.getMinX();
        double maxY = columnsStatistics.getMaxY();

        dataset.foreach(row -> {
            double valueX = row.getDouble(row.fieldIndex(column1));
            double valueY = row.getDouble(row.fieldIndex(column2));

            int tileRow = (int) Math.min(rangeCountY - 1, Math.floor((maxY - valueY) / rangeWidthY));
            int tileColumn = (int) Math.min(rangeCountX - 1, Math.floor((valueX - minX) / rangeWidthX));

            if (tileRow >= 0 && tileRow < rangeCountY && tileColumn >= 0 && tileColumn < rangeCountX) {
                synchronized (tiles[tileRow][tileColumn]) {
                    tiles[tileRow][tileColumn].addValuePair(new DoublePair(valueX, valueY));
                }
            } else {
                throw new ArrayIndexOutOfBoundsException("Tried to access out of bounds array cell");
            }
        });
    }


    private void calculateMinMaxColumnValues() {
        Row result = dataset.agg(
                min(column1).alias("minValueX"),
                max(column1).alias("maxValueX"),
                min(column2).alias("minValueY"),
                max(column2).alias("maxValueY"),
                stddev(column1).alias("std devX"),
                stddev(column2).alias("std devY")
        ).first();

        // Extract the min, max and standard deviation values for both columns
        double minValueX = result.getDouble(0);
        double maxValueX = result.getDouble(1);
        double minValueY = result.getDouble(2);
        double maxValueY = result.getDouble(3);
        double stdDevX = result.getDouble(4);
        double stdDevY = result.getDouble(5);

        columnsStatistics = new ColumnsStatistics(minValueX, maxValueX, minValueY, maxValueY, stdDevX, stdDevY);
    }

    public int calculateRangesCount(double rangeWidth, double min, double max) {
        double range = max - min;
        return (int) Math.ceil(range / rangeWidth);
    }

    private double calculateRangesWidth(double stdDev, double datasetCount) {
        return 3.49 * (stdDev / Math.pow(datasetCount, 1.0 / 3.0));
    }
}
