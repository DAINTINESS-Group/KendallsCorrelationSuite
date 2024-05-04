package tiles.tilemgr.rangemaker;

import common.ColumnsStatistics;

public class RangeMakerFactory {
	
	public RangeMakerInterface makeRangeMakerFixedNumBins(ColumnsStatistics columnStatistics, int numBinsX, int numBinsY) {
		return new RangeMakerFixedNumBins(columnStatistics, numBinsX, numBinsY) ;
	}

	public RangeMakerInterface makeRangeMakerScottRule(ColumnsStatistics columnStatistics) {
		return new RangeMakerScottRule(columnStatistics) ;
	}


}//end class
