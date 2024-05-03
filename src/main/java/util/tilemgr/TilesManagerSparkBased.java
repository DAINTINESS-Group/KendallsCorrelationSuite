package util.tilemgr;

import java.io.Serializable;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import static org.apache.spark.sql.functions.*;

import util.common.ColumnsStatistics;
import util.common.DoublePair;
import util.tilemgr.TilesManagerListBasedTilesWithCounters.RangeMakerMethodEnum;
import util.tilemgr.rangemaker.RangeMakerFactory;
import util.tilemgr.rangemaker.RangeMakerInterface;
import util.tilemgr.rangemaker.RangeMakerResult;
import util.tiles.TileStored;



public class TilesManagerSparkBased implements Serializable {
    private static final long serialVersionUID = 8765154256335271048L;
	protected static final boolean DEBUG_FLAG = false;
	
	protected static TileStored[][] tiles;
	protected long datasetRowCount;
	protected int numOfBinsX;
	protected int numOfBinsY;
	protected double rangeWidthX;
	protected double rangeWidthY;
	protected ColumnsStatistics columnsStatistics;

	protected RangeMakerMethodEnum rangeMakerMethod;
	
    private final String column1;
    private final String column2;
    private final Dataset<Row> dataset;

    public TilesManagerSparkBased(Dataset<Row> dataset, String column1, String column2) {
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
		this.rangeMakerMethod = RangeMakerMethodEnum.FIXED; //DECIDE: FIXED or SCOTT?
    }

    
    public TilesManagerSparkBased(Dataset<Row> dataset, String column1, String column2, RangeMakerMethodEnum method) {
        this.column1 = column1;
        this.column2 = column2;
        this.dataset = dataset;
    	this.rangeMakerMethod = method;
    }

	public TileStored[][] createTilesArray() {
		
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
	    	
	    	start = System.currentTimeMillis(); 
	    closeFileWriters();
    		end = System.currentTimeMillis();
    		elapsed = (end - start) / 1000.0;
    		if(DEBUG_FLAG) {
    			System.out.println("Tiles FileWriter close() " + elapsed + " seconds\n");
    		}
	    
	    
	    return tiles;
	}

	protected void setupTilesArrayMetadata() {
		RangeMakerFactory factory = new RangeMakerFactory();
		RangeMakerInterface rangeMaker = null;
		switch(this.rangeMakerMethod) {
		//TODO IMPROVE IMPROVE IMPROVE
		//obviously deserves better.
		//Constructor should get a hashmap of strings with various parameters and move on.
			case FIXED: 
				final int BINS_X = 100;
				final int BINS_Y = 100;
				rangeMaker = factory.makeRangeMakerFixedNumBins(columnsStatistics, BINS_X, BINS_Y);
				break;
			default://implies SCOTT too
				rangeMaker = factory.makeRangeMakerScottRule(columnsStatistics);
		}
		RangeMakerResult result = rangeMaker.divideColumnsInBinsAndRanges();
		rangeWidthX = result.getRangeWidthX();
		rangeWidthY = result.getRangeWidthY();
		numOfBinsX = result.getNumberOfBinsX();
		numOfBinsY = result.getNumberOfBinsY();
//System.err.println("RANGEMAKER " + result.toString()+"\n");	
//		double datasetRowCountAsDouble = (double) this.datasetRowCount;
//	    rangeWidthX = calculateRangesWidth(columnsStatistics.getStdDevX(), datasetRowCountAsDouble);
//	    rangeWidthY = calculateRangesWidth(columnsStatistics.getStdDevY(), datasetRowCountAsDouble);       
//		numOfBinsX = calculateRangesCount(rangeWidthX, columnsStatistics.getMinX(), columnsStatistics.getMaxX());
//	    numOfBinsY = calculateRangesCount(rangeWidthY, columnsStatistics.getMinY(), columnsStatistics.getMaxY());
	}

	protected void closeFileWriters() {
	    for (int row = 0; row < numOfBinsY; row++) {
	        for (int col = 0; col < numOfBinsX; col++) {
	            TileStored tile = tiles[row][col] ;
	            tile.finalizeTilePopulation();
	        }
	    }
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


    
	protected void initializeTilesArray() {
	    tiles = new TileStored[this.numOfBinsY][this.numOfBinsX];    	
	    for (int row = 0; row < numOfBinsY; row++) {
	        for (int col = 0; col < numOfBinsX; col++) {
	            tiles[row][col] = new TileStored(row, col); 

	        }
	    }
	}
	
    protected final void populateTiles() {
        double minX = columnsStatistics.getMinX();
        double maxY = columnsStatistics.getMaxY();
    
    	final int localRangeCountX = this.numOfBinsX;
    	final int localRangeCountY = this.numOfBinsY;
    	final double localRangeWidthX = this.rangeWidthX;
    	final double localRangeWidthY = this.rangeWidthY;        

        dataset.foreach(row -> {
            double valueX = row.getDouble(row.fieldIndex(column1));
            double valueY = row.getDouble(row.fieldIndex(column2));

            int tileRow = (int) Math.min(localRangeCountY - 1, Math.floor((maxY - valueY) / localRangeWidthY));
            int tileColumn = (int) Math.min(localRangeCountX - 1, Math.floor((valueX - minX) / localRangeWidthX));

            if (tileRow >= 0 && tileRow < localRangeCountY && tileColumn >= 0 && tileColumn < localRangeCountX) {
                synchronized (tiles[tileRow][tileColumn]) {
                    tiles[tileRow][tileColumn].addValuePair(new DoublePair(valueX, valueY));
                }
            } else {
                throw new ArrayIndexOutOfBoundsException("Tried to access out of bounds array cell." 
//                		+ this.toString()
                );
            }
        });
    }


    protected final void calculateMinMaxColumnValues() {
        Row result = dataset.agg(
                min(column1).alias("minValueX"),
                max(column1).alias("maxValueX"),
                min(column2).alias("minValueY"),
                max(column2).alias("maxValueY"),
                stddev(column1).alias("std devX"),
                stddev(column2).alias("std devY")
        ).first();

        // Extract the min, max and standard deviation values for both columns
        double minValueX = result.getDouble(0);
        double maxValueX = result.getDouble(1);
        double minValueY = result.getDouble(2);
        double maxValueY = result.getDouble(3);
        double stdDevX = result.getDouble(4);
        double stdDevY = result.getDouble(5);
        this.datasetRowCount = dataset.count();
        
        this.columnsStatistics = new ColumnsStatistics(datasetRowCount, minValueX, maxValueX, minValueY, maxValueY, stdDevX, stdDevY);
    }


	@Override
	public String toString() {
		return "TilesManagerSparkBasedSimple [datasetRowCount=" + datasetRowCount + 
				", numOfBinsX=" + numOfBinsX
				+ ", numOfBinsY=" + numOfBinsY 
				+ ", rangeWidthX=" + rangeWidthX 
				+ ", rangeWidthY=" + rangeWidthY
				+ ", columnsStatistics=" + columnsStatistics.toString() 
				+ "]";
	}

    
}
