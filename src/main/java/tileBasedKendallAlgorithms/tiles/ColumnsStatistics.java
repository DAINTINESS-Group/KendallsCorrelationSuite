package tileBasedKendallAlgorithms.tiles;

import java.io.Serializable;

public class ColumnsStatistics implements Serializable {

    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;
    private final double stdDevX;
    private final double stdDevY;

    public ColumnsStatistics(double minX, double maxX, double minY, double maxY, double stdDevX, double stdDevY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        this.stdDevX = stdDevX;
        this.stdDevY = stdDevY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getStdDevX() {
        return stdDevX;
    }

    public double getStdDevY() {
        return stdDevY;
    }

    @Override
    public String toString() {
        return "ColumnsMeasures{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", stdDevX=" + stdDevX +
                ", stdDevY=" + stdDevY +
                '}';
    }
}
