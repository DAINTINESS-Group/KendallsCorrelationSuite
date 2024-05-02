package sparkBasedKendallAlgorithms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sparkBasedKendallAlgorithms.reader.DatasetReaderTest;
import sparkBasedKendallAlgorithms.sparkSetup.SparkSetupTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	SparkBasedKendallManagerSimpleTest.class, 
	SparkBasedKendallManagerTest.class, 
	DatasetReaderTest.class,
	SparkSetupTest.class	
})
public class AllTileBasedTests {

}




