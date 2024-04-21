package tileUtil.tiles;

import java.io.Serializable;

public class ColumnsStatistics implements Serializable {

    private static final long serialVersionUID = -1503614268476346388L;
	private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;
    private final double stdDevX;
    private final double stdDevY;
    private final long rowCount;

    public ColumnsStatistics(long rowCount, double minX, double maxX, double minY, double maxY, double stdDevX, double stdDevY) {
        this.rowCount = rowCount;
    	this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        this.stdDevX = stdDevX;
        this.stdDevY = stdDevY;
    }

    public long getRowCount() {
		return rowCount;
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
        return "MetadataStats{" +
        		"#rows=" + rowCount + 
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", stdDevX=" + stdDevX +
                ", stdDevY=" + stdDevY +
                '}';
    }
}
