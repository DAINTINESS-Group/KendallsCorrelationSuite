package kendall;

import static org.junit.Assert.*;

import org.junit.Test;

import reader.ColumnPair;
import reader.Reader;

public class BrophyCalculatorTest {

	@Test
	public final void testCalculateKendallHappy() {
		Reader reader = new Reader();
		String filePath = "src/test/resources/input/TauAData.csv";
		ColumnPair pair = reader.read(filePath, "z", "x");

		ApacheCommonsKendall commons = new ApacheCommonsKendall();
		double expected = commons.calculateKendallTau(pair.getXColumn(), pair.getYColumn());
		
		IKendallCalculator kendallCalculator = new BrophyCalculator(); 
		double actual = kendallCalculator.calculateKendall(pair);

		System.out.println("\nBrophy Test (Tau B)");
		System.out.println("Expected: " + expected);
		System.out.println("Actual: " + actual);

		assertEquals(expected, actual, 0.0);
	}

	
	//TODO: rainyday tests!!!! What happens in files with NaN, missing values, ...
	//Maybe it is the job of Reader to filter out offending records.
}
