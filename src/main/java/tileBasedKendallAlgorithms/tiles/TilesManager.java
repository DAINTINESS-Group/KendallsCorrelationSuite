package tileBasedKendallAlgorithms.tiles;

import static org.apache.spark.sql.functions.*;
import org.apache.commons.math3.util.Pair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;

public class TilesManager implements Serializable {
    private final int rangeCountX;
    private final int rangeCountY;
    private final String column1;
    private final String column2;
    private static Tile[][] tiles;
    private double[] minMaxOfX;
    private double[] minMaxOfY;
    private final double rangeWidthX;
    private final double rangeWidthY;
    private final Dataset<Row> dataset;
    private final double avgPairsPerTile = 10.0;

    public TilesManager(Dataset<Row> dataset, String column1, String column2) {
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
        System.out.println("Count: " + dataset.count());
        System.out.println("avg tiles = " + avgPairsPerTile);

        double start = System.currentTimeMillis();

        calculateMinMaxColumnValues();

        double end = System.currentTimeMillis();
        double elapsed = (end - start) / 1000.0;
        System.out.println("X,Y min and max took: " + elapsed + " seconds");

        start = System.currentTimeMillis();
        double datasetRowCount = dataset.count();

        double totalTiles = (datasetRowCount / avgPairsPerTile);
        int tilesPerRow = (int) Math.sqrt(totalTiles);

        this.rangeCountX = tilesPerRow; // calculateBins(column1, tileWidthX, minMaxOfX);
        this.rangeCountY = tilesPerRow; // calculateBins(column2, tileWidthY, minMaxOfY);

        rangeWidthX = (minMaxOfX[1] - minMaxOfX[0]) / rangeCountX; // calculateBinWidth(column1, datasetRowCount);
        rangeWidthY = (minMaxOfY[1] - minMaxOfY[0]) / rangeCountY; // calculateBinWidth(column2, datasetRowCount);

        end = System.currentTimeMillis();
        elapsed = (end - start) / 1000.0;
        System.out.println("Tiles bin number and binWidth calculations took: " + elapsed + " seconds");
        System.out.println("#RangesX: " + rangeCountX + "\n#RangesY: " + rangeCountY + "\nTotal tiles: " + rangeCountX * rangeCountY);

        tiles = new Tile[this.rangeCountX][this.rangeCountY];
    }

    public Tile[][] createTilesArray() {
        initializeTilesArray();
        double startTime = System.currentTimeMillis(); // Timing population
        populateTiles();
        double endTime = System.currentTimeMillis();
        double elapsed = (endTime - startTime) / 1000.0;
        System.out.println("Tiles Population took " + elapsed + " seconds\n");

        return tiles;
    }

    private void initializeTilesArray() {
        double start = System.currentTimeMillis();
        for (int row = 0; row < this.rangeCountX; row++) {
            for (int col = 0; col < this.rangeCountY; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }
        double end = System.currentTimeMillis();
        double elapsed = (end - start) / 1000.0;
        System.out.println("Tiles initialization took: " + elapsed + " seconds");
    }

    private void populateTiles() {
        dataset.foreach(row -> {
            double valueX = row.getDouble(row.fieldIndex(column1));
            double valueY = row.getDouble(row.fieldIndex(column2));

            int tileRow = (int) Math.min(rangeCountX - 1, Math.floor((valueX - minMaxOfX[0]) / rangeWidthX));
            int tileCol = (int) Math.min(rangeCountY - 1, Math.floor((valueY - minMaxOfY[0]) / rangeWidthY));

            if (tileRow >= 0 && tileRow < rangeCountX && tileCol >= 0 && tileCol < rangeCountY) {
                synchronized (tiles[tileRow][tileCol]) {
                    tiles[tileRow][tileCol].addValuePair(new Pair<>(valueX, valueY));
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
                max(column2).alias("maxValueY")
        ).first();

        // Extract the min and max values for both columns
        double minValueX = result.getDouble(0);
        double maxValueX = result.getDouble(1);
        double minValueY = result.getDouble(2);
        double maxValueY = result.getDouble(3);

        minMaxOfX = new double[]{minValueX, maxValueX};
        minMaxOfY = new double[]{minValueY, maxValueY};
    }

    public int calculateBins(String columnName, double binWidth, double[] minMax) {
        double range = minMax[1] - minMax[0];
        return (int) Math.ceil(range / binWidth);
    }

    private double calculateBinWidth(String columnName, double datasetCount) {
        double standardDeviation = dataset.agg(stddev(columnName)).first().getDouble(0);
        return 3.5 * (standardDeviation / Math.pow(datasetCount, 1.0 / 3.0));
    }
}
