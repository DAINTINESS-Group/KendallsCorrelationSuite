package tiles.tilemgr;

import java.util.List;

import common.ColumnPair;
import common.ColumnsStatistics;
import tiles.dom.DoublePair;
import tiles.dom.TileInMemWCounters;
import tiles.tilemgr.rangemaker.RangeMakerFactory;
import tiles.tilemgr.rangemaker.RangeMakerInterface;
import tiles.tilemgr.rangemaker.RangeMakerResult;
import util.TileConstructionParameters;
import util.TileConstructionParameters.RangeMakingMode;

public class TilesManagerListReaderTilesInMemWCounters implements ITilesManager  {
	//protected RangeMakerMethodEnum rangeMakerMethod;
	
	protected boolean DEBUG_FLAG = false;
	protected boolean EXPERIMENT_FLAG = false;
	protected TileConstructionParameters parameters; 
	protected RangeMakingMode rangeMakerMethod;
	
	protected final ColumnPair pair;
	protected static TileInMemWCounters[][] tiles;
	protected long datasetRowCount;
	protected int numOfBinsX;
	protected int numOfBinsY;
	protected double rangeWidthX;
	protected double rangeWidthY;
	protected ColumnsStatistics columnsStatistics;


	public TilesManagerListReaderTilesInMemWCounters(ColumnPair pair, TileConstructionParameters parameters) {
		this.pair = pair;
        this.DEBUG_FLAG = parameters.isDebugModeOn();
        this.EXPERIMENT_FLAG = parameters.isExperimentModeOn();
        this.parameters = parameters;
        this.rangeMakerMethod = this.parameters.getRangeMakingMode();
	}

	public TileInMemWCounters[][] createTilesArray() {
		
			double start = System.currentTimeMillis();
		calculateMinMaxColumnValues();
			double end = System.currentTimeMillis();
			double elapsed = (end - start) / 1000.0;
			if(EXPERIMENT_FLAG) {
				System.out.println("X,Y min and max and stddev took: " + elapsed + " seconds");
			}

			start = System.currentTimeMillis();
		setupTilesArrayMetadata();
			end = System.currentTimeMillis();
			elapsed = (end - start) / 1000.0;
			if(EXPERIMENT_FLAG) {
				System.out.println("Tiles bin number and binWidth calculations took: " + elapsed + " seconds");
			}
			if(DEBUG_FLAG) {
				System.out.println("#BinsX: " + numOfBinsX + "\n#BinsY: " + numOfBinsY + "\nTotal tiles: " + numOfBinsX * numOfBinsY);
				System.out.println("RangeWidthX: " + rangeWidthX + "\n#RangeWidthY: " + rangeWidthY + "\nTotal #tuples: " + datasetRowCount);
			}

			start = System.currentTimeMillis();
		initializeTilesArray();
			end = System.currentTimeMillis();
			elapsed = (end - start) / 1000.0;
			if(EXPERIMENT_FLAG) {
				System.out.println("Tiles initialization took: " + elapsed + " seconds");
			}

			start = System.currentTimeMillis(); // Timing population
		populateTiles();
			end = System.currentTimeMillis();
			elapsed = (end - start) / 1000.0;
			if(EXPERIMENT_FLAG) {
				System.out.println("Tiles Population took " + elapsed + " seconds\n");
			}
			if(DEBUG_FLAG) {
				int row, column, count = -1;
				for (TileInMemWCounters[] rowOfTiles : tiles) {
					for (TileInMemWCounters tile : rowOfTiles) {
						if (!tile.isEmpty()) {
							row = tile.getRow();
							column = tile.getColumn();
							count = tile.getCount();
							System.err.printf("Count[%d,%d]:\t%d\n",row,column,count);
						}
					}
				}
			}
		return tiles;
	}//end method

