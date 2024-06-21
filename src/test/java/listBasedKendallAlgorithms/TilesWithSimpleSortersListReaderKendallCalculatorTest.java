package listBasedKendallAlgorithms;

import org.junit.BeforeClass;
import org.junit.Test;

import common.ColumnPair;
import listBasedKendallAlgorithms.reader.Reader;
import util.TileConstructionParameters;
import util.TileConstructionParameters.RangeMakingMode;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

public class TilesWithSimpleSortersListReaderKendallCalculatorTest {
	private static 	Reader reader;
	private static TilesWithSimpleSortersListReaderKendallCalculator ssMgr;
	
	@BeforeClass
	public static void setup() {
		TileConstructionParameters params = new TileConstructionParameters.Builder(false)
				.rangeMakingMode(RangeMakingMode.FIXED)
				.numBinsX(50)
				.numBinsY(50)
				.build();
		reader = new Reader();
		ssMgr = new TilesWithSimpleSortersListReaderKendallCalculator(params);
	}
	
	@Test
	public final void testCalculateKendallTauA() {
		String filePath = "src/test/resources/testInput/TauAData.tsv";
        String column1 = "X";
        String column2 = "Y";

		ColumnPair pair = null;
		try {
			pair = reader.read(filePath, column1, column2, "\t");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        double actual =ssMgr.calculateKendall(pair);
        double expected = 0.23076923076923078;
        double delta = 0.0;
		System.out.println("\nList-Based Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);

	}//end method

	@Test
	public final void testCalculateKendallTestFile() {
		String filePath = "src\\test\\resources\\testInput\\TestFile.tsv";
        String column1 = "X";
        String column2 = "Y";

		ColumnPair pair = null;
		try {
			pair = reader.read(filePath, column1, column2, "\t");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        double actual =ssMgr.calculateKendall(pair);
        double expected = 0.04957330142836763;
        double delta = 0.0;
		System.out.println("\nList-Based Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);

	}//end method	

	@Test
	public final void testCalculateKendallCars10K() {
		
		String filePath = "src\\test\\resources\\testInput\\cars_10kTest.csv";
        String column1 = "mpg";
        String column2 = "mileage";

		ColumnPair pair = null;
		try {
			pair = reader.read(filePath, column1, column2, ",");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        double actual =ssMgr.calculateKendall(pair);
        double expected = 0.2943112515766309;
        double delta = 0.0;
		System.out.println("\nList-Based Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);

	}//end method	
	
	@Test
	public final void testCalculateKendallCars100K() {
        String filePath = "src\\test\\resources\\input\\cars_100k.csv";
        String column1 = "mpg";
        String column2 = "mileage";

		ColumnPair pair = null;
		try {
			pair = reader.read(filePath, column1, column2, ",");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        double actual =ssMgr.calculateKendall(pair);
        double expected = 0.23002983829926982;
        double delta = 0.0;

		System.out.println("\nList-Based Kendall (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);
        assertEquals(expected, actual, delta);

	}//end method	
	
}//end class
