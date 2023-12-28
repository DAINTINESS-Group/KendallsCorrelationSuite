package model;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class TilesManager implements Serializable {

    private final int numBinsX;
    private final int numBinsY;
    private final String column1;
    private final String column2;
    private static Tile[][] tiles;

    public TilesManager(int numBinsX, int numBinsY, String column1, String column2) {
        this.numBinsX = numBinsX;
        this.numBinsY = numBinsY;
        this.column1 = column1;
        this.column2 = column2;
    }

    public Tile[][] createTilesArray(Dataset<Row> dataset, double rangeSizeX, double rangeSizeY) {
        initializeTilesArray(dataset, rangeSizeX, rangeSizeY);
        populateTiles(dataset, rangeSizeX, rangeSizeY);
        return tiles;
    }

    private void populateTiles(Dataset<Row> dataset, double rangeSizeX, double rangeSizeY) {

        double minValueX = calculateMinColumnValue(dataset, column1);
        double minValueY = calculateMinColumnValue(dataset, column2);

        dataset.foreach(row -> {
            double x = row.getDouble(row.fieldIndex(column1));
            double y = row.getDouble(row.fieldIndex(column2));
            long pairId = row.getLong(row.fieldIndex("id"));

            int tileI = (int) Math.min(Math.floor((x - minValueX) / rangeSizeX), numBinsX - 1);
            int tileJ = (int) Math.min(Math.floor((y - minValueY) / rangeSizeY), numBinsY - 1);

            // Check if the data point is within bounds
            if (tileI >= 0 && tileI < numBinsX && tileJ >= 0 && tileJ < numBinsY) {
                tiles[tileI][tileJ].addPairId(pairId);
            } else {
                throw new IndexOutOfBoundsException("Out-of-bounds tile indices for " + column1 + " =" + x + ", " + column2 + " =" + y);
            }
        });
    }

    public void printTilesArray(Tile[][] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if(!tiles[i][j].isEmpty()) {
                    System.out.print("Tile " + i + "-" + j + ": ");
                    System.out.print(tiles[i][j]);
                    System.out.println();
                }
            }
        }
    }

    private void initializeTilesArray(Dataset<Row> dataset, double rangeSizeX, double rangeSizeY) {
        tiles = new Tile[this.numBinsX][this.numBinsY];

        double minValueX = calculateMinColumnValue(dataset, column1);
        double minValueY = calculateMinColumnValue(dataset, column2);
        double maxValueX = calculateMaxColumnValue(dataset, column1);
        double maxValueY = calculateMaxColumnValue(dataset, column2);

        for (int i = 0; i < this.numBinsX; i++) {
            for (int j = 0; j < this.numBinsY; j++) {
                double startX = minValueX + i * rangeSizeX;
                double endX = (i == numBinsX - 1) ? maxValueX : startX + rangeSizeX;

                double startY = minValueY + j * rangeSizeY;
                double endY = (j == numBinsY - 1) ? maxValueY : startY + rangeSizeY;
                tiles[i][j] = new Tile(
                        new Range(startX, endX),
                        new Range(startY, endY)
                );
            }
        }
    }

    private double calculateMinColumnValue(Dataset<Row> dataset, String columnName) {
        return dataset.selectExpr("min(" + columnName + ")").first().getDouble(0);
    }

    private double calculateMaxColumnValue(Dataset<Row> dataset, String columnName) {
        return dataset.selectExpr("max(" + columnName + ")").first().getDouble(0);
    }
}
