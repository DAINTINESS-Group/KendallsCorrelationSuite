package util.algo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import listBasedKendallAlgorithms.listBasedReader.Reader;
import sparkBasedKendallAlgorithms.SparkSessionTestSetup;

import util.tilemgr.TilesManagerListBasedTilesWithCounters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TileXBasedCalculatorServiceTest extends SparkSessionTestSetup {

    private final String path;
    private final String column1;
    private final String column2;
    private final String delimiter;
    private final double expected;

    public TileXBasedCalculatorServiceTest(
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
    public void testCalculateListTilesWithCounters() {
        Reader reader = new Reader();
		ColumnPair pair = null;
		try {
			pair = reader.read(path, column1, column2, delimiter);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		//TILES WITH COUNTERS
		TilesManagerListBasedTilesWithCounters tilesManagerWithCounters = new TilesManagerListBasedTilesWithCounters(pair);
        //SERVICE WITH NEW TILES WITH MEMORY
        TileXBasedCalculatorService service = new TileXBasedCalculatorService(tilesManagerWithCounters);
        double actual = service.calculateKendallTauCorrelation();

        // Assert
        double delta = 0.0;
		System.out.println("\nBands With Memory Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);
    }
    
}