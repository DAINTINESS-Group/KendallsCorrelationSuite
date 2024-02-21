package tileBasedKendallAlgorithms.tiles;

import static org.apache.spark.sql.functions.*;
import static tileBasedKendallAlgorithms.tiles.ColumnsMeasures.*;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;


public class TilesManager implements Serializable {
    private final int rangeCountX;
    private final int rangeCountY;
    private final String column1;
    private final String column2;
    private static Tile[][] tiles;
    private double[] columnsStats;
    private final double rangeWidthX;
    private final double rangeWidthY;
    private final Dataset<Row> dataset;
    private final double avgPairsPerTile = 100;

    public TilesManager(Dataset<Row> dataset, String column1, String column2) {
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
        System.out.println("avg tiles = " + avgPairsPerTile);

        double start = System.currentTimeMillis();

        calculateMinMaxColumnValues();

        double end = System.currentTimeMillis();
        double elapsed = (end - start) / 1000.0;
        System.out.println("X,Y min and max and stddev took: " + elapsed + " seconds");

        double datasetRowCount = dataset.count();
        System.out.println("Dataset size: " + datasetRowCount);

        start = System.currentTimeMillis();

        rangeWidthX = calculateRangesWidth(columnsStats[STD_DEV_X], datasetRowCount);
        rangeWidthY = calculateRangesWidth(columnsStats[STD_DEV_Y], datasetRowCount);
        rangeCountX = calculateRangesCount(rangeWidthX, columnsStats[MIN_X], columnsStats[MAX_X]);
        rangeCountY = calculateRangesCount(rangeWidthY, columnsStats[MIN_Y], columnsStats[MAX_Y]);

//        double totalTiles = (datasetRowCount / avgPairsPerTile);
//        int tilesPerRow = (int) Math.sqrt(totalTiles);
//        rangeCountX = tilesPerRow;
//        rangeCountY = tilesPerRow;
//        rangeWidthX = (columnsStats[MAX_X] - columnsStats[MIN_X]) / rangeCountX;
//        rangeWidthY = (columnsStats[MAX_Y] - columnsStats[MIN_Y]) / rangeCountY;

        end = System.currentTimeMillis();
        elapsed = (end - start) / 1000.0;
        System.out.println("Tiles bin number and binWidth calculations took: " + elapsed + " seconds");
        System.out.println("#RangesX: " + rangeCountX + "\n#RangesY: " + rangeCountY + "\nTotal tiles: " + rangeCountX * rangeCountY);

        tiles = new Tile[this.rangeCountY][this.rangeCountX];
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
        for (int row = 0; row < this.rangeCountY; row++) {
            for (int col = 0; col < this.rangeCountX; col++) {
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

            int tileRow = (int) Math.min(rangeCountY - 1, Math.floor((valueY - columnsStats[MIN_Y]) / rangeWidthY));
            int tileCol = (int) Math.min(rangeCountX - 1, Math.floor((valueX - columnsStats[MIN_X]) / rangeWidthX));

            if (tileRow >= 0 && tileRow < rangeCountY && tileCol >= 0 && tileCol < rangeCountX) {
                synchronized (tiles[tileRow][tileCol]) {
                    tiles[tileRow][tileCol].addValuePair(new DoublePair(valueX, valueY));
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
                stddev(column1).alias("stddevX"),
                stddev(column2).alias("stddevY")
        ).first();

        // Extract the min and max values for both columns
        double minValueX = result.getDouble(MIN_X);
        double maxValueX = result.getDouble(MAX_X);
        double minValueY = result.getDouble(MIN_Y);
        double maxValueY = result.getDouble(MAX_Y);
        double stdDevX = result.getDouble(STD_DEV_X);
        double stdDevY = result.getDouble(STD_DEV_Y);

        columnsStats = new double[]{minValueX, maxValueX, minValueY, maxValueY, stdDevX, stdDevY};
    }

    public int calculateRangesCount(double binWidth, double min, double max) {
        double range = max - min;
        return (int) Math.ceil(range / binWidth);
    }

    private double calculateRangesWidth(double stdDev, double datasetCount) {
        return 3.5 * (stdDev / Math.pow(datasetCount, 1.0 / 3.0));
    }
}
