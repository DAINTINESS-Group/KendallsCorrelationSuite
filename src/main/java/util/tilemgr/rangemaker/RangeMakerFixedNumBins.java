package util.tilemgr.rangemaker;

//import java.math.BigDecimal;

import util.common.ColumnsStatistics;

public class RangeMakerFixedNumBins implements RangeMakerInterface {

	
	private ColumnsStatistics columnStatistics;
	private int numBinsX;
	private int numBinsY;
	
	public RangeMakerFixedNumBins(ColumnsStatistics columnStatistics, int numBinsX, int numBinsY) {
		this.columnStatistics = columnStatistics;
		this.numBinsX = numBinsX;
		this.numBinsY = numBinsY;
	}

	@Override
	public RangeMakerResult divideColumnsInBinsAndRanges() {
		RangeMakerResult result = new RangeMakerResult();
		double min = Double.NaN;
		double max = Double.NaN;
		double fullRange = Double.NaN;
		double rangeWidth  = Double.NaN;
//		BigDecimal bigDecimal = null;
//		int intValue = -1;
		int numBins = -1;
		
		numBins = numBinsX;
		min = columnStatistics.getMinX();
		max = columnStatistics.getMaxX();
		fullRange = max - min;
		rangeWidth = fullRange / numBins;
//		bigDecimal = new BigDecimal(String.valueOf(rangeWidth));
//		intValue = bigDecimal.intValue();
//		if (0.0 != rangeWidth - intValue)
//			numBins++;
		result.numberOfBinsX = numBins;
		result.rangeWidthX = rangeWidth;
		
		numBins = numBinsY;
		min = columnStatistics.getMinY();
		max = columnStatistics.getMaxY();
		fullRange = max - min;
		rangeWidth = fullRange / numBins;
//		bigDecimal = new BigDecimal(String.valueOf(rangeWidth));
//		intValue = bigDecimal.intValue();
//		if (0.0 != rangeWidth - intValue)
//			numBins++;
		result.numberOfBinsY = numBins;
		result.rangeWidthY = rangeWidth;
		
		return result;
	}
	
	
}
