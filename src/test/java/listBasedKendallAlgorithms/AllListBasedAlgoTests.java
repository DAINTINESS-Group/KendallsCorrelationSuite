package listBasedKendallAlgorithms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ 
	BrophyCalculatorTest.class, BruteForceCalculatorTest.class, ListBasedKendallFactoryTest.class, ReaderTest.class,
	ListBasedTileBasedKendallManagerTest.class })
public class AllListBasedAlgoTests {

}