	protected void setupTilesArrayMetadata() {
		RangeMakerFactory factory = new RangeMakerFactory();
		RangeMakerInterface rangeMaker = null;
		switch(this.rangeMakerMethod) {
			case FIXED: 
				final int BINS_X = parameters.getNumBinsX();
				final int BINS_Y = parameters.getNumBinsY();
				rangeMaker = factory.makeRangeMakerFixedNumBins(columnsStatistics, BINS_X, BINS_Y);
				break;
			case SCOTT_RULE:  
				rangeMaker = factory.makeRangeMakerScottRule(columnsStatistics);
				break;
			default://implies SCOTT too
				this.rangeMakerMethod = RangeMakingMode.SCOTT_RULE;
				rangeMaker = factory.makeRangeMakerScottRule(columnsStatistics);
		}
		RangeMakerResult result = rangeMaker.divideColumnsInBinsAndRanges();
		rangeWidthX = result.getRangeWidthX();
		rangeWidthY = result.getRangeWidthY();
		numOfBinsX = result.getNumberOfBinsX();
		numOfBinsY = result.getNumberOfBinsY();
//System.err.println("RANGEMAKER " + result.toString()+"\n");	
//		double datasetRowCountAsDouble = (double) this.datasetRowCount;
//		rangeWidthX = calculateRangesWidth(columnsStatistics.getStdDevX(), datasetRowCountAsDouble);
//		rangeWidthY = calculateRangesWidth(columnsStatistics.getStdDevY(), datasetRowCountAsDouble);       
//		numOfBinsX = calculateRangesCount(rangeWidthX, columnsStatistics.getMinX(), columnsStatistics.getMaxX());
//		numOfBinsY = calculateRangesCount(rangeWidthY, columnsStatistics.getMinY(), columnsStatistics.getMaxY());
	}

	protected int calculateRangesCount(double rangeWidth, double min, double max) {
		double range = max - min;
		return (int) Math.ceil(range / rangeWidth);
	}

	//Scott's rule https://en.wikipedia.org/wiki/Scott%27s_rule
	protected double calculateRangesWidth(double stdDev, double datasetCount) {
		double denominator = Math.pow(datasetCount, 1.0 / 3.0);
		double scottRuleRangeWidth = 3.49 * stdDev / denominator;		
		return scottRuleRangeWidth;
	}


	protected final void populateTiles() {
		List<Double> xList = pair.getXColumn();
		List<Double> yList = pair.getYColumn();
		double minX = columnsStatistics.getMinX();
		double maxY = columnsStatistics.getMaxY();

		for (int i=0; i<this.datasetRowCount;i++ ) {
			double valueX = xList.get(i);
			double valueY = yList.get(i);

			int tileRow = (int) Math.min(numOfBinsY - 1, Math.floor((maxY - valueY) / rangeWidthY));
			int tileColumn = (int) Math.min(numOfBinsX - 1, Math.floor((valueX - minX) / rangeWidthX));

			if (tileRow >= 0 && tileRow < numOfBinsY && tileColumn >= 0 && tileColumn < numOfBinsX) {
				synchronized (tiles[tileRow][tileColumn]) {
					tiles[tileRow][tileColumn].addValuePair(new DoublePair(valueX, valueY));
				}
			} else {
				throw new ArrayIndexOutOfBoundsException("Tried to access out of bounds array cell.\n" +
						"i: " + i + " tileRow: " + tileRow + " tileCol: " + tileColumn
						);
			}
		}
	}


	protected final void calculateMinMaxColumnValues() {
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
		this.datasetRowCount = pair.getSize();
		stdDevX = computeStdDev(sumValueX, xList);
		stdDevY = computeStdDev(sumValueY, yList);



		columnsStatistics = new ColumnsStatistics(this.datasetRowCount, minValueX, maxValueX, minValueY, maxValueY, stdDevX, stdDevY);
	}

	private double computeStdDev(double sumValue, List<Double> aList) {
		double meanValueX = Double.NaN;
		double stdDevX = 0;

		meanValueX = sumValue / (double) this.datasetRowCount;
		Double var = 0.0;
		for (Double d: aList) {
			var += Math.pow(d - meanValueX, 2);
		}
		var = var / (double) this.datasetRowCount;
		stdDevX =  Math.sqrt(var);
		return stdDevX;
	}

	protected void initializeTilesArray() {
		tiles = new TileInMemWCounters[this.numOfBinsY][this.numOfBinsX];    	
		for (int row = 0; row < numOfBinsY; row++) {
			for (int col = 0; col < numOfBinsX; col++) {
				tiles[row][col] = new TileInMemWCounters(row, col); 
			}
		}
	}
}
