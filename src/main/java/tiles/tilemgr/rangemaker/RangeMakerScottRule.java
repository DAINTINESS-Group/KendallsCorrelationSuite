package tiles.tilemgr.rangemaker;

import common.ColumnsStatistics;

public class RangeMakerScottRule implements RangeMakerInterface {
	
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
		double rangeWidth  = Double.NaN;
		long rowCount = columnStatistics.getRowCount();
		
		min = columnStatistics.getMinX();
		max = columnStatistics.getMaxX();
		stdDev = columnStatistics.getStdDevX();
		rangeWidth  = calculateRangesWidthWithScottRule(stdDev, rowCount);
		result.rangeWidthX = rangeWidth;
		result.numberOfBinsX = calculateNumberOfBins(rangeWidth, min, max);
	
		min = columnStatistics.getMinY();
		max = columnStatistics.getMaxY();
		stdDev = columnStatistics.getStdDevY();
		rangeWidth  = calculateRangesWidthWithScottRule(stdDev, rowCount);
		result.rangeWidthY = rangeWidth;
		result.numberOfBinsY = calculateNumberOfBins(rangeWidth, min, max);

		
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
