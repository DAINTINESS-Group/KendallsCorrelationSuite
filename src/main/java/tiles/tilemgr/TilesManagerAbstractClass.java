package tiles.tilemgr;

import common.ColumnsStatistics;
import tiles.dom.ITile;
import tiles.dom.ITileFactory;
import tiles.dom.ITileType;
import tiles.tilemgr.rangemaker.RangeMakerFactory;
import tiles.tilemgr.rangemaker.RangeMakerInterface;
import tiles.tilemgr.rangemaker.RangeMakerResult;
import util.TileConstructionParameters;
import util.TileConstructionParameters.RangeMakingMode;


public abstract class TilesManagerAbstractClass implements ITilesManager{

	protected boolean DEBUG_FLAG = false;
	protected boolean EXPERIMENT_FLAG = false;

	protected RangeMakingMode rangeMakerMethod;
	protected TileConstructionParameters parameters; 
	protected RangeMakerInterface rangeMaker = null;
	
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

	
	
	public TilesManagerAbstractClass() {
		this.DEBUG_FLAG = false;
		this.EXPERIMENT_FLAG = false;
		this.parameters = new TileConstructionParameters.Builder(false)
				.rangeMakingMode(RangeMakingMode.SCOTT_RULE)
				.build();
	}
	
	public TilesManagerAbstractClass(TileConstructionParameters parameters) {
        this.DEBUG_FLAG = parameters.isDebugModeOn();
        this.EXPERIMENT_FLAG = parameters.isExperimentModeOn();
        this.parameters = parameters;
	}
	
	
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
		RangeMakerFactory factory = new RangeMakerFactory();
		
		switch(parameters.getRangeMakingMode()) {
			case SCOTT_RULE: 
				this.rangeMakerMethod =parameters.getRangeMakingMode(); 
				rangeMaker = factory.makeRangeMakerScottRule(columnsStatistics);
				break;
			case FIXED:
				this.rangeMakerMethod =parameters.getRangeMakingMode();
				final int BINS_X = parameters.getNumBinsX();
				final int BINS_Y = parameters.getNumBinsY();
				rangeMaker = factory.makeRangeMakerFixedNumBins(columnsStatistics, BINS_X, BINS_Y);
				break;
			default:
				System.err.println("TilesManagerSparkReaderTilesStoredSimple: error in constructor parameters.\n"  + parameters.toString() +"\nAborting.");
				System.exit(-1);
		}
		

		RangeMakerResult result = rangeMaker.divideColumnsInBinsAndRanges();
		rangeWidthX = result.getRangeWidthX();
		rangeWidthY = result.getRangeWidthY();
		numOfBinsX = result.getNumberOfBinsX();
		numOfBinsY = result.getNumberOfBinsY();
		
//		double datasetRowCountAsDouble = (double) this.datasetRowCount;
//	    rangeWidthX = calculateRangesWidth(columnsStatistics.getStdDevX(), datasetRowCountAsDouble);
//	    rangeWidthY = calculateRangesWidth(columnsStatistics.getStdDevY(), datasetRowCountAsDouble);       
//		numOfBinsX = calculateRangesCount(rangeWidthX, columnsStatistics.getMinX(), columnsStatistics.getMaxX());
//	    numOfBinsY = calculateRangesCount(rangeWidthY, columnsStatistics.getMinY(), columnsStatistics.getMaxY());
		
	}//end setup tiles metadata ()

//	protected void initializeTilesArray() {
//	    tiles = new ITile[this.numOfBinsY][this.numOfBinsX];    	
//	    for (int row = 0; row < numOfBinsY; row++) {
//	        for (int col = 0; col < numOfBinsX; col++) {
//	            tiles[row][col] = new TileInMemSimple(row, col);
//	        }
//	    }
//	}
//
//	protected int calculateRangesCount(double rangeWidth, double min, double max) {
//	    double range = max - min;
//	    return (int) Math.ceil(range / rangeWidth);
//	}
//
//	protected double calculateRangesWidth(double stdDev, double datasetCount) {
//		double denominator = Math.pow(datasetCount, 1.0 / 3.0);
//		double scottRuleRangeWidth = 3.49 * stdDev / denominator;		
//	    return scottRuleRangeWidth;
//	}

}