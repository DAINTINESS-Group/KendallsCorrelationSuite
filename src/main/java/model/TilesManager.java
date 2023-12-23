package model;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;

public class TilesManager implements Serializable {

    private final int numBinsX;
    private final int numBinsY;
    private final String column1;
    private final String column2;
    private double minValueX;
    private double minValueY;
    private double maxValueX;
    private double maxValueY;

    private static Tile[][] tiles;

    public TilesManager(int numBinsX, int numBinsY, String column1, String column2) {
        this.numBinsX = numBinsX;
        this.numBinsY = numBinsY;
        this.column1 = column1;
        this.column2 = column2;
        tiles = new Tile[this.numBinsY][this.numBinsY];
    }

    public Tile[][] createTilesArray(Dataset<Row> dataset, double rangeSizeX,
                                     double rangeSizeY) {

        minValueX = calculateMinColumnValue(dataset, column1);
        maxValueX = calculateMaxColumnValue(dataset, column1);
        minValueY = calculateMinColumnValue(dataset, column2);
        maxValueY = calculateMaxColumnValue(dataset, column2);

        for (int i = 0; i < numBinsX; i++) {
            for (int j = 0; j < numBinsY; j++) {
                double startX = minValueX + i * rangeSizeX;
                double endX = (i == numBinsX - 1) ? maxValueX : startX + rangeSizeX;

                double startY = minValueY + j * rangeSizeY;
                double endY = (j == numBinsY - 1) ? maxValueY : startY + rangeSizeY;

                tiles[i][j] = new Tile(startX, endX, startY, endY);
            }
        }

        populateTiles(dataset, rangeSizeX, rangeSizeY);

        return tiles;
    }

    private void populateTiles(Dataset<Row> dataset, double rangeSizeX, double rangeSizeY) {

        dataset.foreach(row -> {
            double x = row.getDouble(row.fieldIndex(column1));
            double y = row.getDouble(row.fieldIndex(column2));
            long pairId = row.getLong(row.fieldIndex("id"));
            
            int tileX = (int) Math.min(Math.floor((x - minValueX) / rangeSizeX), numBinsX - 1);
            int tileY = (int) Math.min(Math.floor((y - minValueY) / rangeSizeY), numBinsY - 1);

            // Check if the data point is within bounds
            if (tileX >= 0 && tileX < numBinsX && tileY >= 0 && tileY < numBinsY) {
                tiles[tileX][tileY].addPairId(pairId);
            } else {
                System.out.println("Warning: Ignoring out-of-bounds tile indices for " + column1 + " =" + x + ", " + column2 + " =" + y);
            }
        });
    }

    private double calculateMaxColumnValue(Dataset<Row> dataset, String columnName) {
        return dataset.selectExpr("max(" + columnName + ")").first().getDouble(0);
    }

    private double calculateMinColumnValue(Dataset<Row> dataset, String columnName) {
        return dataset.selectExpr("min(" + columnName + ")").first().getDouble(0);
    }

    public void printTilesArray(Tile[][] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                System.out.print("Tile " + i + "-" + j + ": ");
                System.out.print(tiles[i][j]);
                System.out.println();
            }
        }
    }
}
