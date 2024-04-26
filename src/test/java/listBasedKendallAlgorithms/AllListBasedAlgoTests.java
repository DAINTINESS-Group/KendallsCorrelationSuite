package listBasedKendallAlgorithms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ 
	BrophyCalculatorTest.class, BruteForceCalculatorTest.class, ListBasedKendallFactoryTest.class, ReaderTest.class,
	ListBasedSimpleTileAndPointsManagerTest.class, ListBasedBandsWithMemoryManagerTest.class })
public class AllListBasedAlgoTests {

}
