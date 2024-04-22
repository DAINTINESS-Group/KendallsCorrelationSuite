package util.tilemgr;

import util.common.ColumnsStatistics;
import util.tiles.Tile;


public abstract class TilesManagerAbstractClass implements ITilesManager{

	protected static final boolean DEBUG_FLAG = false;
	protected static Tile[][] tiles;
	protected long datasetRowCount;
	protected int rangeCountX;
	protected int rangeCountY;
	protected double rangeWidthX;
	protected double rangeWidthY;
	protected ColumnsStatistics columnsStatistics;
	

	protected abstract void populateTiles();
	protected abstract void calculateMinMaxColumnValues();


	@Override
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

	protected void setupTilesArrayMetadata() {
	
	    double start = System.currentTimeMillis();
	
	    calculateMinMaxColumnValues();
	
	    double end = System.currentTimeMillis();
	    double elapsed = (end - start) / 1000.0;
	    System.out.println("X,Y min and max and stddev took: " + elapsed + " seconds");
	    System.out.println("Dataset size: " + datasetRowCount);
	    
	    start = System.currentTimeMillis();
	    double datasetRowCountAsDouble = (double) this.datasetRowCount;
	    rangeWidthX = calculateRangesWidth(columnsStatistics.getStdDevX(), datasetRowCountAsDouble);
	    rangeWidthY = calculateRangesWidth(columnsStatistics.getStdDevY(), datasetRowCountAsDouble);       
		rangeCountX = calculateRangesCount(rangeWidthX, columnsStatistics.getMinX(), columnsStatistics.getMaxX());
	    rangeCountY = calculateRangesCount(rangeWidthY, columnsStatistics.getMinY(), columnsStatistics.getMaxY());
	
	    end = System.currentTimeMillis();
	    elapsed = (end - start) / 1000.0;
	    System.out.println("Tiles bin number and binWidth calculations took: " + elapsed + " seconds");
	    System.out.println("#RangesX: " + rangeCountX + "\n#RangesY: " + rangeCountY + "\nTotal tiles: " + rangeCountX * rangeCountY);
	    System.out.println("RangeWidthX: " + rangeWidthX + "\n#RangeWidthY: " + rangeWidthY + "\nTotal #tuples: " + datasetRowCount);
	
	    tiles = new Tile[this.rangeCountY][this.rangeCountX];
	}

	protected void initializeTilesArray() {
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

	protected int calculateRangesCount(double rangeWidth, double min, double max) {
	    double range = max - min;
	    return (int) Math.ceil(range / rangeWidth);
	}

	protected double calculateRangesWidth(double stdDev, double datasetCount) {
		double denominator = Math.pow(datasetCount, 1.0 / 3.0);
		double scottRuleRangeWidth = 3.49 * stdDev / denominator;		
	    return scottRuleRangeWidth;
	}

}