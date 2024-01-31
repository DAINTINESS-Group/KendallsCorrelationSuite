package tileBasedKendallAlgorithms.tiles;

import tileBasedKendallAlgorithms.algo.IBinCalculator;
import org.apache.commons.math3.util.Pair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class TilesManager {
    private final int binCountX;
    private final int binCountY;
    private final String column1;
    private final String column2;
    private final Tile[][] tiles;
    private final double minValueX;
    private final double minValueY;
    private final double maxValueX;
    private final double maxValueY;
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
        this.maxValueX = calculateMaxColumnValue(column1);
        this.maxValueY = calculateMaxColumnValue(column2);

        this.binCountX = determineOptimalBins(column1);
        this.binCountY = determineOptimalBins(column2);

        this.rangeSizeX = Math.ceil((maxValueX - minValueX) / binCountX);
        this.rangeSizeY = Math.ceil((maxValueY - minValueY) / binCountY);

        this.tiles = new Tile[this.binCountX][this.binCountY];
    }

    public Tile[][] createTilesArray() {
        initializeTilesArray();
        populateTiles();

        return tiles;
    }

    private void initializeTilesArray() {
        for (int i = 0; i < this.binCountX; i++) {
            for (int j = 0; j < this.binCountY; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    private void populateTiles() {
        dataset.toLocalIterator().forEachRemaining(row -> {
            double valueX = row.getDouble(row.fieldIndex(column1));
            double valueY = row.getDouble(row.fieldIndex(column2));

            int tileRow = (int) Math.min(binCountX - 1, Math.floor((valueX - minValueX) / rangeSizeX));
            int tileCol = (int) Math.min(binCountY - 1, Math.floor((valueY - minValueY) / rangeSizeY));

            if (tileRow >= 0 && tileRow < binCountX && tileCol >= 0 && tileCol < binCountY) {
                tiles[tileRow][tileCol].addValuePair(new Pair<>(valueX, valueY));
                tiles[tileRow][tileCol].setRow(tileRow);
                tiles[tileRow][tileCol].setCol(tileCol);
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
