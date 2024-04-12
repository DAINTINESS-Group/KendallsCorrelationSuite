package listBasedKendallAlgorithms.listBasedTiles;

import java.io.Serializable;
import java.util.List;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import tileUtil.tiles.ColumnsStatistics;
import tileUtil.tiles.DoublePair;
import tileUtil.tiles.Tile;



public class TilesManager implements Serializable {
    private static final long serialVersionUID = 8765154256335271048L;
	private static Tile[][] tiles;
    private int rangeCountX;
    private int rangeCountY;
    private double rangeWidthX;
    private double rangeWidthY;
    private final ColumnPair pair;
    private ColumnsStatistics columnsStatistics;
    double datasetRowCount;

    public TilesManager(ColumnPair pair) {
        this.pair = pair;
        this.datasetRowCount = pair.getSize();
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
    	List<Double> xList = pair.getXColumn();
    	List<Double> yList = pair.getYColumn();
        double minX = columnsStatistics.getMinX();
        double maxY = columnsStatistics.getMaxY();

        for (int i=0; i<this.datasetRowCount;i++ ) {
        	double valueX = xList.get(i);
        	double valueY = yList.get(i);

            int tileRow = (int) Math.min(rangeCountY - 1, Math.floor((maxY - valueY) / rangeWidthY));
            int tileColumn = (int) Math.min(rangeCountX - 1, Math.floor((valueX - minX) / rangeWidthX));

            if (tileRow >= 0 && tileRow < rangeCountY && tileColumn >= 0 && tileColumn < rangeCountX) {
                synchronized (tiles[tileRow][tileColumn]) {
                    tiles[tileRow][tileColumn].addValuePair(new DoublePair(valueX, valueY));
                }
            } else {
                throw new ArrayIndexOutOfBoundsException("Tried to access out of bounds array cell");
            }
        }
    }


    private void calculateMinMaxColumnValues() {
        double minValueX = Double.MAX_VALUE;
        double minValueY = Double.MAX_VALUE;
        double maxValueX = Double.MIN_VALUE;
        double maxValueY = Double.MIN_VALUE;
        double sumValueX = 0.0;
        double sumValueY = 0.0;
        double stdDevX = 0.0;
        double stdDevY = 0.0;
    	
    	List<Double> xList = pair.getXColumn();
    	List<Double> yList = pair.getYColumn();

    	for(Double d: xList) {
    		if (d < minValueX)
    			minValueX = d;
    		if (d > maxValueX)
    			maxValueX = d;
    		sumValueX += d;
    	}

    	for(Double d: yList) {
    		if (d < minValueY)
    			minValueY = d;
    		if (d > maxValueY)
    			maxValueY = d;
    		sumValueY += d;
    	}

    	stdDevX = computeStdDev(sumValueX, xList);
    	stdDevY = computeStdDev(sumValueY, yList);
      
        
        columnsStatistics = new ColumnsStatistics(minValueX, maxValueX, minValueY, maxValueY, stdDevX, stdDevY);
    }

	private double computeStdDev(double sumValue, List<Double> aList) {
		double meanValueX = Double.NaN;
		double stdDevX = 0;;
		
		meanValueX = sumValue / (double) this.datasetRowCount;
    	Double var = 0.0;
        for (Double d: aList) {
            var += Math.pow(d - meanValueX, 2);
        }
        var = var / (double) this.datasetRowCount;
        stdDevX =  Math.sqrt(var);
		return stdDevX;
	}

    public int calculateRangesCount(double rangeWidth, double min, double max) {
        double range = max - min;
        return (int) Math.ceil(range / rangeWidth);
    }

    private double calculateRangesWidth(double stdDev, double datasetCount) {
        return 3.49 * (stdDev / Math.pow(datasetCount, 1.0 / 3.0));
    }
}
