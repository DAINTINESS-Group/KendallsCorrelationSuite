package listBasedKendallAlgorithms;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;

public class BrophyCalculatorTest {

	@Test
	public final void testCalculateKendallHappy() throws IOException {
		String filePath = "src/test/resources/input/TauAData.tsv";
		Reader reader = new Reader();

		ColumnPair pair = reader.read(filePath, "X", "Y", "\t");

		ApacheCommonsKendall commons = new ApacheCommonsKendall();
		double expected = commons.calculateKendallTau(pair.getXColumn(), pair.getYColumn());
		
		IListBasedKendallCalculator listBasedKendallCalculator = new BrophyKendallCalculator();
		double actual = listBasedKendallCalculator.calculateKendall(pair);

		System.out.println("\nBrophy Test (Tau B)");
		System.out.println("Expected: " + expected);
		System.out.println("Actual: " + actual);

		assertEquals(expected, actual, 0.0);
	}

	//TODO: rainyday tests!!!! What happens in files with NaN, missing values, ...
	//Maybe it is the job of Reader to filter out offending records.
}
