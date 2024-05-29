package tiles.tilemgr;

import java.io.Serializable;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

//import common.ColumnsStatistics;
//import tiles.dom.DoublePair;
import tiles.dom.ITile;
import tiles.dom.ITileFactory;
import tiles.dom.ITileType;
import util.TileConstructionParameters;

//import static org.apache.spark.sql.functions.*;


public class TilesManagerSparkReaderTilesInMemSimple extends TilesManagerSparkBasedAbstractClass implements Serializable {
    private static final long serialVersionUID = 8765154256335271048L;
	protected boolean DEBUG_FLAG ;
	protected boolean EXPERIMENT_FLAG ;
	protected TileConstructionParameters parameters; 

//    private final String column1;
//    private final String column2;
//    private final Dataset<Row> dataset;

    public TilesManagerSparkReaderTilesInMemSimple(Dataset<Row> dataset, String column1, String column2, TileConstructionParameters parameters) {
    	super(column1, column2, dataset, parameters);

        this.tileType = ITileType.WITH_COUNTER_MAPS;
        this.tileFactory = new ITileFactory();
//        this.DEBUG_FLAG = parameters.isDebugModeOn();
//        this.EXPERIMENT_FLAG = parameters.isExperimentModeOn();
//        this.parameters = parameters;
    }

    @Override
	protected void initializeTilesArray() {
	    tiles = new ITile[this.numOfBinsY][this.numOfBinsX];    	
	    for (int row = 0; row < numOfBinsY; row++) {
	        for (int col = 0; col < numOfBinsX; col++) {
	            tiles[row][col] = tileFactory.createTile(this.tileType, row, col); 
	        }
	    }
	}
	
//    protected final void populateTiles() {
//        double minX = columnsStatistics.getMinX();
//        double maxY = columnsStatistics.getMaxY();
//    
//    	final int localRangeCountX = this.numOfBinsX;
//    	final int localRangeCountY = this.numOfBinsY;
//    	final double localRangeWidthX = this.rangeWidthX;
//    	final double localRangeWidthY = this.rangeWidthY;        
//
//        dataset.foreach(row -> {
//            double valueX = row.getDouble(row.fieldIndex(column1));
//            double valueY = row.getDouble(row.fieldIndex(column2));
//
//            int tileRow = (int) Math.min(localRangeCountY - 1, Math.floor((maxY - valueY) / localRangeWidthY));
//            int tileColumn = (int) Math.min(localRangeCountX - 1, Math.floor((valueX - minX) / localRangeWidthX));
//
//            if (tileRow >= 0 && tileRow < localRangeCountY && tileColumn >= 0 && tileColumn < localRangeCountX) {
//                synchronized (tiles[tileRow][tileColumn]) {
//                    tiles[tileRow][tileColumn].addValuePair(new DoublePair(valueX, valueY));
//                }
//            } else {
//                throw new ArrayIndexOutOfBoundsException("Tried to access out of bounds array cell." 
////                		+ this.toString()
//                );
//            }
//        });
//    }
//
//
//    protected final void calculateMinMaxColumnValues() {
//        Row result = dataset.agg(
//                min(column1).alias("minValueX"),
//                max(column1).alias("maxValueX"),
//                min(column2).alias("minValueY"),
//                max(column2).alias("maxValueY"),
//                stddev(column1).alias("std devX"),
//                stddev(column2).alias("std devY")
//        ).first();
//
//        // Extract the min, max and standard deviation values for both columns
//        double minValueX = result.getDouble(0);
//        double maxValueX = result.getDouble(1);
//        double minValueY = result.getDouble(2);
//        double maxValueY = result.getDouble(3);
//        double stdDevX = result.getDouble(4);
//        double stdDevY = result.getDouble(5);
//        this.datasetRowCount = dataset.count();
//        
//        this.columnsStatistics = new ColumnsStatistics(datasetRowCount, minValueX, maxValueX, minValueY, maxValueY, stdDevX, stdDevY);
//    }
//

	@Override
	public String toString() {
		return "TilesManagerSparkReaderTilesInMemSimple [datasetRowCount=" + datasetRowCount + 
				", numOfBinsX=" + numOfBinsX
				+ ", numOfBinsY=" + numOfBinsY 
				+ ", rangeWidthX=" + rangeWidthX 
				+ ", rangeWidthY=" + rangeWidthY
				+ ", columnsStatistics=" + columnsStatistics.toString() 
				+ "]";
	}

    
}