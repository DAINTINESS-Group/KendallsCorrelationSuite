package listBasedKendallAlgorithms;

import static org.junit.Assert.*;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import listBasedKendallAlgorithms.listBasedReader.Reader;
import org.junit.Test;

import java.io.IOException;

public class BruteForceCalculatorTest {

	@Test
	public final void testCalculateKendallHappy() throws IOException {
		Reader reader = new Reader();
		String filePath = "src/test/resources/testInput/TauAData.tsv";
		ColumnPair pair = reader.read(filePath, "X", "Y", "\t");

		IListBasedKendallCalculator commons = new ApacheCommonsKendall();
		double expected = commons.calculateKendall(pair);
		
		IListBasedKendallCalculator listBasedKendallCalculator = new BruteForceNoTiesKendallCalculator(); 
		double actual = listBasedKendallCalculator.calculateKendall(pair);

		System.out.println("\nBrophy Test (Tau B)");
		System.out.println("Expected: " + expected);
		System.out.println("Actual: " + actual);

		assertEquals(expected, actual, 0.0);
	}
}
