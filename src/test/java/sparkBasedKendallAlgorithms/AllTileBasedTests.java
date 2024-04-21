package sparkBasedKendallAlgorithms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sparkBasedKendallAlgorithms.algo.TileBasedCalculatorServiceTest;
import sparkBasedKendallAlgorithms.reader.DatasetReaderTest;
import sparkBasedKendallAlgorithms.sparkSetup.SparkSetupTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	DatasetManagerTest.class, // no test, just setup for SparkSessionTestSetup.class,
	TileBasedCalculatorServiceTest.class, 
	DatasetReaderTest.class,
	SparkSetupTest.class	
})
public class AllTileBasedTests {

}




