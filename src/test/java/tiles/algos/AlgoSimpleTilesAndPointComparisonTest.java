package tiles.algos;

import org.apache.spark.sql.AnalysisException;
//import org.apache.spark.sql.Dataset;
//import org.apache.spark.sql.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import common.ColumnPair;
import listBasedKendallAlgorithms.TilesWithSimplePointChecksListReaderKendallCalculator;
import listBasedKendallAlgorithms.reader.Reader;
import sparkBasedKendallAlgorithms.SparkSessionTestSetup;
import sparkBasedKendallAlgorithms.TilesWithSimplePointChecksSparkReaderKendallCalculator;
import util.TileConstructionParameters;
import util.TileConstructionParameters.RangeMakingMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class AlgoSimpleTilesAndPointComparisonTest extends SparkSessionTestSetup {

    private final String path;
    private final String column1;
    private final String column2;
    private final String delimiter;
    private final double expected;

    public AlgoSimpleTilesAndPointComparisonTest(String path, String column1, String column2, String delimiter, double expected) {
        this.path = path;
        this.column1 = column1;
        this.column2 = column2;
        this.delimiter = delimiter;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"src\\test\\resources\\testInput\\TestFile.tsv", "X", "Y", "\t", 0.04957330142836763},
                {"src\\test\\resources\\testInput\\cars_10kTest.csv", "mpg", "mileage", ",", 0.2943112515766309},
                {"src\\test\\resources\\input\\cars_100k.csv", "mpg", "mileage", ",", 0.23002983829926982},
        });
    }

    @Test
    public void testCalculateSparkKendallTauCorrelationWithDifferentDatasets() {
//        DatasetReader datasetReader = new DatasetReader(spark, path, delimiter);
//        Dataset<Row> dataset = datasetReader.read(column1, column2);
        //ITilesManager tilesManagerSparkBased = new TilesManagerSparkReaderTilesInMemSimple(dataset, column1, column2);
        
        TilesWithSimplePointChecksSparkReaderKendallCalculator tilesWithSimplePointChecksSparkReaderKendallCalculator = new TilesWithSimplePointChecksSparkReaderKendallCalculator();
        try {
			tilesWithSimplePointChecksSparkReaderKendallCalculator.loadDataset(path, column1, column2);
		} catch (AnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TileConstructionParameters paramsSparkTileList = new TileConstructionParameters.Builder(false)
				.rangeMakingMode(RangeMakingMode.FIXED)
				.numBinsX(50)
				.numBinsY(50)
				.build();
        double actual = tilesWithSimplePointChecksSparkReaderKendallCalculator.calculateKendallTau(column1, column2, paramsSparkTileList);
//        
//        TileBasedCalculatorService service = new TileBasedCalculatorService(tilesManagerSparkBased);
//        double actual = service.calculateKendallTauCorrelation(AlgoEnum.SPARK_TILES_ALGO);

        // Assert
        double delta = 0.0;
		System.out.println("\nSpark-Based Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);
    }
    
    @Test
    public void testCalculateListSimpleTilesSimpleSrvc() {
        Reader reader = new Reader();
		ColumnPair pair = null;
		try {
			pair = reader.read(path, column1, column2, delimiter);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		TileConstructionParameters params = new TileConstructionParameters.Builder(false)
				.rangeMakingMode(RangeMakingMode.FIXED)
				.numBinsX(50)
				.numBinsY(50)
				.build();

		//SIMPLE TILES MANAGER
//		ITilesManager tilesManagerListBased = new TilesManagerListReaderTilesInMemSimple(pair);
        TilesWithSimplePointChecksListReaderKendallCalculator calculator = new TilesWithSimplePointChecksListReaderKendallCalculator(params);
        double actual = calculator.calculateKendall(pair);
//        //SIMPLE TILES SERVICE
//        TileBasedCalculatorService service = new TileBasedCalculatorService(tilesManagerListBased);
//        double actual = service.calculateKendallTauCorrelation(AlgoEnum.SIMPLE_TILES_AND_POINT_COMPARISONS);

        // Assert
        double delta = 0.0;
		System.out.println("\nSimple List-Based Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);
    }
    

    
}
