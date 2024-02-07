package tileBasedKendallAlgorithms.tiles;

import tileBasedKendallAlgorithms.algo.IBinCalculator;
import org.apache.commons.math3.util.Pair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;

public class TilesManager implements Serializable {
    private final int tileCountX;
    private final int tileCountY;
    private final String column1;
    private final String column2;
    private static Tile[][] tiles;
    private final double minValueX;
    private final double minValueY;
    private final double rangeSizeX;
    private final double rangeSizeY;
    private final Dataset<Row> dataset;
    private final IBinCalculator calculator;

    public TilesManager(Dataset<Row> dataset, String column1, String column2, IBinCalculator calculator) {
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
        this.calculator = calculator;

        this.minValueX = calculateMinColumnValue(column1);
        this.minValueY = calculateMinColumnValue(column2);
        double maxValueX = calculateMaxColumnValue(column1);
        double maxValueY = calculateMaxColumnValue(column2);

        this.tileCountX = determineOptimalBins(column1);
        this.tileCountY = determineOptimalBins(column2);

        this.rangeSizeX = Math.ceil((maxValueX - minValueX) / tileCountX);
        this.rangeSizeY = Math.ceil((maxValueY - minValueY) / tileCountY);

        tiles = new Tile[this.tileCountX][this.tileCountY];
    }

    public Tile[][] createTilesArray() {
        initializeTilesArray();
        System.out.println("Starting Population");
        double startTime = System.currentTimeMillis();

        populateTiles();
        double endTime = System.currentTimeMillis();

        double elapsed = (endTime - startTime) / 1000.0;
        System.out.println("Tiles Population took " + elapsed + " seconds");

        return tiles;
    }

    private void initializeTilesArray() {
        for (int row = 0; row < this.tileCountX; row++) {
            for (int col = 0; col < this.tileCountY; col++) {
                tiles[row][col] = new Tile(row, col);
            }
        }
    }

    private void populateTiles() {
        dataset.foreach(row -> {
            double valueX = row.getDouble(row.fieldIndex(column1));
            double valueY = row.getDouble(row.fieldIndex(column2));

            int tileRow = (int) Math.min(tileCountX - 1, Math.floor((valueX - minValueX) / rangeSizeX));
            int tileCol = (int) Math.min(tileCountY - 1, Math.floor((valueY - minValueY) / rangeSizeY));

            if (tileRow >= 0 && tileRow < tileCountX && tileCol >= 0 && tileCol < tileCountY) {
                synchronized (tiles[tileRow][tileCol]) {
                    tiles[tileRow][tileCol].addValuePair(new Pair<>(valueX, valueY));
                }
            } else {
                throw new ArrayIndexOutOfBoundsException("Tried to access out of bounds array cell");
            }
        });
    }

    private int determineOptimalBins(String columnName) {
        if (calculator != null) {
            return calculator.calculateBins(dataset, columnName);
        } else {
            throw new IllegalStateException("Calculator is not initialized");
        }
    }

    private double calculateMinColumnValue(String columnName) {
        return dataset.selectExpr("min(" + columnName + ")").first().getDouble(0);
    }

    private double calculateMaxColumnValue(String columnName) {
        return dataset.selectExpr("max(" + columnName + ")").first().getDouble(0);
    }
}
