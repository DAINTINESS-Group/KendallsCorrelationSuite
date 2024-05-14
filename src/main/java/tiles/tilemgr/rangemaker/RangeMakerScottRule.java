package tiles.tilemgr.rangemaker;

import java.io.Serializable;

import common.ColumnsStatistics;

public class RangeMakerScottRule implements RangeMakerInterface, Serializable {
	private static final long serialVersionUID = 1L;
	private static final long UPPER_LIMIT_NUM_TILES = 10000;
	private ColumnsStatistics columnStatistics;
	
	public RangeMakerScottRule(ColumnsStatistics columnStatistics) {
		this.columnStatistics = columnStatistics;
	}

	@Override
	public RangeMakerResult divideColumnsInBinsAndRanges() {
		RangeMakerResult result = new RangeMakerResult();
		double min = Double.NaN;
		double max = Double.NaN;
		double stdDev = Double.NaN;
		//double rangeWidth  = Double.NaN;
		double rangeWidthX = Double.NaN;
		double rangeWidthY = Double.NaN;
		long rowCount = columnStatistics.getRowCount();
		
		min = columnStatistics.getMinX();
		max = columnStatistics.getMaxX();
		stdDev = columnStatistics.getStdDevX();
		rangeWidthX  = calculateRangesWidthWithScottRule(stdDev, rowCount);
		result.rangeWidthX = rangeWidthX;
		int localNumberBinsX = calculateNumberOfBins(rangeWidthX, min, max);
	
		min = columnStatistics.getMinY();
		max = columnStatistics.getMaxY();
		stdDev = columnStatistics.getStdDevY();
		rangeWidthY  = calculateRangesWidthWithScottRule(stdDev, rowCount);
		result.rangeWidthY = rangeWidthY;
		int localNumberBinsY = calculateNumberOfBins(rangeWidthY, min, max);


		//Test to avoid ultra large #tiles, leading to outOfMemory exceptions
		int totalNumTiles = localNumberBinsX * localNumberBinsY;
		while (totalNumTiles > UPPER_LIMIT_NUM_TILES) {
			result.rangeWidthX = result.rangeWidthX * 10;
			result.rangeWidthY = result.rangeWidthY * 10;
			localNumberBinsX = calculateNumberOfBins(result.rangeWidthX, columnStatistics.getMinX(), columnStatistics.getMaxX());
			localNumberBinsY = calculateNumberOfBins(result.rangeWidthY, columnStatistics.getMinY(), columnStatistics.getMaxY());
			totalNumTiles = localNumberBinsX * localNumberBinsY; 
		}

		result.numberOfBinsX = localNumberBinsX; 
		result.numberOfBinsY = localNumberBinsY;
		
		return result;
	}
	
	protected double calculateRangesWidthWithScottRule(double stdDev, double datasetCount) {
		double denominator = Math.pow(datasetCount, 1.0 / 3.0);
		double scottRuleRangeWidth = 3.49 * stdDev / denominator;		
	    return scottRuleRangeWidth;
	}
	
	protected int calculateNumberOfBins(double rangeWidth, double min, double max) {
		double range = max - min;
	    return (int) Math.ceil(range / rangeWidth);
	}
	
}
