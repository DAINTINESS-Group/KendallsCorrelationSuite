package listBasedKendallAlgorithms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import listBasedKendallAlgorithms.reader.ReaderTest;


@RunWith(Suite.class)
@SuiteClasses({ 
	BrophyCalculatorTest.class, BruteForceCalculatorTest.class, IListBasedKendallFactoryTest.class, ReaderTest.class,
	TilesWithSimplePointChecksListReaderKendallCalculatorTest.class, TileBandsWithMemoryKendallCalculatorTest.class,
	TilesMergeSortListReaderKendallCalculatorTest.class, TilesWithSimpleSortersListReaderKendallCalculatorTest.class })
public class AllListBasedAlgoTests {

}
