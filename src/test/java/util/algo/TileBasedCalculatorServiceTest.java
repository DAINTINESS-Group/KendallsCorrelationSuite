package util.algo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import listBasedKendallAlgorithms.listBasedReader.Reader;
import sparkBasedKendallAlgorithms.SparkSessionTestSetup;

import sparkBasedKendallAlgorithms.reader.DatasetReader;
import util.tilemgr.TilesManagerSparkReaderTilesInMemSimple;
import util.tilemgr.ITilesManager;
import util.tilemgr.TilesManagerListReaderTilesInMemSimple;
//import util.tilemgr.TilesManagerListReaderTilesInMemWCounters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TileBasedCalculatorServiceTest extends SparkSessionTestSetup {

    private final String path;
    private final String column1;
    private final String column2;
    private final String delimiter;
    private final double expected;

    public TileBasedCalculatorServiceTest(
            String path, String column1, String column2, String delimiter, double expected) {
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
        DatasetReader datasetReader = new DatasetReader(spark, path, delimiter);
        Dataset<Row> dataset = datasetReader.read(column1, column2);
        ITilesManager tilesManagerSparkBased = new TilesManagerSparkReaderTilesInMemSimple(dataset, column1, column2);
        TileBasedCalculatorService service = new TileBasedCalculatorService(tilesManagerSparkBased);
        //TileBasedCalculatorService service = new TileBasedCalculatorService(dataset, column1, column2);

        double actual = service.calculateKendallTauCorrelation(AlgoEnum.SPARK_TILES_ALGO);

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
        
		//SIMPLE TILES MANAGER
		ITilesManager tilesManagerListBased = new TilesManagerListReaderTilesInMemSimple(pair);
        //SIMPLE TILES SERVICE
        TileBasedCalculatorService service = new TileBasedCalculatorService(tilesManagerListBased);
        double actual = service.calculateKendallTauCorrelation(AlgoEnum.SIMPLE_TILES_AND_POINT_COMPARISONS);

        // Assert
        double delta = 0.0;
		System.out.println("\nSimple List-Based Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);
    }
    
//    @Test
//    public void testCalculateListTilesWithCounters() {
//        Reader reader = new Reader();
//		ColumnPair pair = null;
//		try {
//			pair = reader.read(path, column1, column2, delimiter);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        
//		//TILES WITH COUNTERS
//		ITilesManager tilesManagerWithCounters = new TilesManagerListReaderTilesInMemWCounters(pair);
//        //SERVICE WITH NEW TILES WITH MEMORY
//        TileBasedCalculatorService service = new TileBasedCalculatorService(tilesManagerWithCounters);
//        double actual = service.calculateKendallTauCorrelation(AlgoEnum.BANDS_WITH_VISIT_MEMORY);
//
//        // Assert
//        double delta = 0.0;
//		System.out.println("\nBands With Memory Kendall (Tau B)");
//		System.out.println("Expected:\t" + expected);
//		System.out.println("Actual  :\t" + actual);
//        assertEquals(expected, actual, delta);
//    }
    
}
