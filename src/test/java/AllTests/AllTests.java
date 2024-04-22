package AllTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import listBasedKendallAlgorithms.AllListBasedAlgoTests;
import sparkBasedKendallAlgorithms.AllTileBasedTests;
import util.algo.TileBasedCalculatorServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	AllListBasedAlgoTests.class,
	AllTileBasedTests.class,
	TileBasedCalculatorServiceTest.class,
})
public class AllTests {

}
