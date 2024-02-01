package listBasedKendallAlgorithms;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;

public class BruteForceCalculatorTest {

	@Test
	public final void testCalculateKendallHappy() throws IOException {
		Reader reader = new Reader();
		String filePath = "src/test/resources/input/TauAData.tsv";
		ColumnPair pair = reader.read(filePath, "X", "Y", "\t");

		ApacheCommonsKendall commons = new ApacheCommonsKendall();
		double expected = commons.calculateKendallTau(pair.getXColumn(), pair.getYColumn());
		
		IListBasedKendallCalculator listBasedKendallCalculator = new BruteForceNoTiesKendallCalculator(); 
		double actual = listBasedKendallCalculator.calculateKendall(pair);

		System.out.println("\nBrophy Test (Tau B)");
		System.out.println("Expected: " + expected);
		System.out.println("Actual: " + actual);

		assertEquals(expected, actual, 0.0);
	}
}
