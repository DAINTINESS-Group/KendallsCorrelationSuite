package listBasedKendallAlgorithms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import listBasedKendallAlgorithms.listBasedTiles.*;

@RunWith(Suite.class)
@SuiteClasses({ 
	BrophyCalculatorTest.class, BruteForceCalculatorTest.class, ListBasedKendallMethodsServiceTest.class, ReaderTest.class,
	ListBasedTileBasedKendallManagerTest.class })
public class AllListBasedAlgoTests {

}
