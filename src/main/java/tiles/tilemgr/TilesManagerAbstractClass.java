package tiles.tilemgr;

import common.ColumnsStatistics;
import tiles.dom.ITile;
import tiles.dom.ITileFactory;
import tiles.dom.ITileType;


public abstract class TilesManagerAbstractClass implements ITilesManager{

	protected static final boolean DEBUG_FLAG = false;
	protected static final boolean EXPERIMENT_FLAG = false;

	protected static ITile[][] tiles;
	protected long datasetRowCount;
	protected int numOfBinsX;
	protected int numOfBinsY;
	protected double rangeWidthX;
	protected double rangeWidthY;
	protected ColumnsStatistics columnsStatistics;
    protected ITileType tileType;
    protected ITileFactory tileFactory;

    protected abstract void initializeTilesArray();
	protected abstract void populateTiles();
	protected abstract void calculateMinMaxColumnValues();


	@Override
	public ITile[][] createTilesArray() {
		
	    	double start = System.currentTimeMillis();
	    calculateMinMaxColumnValues();
	    	double end = System.currentTimeMillis();
	    	double elapsed = (end - start) / 1000.0;
	    	if(DEBUG_FLAG) {
	    		System.out.println("X,Y min and max and stddev took: " + elapsed + " seconds");
	    		System.out.println("Dataset size: " + datasetRowCount);
	    	}
	    	
	    	start = System.currentTimeMillis();
	    setupTilesArrayMetadata();
    		end = System.currentTimeMillis();
    		elapsed = (end - start) / 1000.0;
    		if(DEBUG_FLAG) {
    			System.out.println("Tiles bin number and binWidth calculations took: " + elapsed + " seconds");
    			System.out.println("#SubRangesX: " + numOfBinsX + "\n#SubRangesY: " + numOfBinsY + "\nTotal tiles: " + numOfBinsX * numOfBinsY);
    			System.out.println("RangeWidthX: " + rangeWidthX + "\n#RangeWidthY: " + rangeWidthY + "\nTotal #tuples: " + datasetRowCount);
    		}
    		
    		start = System.currentTimeMillis();
	    initializeTilesArray();
    		end = System.currentTimeMillis();
    		elapsed = (end - start) / 1000.0;
    		if(DEBUG_FLAG) {
    			System.out.println("Tiles initialization took: " + elapsed + " seconds");
    		}
	    
	    	start = System.currentTimeMillis(); // Timing population
	    populateTiles();
	    	end = System.currentTimeMillis();
	    	elapsed = (end - start) / 1000.0;
	    	if(DEBUG_FLAG) {
	    		System.out.println("Tiles Population took " + elapsed + " seconds\n");
	    	}
	    return tiles;
	}

	protected void setupTilesArrayMetadata() {
		double datasetRowCountAsDouble = (double) this.datasetRowCount;
	    rangeWidthX = calculateRangesWidth(columnsStatistics.getStdDevX(), datasetRowCountAsDouble);
	    rangeWidthY = calculateRangesWidth(columnsStatistics.getStdDevY(), datasetRowCountAsDouble);       
		numOfBinsX = calculateRangesCount(rangeWidthX, columnsStatistics.getMinX(), columnsStatistics.getMaxX());
	    numOfBinsY = calculateRangesCount(rangeWidthY, columnsStatistics.getMinY(), columnsStatistics.getMaxY());
	}

//	protected void initializeTilesArray() {
//	    tiles = new ITile[this.numOfBinsY][this.numOfBinsX];    	
//	    for (int row = 0; row < numOfBinsY; row++) {
//	        for (int col = 0; col < numOfBinsX; col++) {
//	            tiles[row][col] = new TileInMemSimple(row, col);
//	        }
//	    }
//	}

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