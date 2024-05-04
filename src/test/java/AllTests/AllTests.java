package AllTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import listBasedKendallAlgorithms.AllListBasedAlgoTests;
import sparkBasedKendallAlgorithms.AllTileBasedTests;
import tiles.algos.AllAlgoTests;

@RunWith(Suite.class)
@SuiteClasses({
	AllListBasedAlgoTests.class,
	AllTileBasedTests.class,
	AllAlgoTests.class,
})
public class AllTests {

}
