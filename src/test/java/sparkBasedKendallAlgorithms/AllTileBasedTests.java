package sparkBasedKendallAlgorithms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sparkBasedKendallAlgorithms.reader.DatasetReaderTest;
import sparkBasedKendallAlgorithms.sparkSetup.SparkSetupTest;
import util.algo.TileBasedCalculatorServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	DatasetManagerTest.class, // no test, just setup for SparkSessionTestSetup.class, 
	DatasetReaderTest.class,
	SparkSetupTest.class	
})
public class AllTileBasedTests {

}



