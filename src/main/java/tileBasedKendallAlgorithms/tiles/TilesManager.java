package tileBasedKendallAlgorithms.tiles;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;

public class TilesManager implements Serializable {

    private static final long serialVersionUID = 1L;
	private final int tileCountX;
    private final int tileCountY;
    private final String column1;
    private final String column2;
    private static Tile[][] tiles;
    private double minValueX;
    private double minValueY;
    private final Dataset<Row> dataset;

    public TilesManager(Dataset<Row> dataset, int tileCountX, int tileCountY, String column1, String column2) {
        this.tileCountX = tileCountX;
        this.tileCountY = tileCountY;
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
    }

    public Tile[][] createTilesArray(double rangeSizeX, double rangeSizeY) {
        initializeTilesArray(rangeSizeX, rangeSizeY);
        populateTiles(rangeSizeX, rangeSizeY);
        return tiles;
    }

    private void populateTiles(double rangeSizeX, double rangeSizeY) {

        this.minValueX = calculateMinColumnValue(column1);
        this.minValueY = calculateMinColumnValue(column2);

        dataset.foreach(row -> {
            double valueX = row.getDouble(row.fieldIndex(column1));
            double valueY = row.getDouble(row.fieldIndex(column2));
            long pairId = row.getLong(row.fieldIndex("id"));

            int tileRow = (int) Math.min(Math.floor((valueX - minValueX) / rangeSizeX), tileCountX - 1);
            int tileCol = (int) Math.min(Math.floor((valueY - minValueY) / rangeSizeY), tileCountY - 1);

            // Check if the data point is within bounds
            if (tileRow >= 0 && tileRow < tileCountX && tileCol >= 0 && tileCol < tileCountY) {
                tiles[tileRow][tileCol].addPairId(pairId);
                tiles[tileRow][tileCol].setRow(tileRow);
                tiles[tileRow][tileCol].setCol(tileCol);
            } else {
                throw new IndexOutOfBoundsException("Out-of-bounds tile indices for " + column1 + " =" + valueX + ", " + column2 + " =" + valueY);
            }
        });
    }

    private void initializeTilesArray(double rangeSizeX, double rangeSizeY) {
        tiles = new Tile[this.tileCountX][this.tileCountY];

        double minValueX = calculateMinColumnValue(column1);
        double minValueY = calculateMinColumnValue(column2);
        double maxValueX = calculateMaxColumnValue(column1);
        double maxValueY = calculateMaxColumnValue(column2);

        for (int i = 0; i < this.tileCountX; i++) {
            for (int j = 0; j < this.tileCountY; j++) {
                double startX = minValueX + i * rangeSizeX;
                double endX = (i == tileCountX - 1) ? maxValueX : startX + rangeSizeX;

                double startY = minValueY + j * rangeSizeY;
                double endY = (j == tileCountY - 1) ? maxValueY : startY + rangeSizeY;
                tiles[i][j] = new Tile(
                        new Range(startX, endX),
                        new Range(startY, endY)
                );
            }
        }
    }

    private double calculateMinColumnValue(String columnName) {
        return dataset.selectExpr("min(" + columnName + ")").first().getDouble(0);
    }

    private double calculateMaxColumnValue(String columnName) {
        return dataset.selectExpr("max(" + columnName + ")").first().getDouble(0);
    }
}
