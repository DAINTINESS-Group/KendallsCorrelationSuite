package listBasedKendallAlgorithms;

import static org.junit.Assert.*;

import org.junit.Test;

import listBasedKendallAlgorithms.reader.ColumnPair;
import listBasedKendallAlgorithms.reader.Reader;

import java.io.IOException;

public class BrophyCalculatorTest {

	@Test
	public final void testCalculateKendallHappy() throws IOException {
		String filePath = "src/test/resources/testInput/TauAData.tsv";
		Reader reader = new Reader();

		ColumnPair pair = reader.read(filePath, "X", "Y", "\t");

		IListBasedKendallCalculator apacheCommons = new ApacheCommonsKendall();
		double expected = apacheCommons.calculateKendall(pair);
		
		IListBasedKendallCalculator listBasedKendallCalculator = new BrophyKendallCalculator();
		double actual = listBasedKendallCalculator.calculateKendall(pair);

		System.out.println("\nBrophy Test (Tau B)");
		System.out.println("Expected:\t" + expected);
		System.out.println("Actual  :\t" + actual);

		assertEquals(expected, actual, 0.0);
	}
}
