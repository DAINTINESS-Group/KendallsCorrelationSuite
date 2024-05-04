package util.tilemgr;


import java.util.List;

import listBasedKendallAlgorithms.reader.ColumnPair;
import util.common.ColumnsStatistics;
import util.common.DoublePair;

public abstract class TilesManagerListBasedAbstractClass extends TilesManagerAbstractClass  {
    
    private final ColumnPair pair;

    protected abstract void initializeTilesArray();
    
    public TilesManagerListBasedAbstractClass(ColumnPair pair) {
        this.pair = pair;
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

}//end class
