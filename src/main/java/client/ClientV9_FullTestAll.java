package client;

import java.io.File;
import java.io.IOException;

import org.apache.spark.sql.AnalysisException;

import common.ColumnPair;
import listBasedKendallAlgorithms.*;
import listBasedKendallAlgorithms.IListBasedKendallFactory.KendallCalculatorMethods;
import listBasedKendallAlgorithms.reader.Reader;
import sparkBasedKendallAlgorithms.TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator;
import tiles.dom.writer.WriterSetup;
import util.TileConstructionParameters;
import util.TileConstructionParameters.RangeMakingMode;
import sparkBasedKendallAlgorithms.TilesWithSimplePointChecksSparkReaderKendallCalculator;


public class ClientV9_FullTestAll {
	public static void main(String[] args) throws IOException, AnalysisException {
	
		//74001 tuples
		String filePath = "src\\test\\resources\\input\\acs2017_census_tract_data.csv";
		String column1 = "Hispanic";
		String column2 = "Native";
		String delimiter = ",";

		//74001 tuples
		//        String filePath = "src\\test\\resources\\input\\acs2017_census_tract_data.csv";
		//        String column1 = "Men";
		//        String column2 = "Women";
		//        String delimiter = ",";

		//108,539 tuples
		// manufacturer,model,year,price,transmission,mileage,fuelType,tax,mpg,engineSize
		//        String filePath = "src\\test\\resources\\input\\cars_100k.csv";
		//        String column1 = "mileage";
		//        String column2 = "mpg";
		//        String delimiter = ",";

		//619,040 tuples
		//        String filePath = "src\\test\\resources\\input\\all_stocks_5yr.csv";
		//        String column1 = "low";
		//        String column2 = "high";
		//        String delimiter = ",";

		//619,040 tuples
		//        String filePath = "src\\test\\resources\\input\\all_stocks_5yr.csv";
		//        String column1 = "close";
		//        String column2 = "volume";
		//        String delimiter = ",";

		//1,000,000 = 1 million tuples
		//        String filePath = "src\\test\\resources\\input\\Random1Mil.csv";
		//        String column1 = "X";
		//        String column2 = "Y";
		//        String delimiter = ",";

		//5,819,079 tuples
		//        String filePath = "src\\test\\resources\\input\\flights5_7m.csv"; // flight cancels 2015
		//        String column1 = "FLIGHT_NUMBER";
		//        String column2 = "DEPARTURE_TIME";
		//        String delimiter = ",";

		int NUM_BINS_X = 50;
		int NUM_BINS_Y = 50;
		long startTime = -1;
		long endTime = -1;
		double elapsedTimeSeconds = -1.0;

		IListBasedKendallFactory methods = new IListBasedKendallFactory();
		startTime = System.currentTimeMillis();
		Reader reader = new Reader();
		ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		printResults("ValueReader For ALL lists: ", filePath, Double.NaN, elapsedTimeSeconds);

		/* BRUTE */
		IListBasedKendallCalculator bruteForceTauA = methods.createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE, null);
		startTime = System.currentTimeMillis();
		double actualBruteForce = bruteForceTauA.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		printResults("Brute Force", filePath, actualBruteForce, elapsedTimeSeconds);      

		/* BROPHY */
		IListBasedKendallCalculator brophyKendallTauB = methods.createKendallCalculator(KendallCalculatorMethods.BROPHY, null);
		startTime = System.currentTimeMillis();
		double actualBrophy = brophyKendallTauB.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		printResults("Brophy", filePath, actualBrophy, elapsedTimeSeconds);

		
		/* APACHE */
		startTime = System.currentTimeMillis();
		IListBasedKendallCalculator apacheKendall = methods.createKendallCalculator(KendallCalculatorMethods.APACHE, null);
		double apacheResult = apacheKendall.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		printResults("Apache", filePath, apacheResult, elapsedTimeSeconds);


		TileConstructionParameters paramsTileList = new TileConstructionParameters.Builder(false)
				.rangeMakingMode(RangeMakingMode.FIXED)
				.numBinsX(NUM_BINS_X)
				.numBinsY(NUM_BINS_Y)
				.build();
		
		/* TILES WITH LISTS*/
		startTime = System.currentTimeMillis();
		IListBasedKendallCalculator lbtbMgr = methods.createKendallCalculator(KendallCalculatorMethods.SIMPLE_TILES_LIST, paramsTileList);
		double listTileKendallResult =lbtbMgr.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
		printResults("List Tiles", filePath, listTileKendallResult, elapsedTimeSeconds);

		/* TILES WITH MEMORY*/
		startTime = System.currentTimeMillis();
		IListBasedKendallCalculator bwmMgr = methods.createKendallCalculator(KendallCalculatorMethods.BANDS_WITH_MEMORY, paramsTileList);
		double bandsWithMemoryKendallResult =bwmMgr.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
		printResults("Bands With Memory", filePath, bandsWithMemoryKendallResult, elapsedTimeSeconds);

		/* SIMPLE TILES WITH MERGESORT*/
		startTime = System.currentTimeMillis();
		IListBasedKendallCalculator msMgr	= methods.createKendallCalculator(KendallCalculatorMethods.MERGESORT, paramsTileList);
		double msTileKendallResult 	= msMgr.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
		printResults("Simple Tiles, SortMerge", filePath, msTileKendallResult, elapsedTimeSeconds);
	
		
		/* Tile Implementation with SPARK and valuePairs*/
		startTime = System.currentTimeMillis();
		TilesWithSimplePointChecksSparkReaderKendallCalculator tilesWithSimplePointChecksSparkReaderKendallCalculator = new TilesWithSimplePointChecksSparkReaderKendallCalculator();
		tilesWithSimplePointChecksSparkReaderKendallCalculator.loadDataset(filePath, column1, column2);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		System.out.println("Spark InitialSetup and Dataset loading took: " + elapsedTimeSeconds + "\n");
	
		TileConstructionParameters paramsSparkTileList = new TileConstructionParameters.Builder(false)
				.rangeMakingMode(RangeMakingMode.FIXED)
				.numBinsX(NUM_BINS_X)
				.numBinsY(NUM_BINS_Y)
				.build();
		startTime = System.currentTimeMillis();
		double sparkListKendall = tilesWithSimplePointChecksSparkReaderKendallCalculator.calculateKendallTau(column1, column2, paramsSparkTileList);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		printResults("Spark w. Simple InMem Tiles", filePath, sparkListKendall, elapsedTimeSeconds);
	
		/* Tile Implementation with SPARK and stored tiles*/
		startTime = System.currentTimeMillis();            
		TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator= new TilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator();
		tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.loadDataset(filePath, column1, column2);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		System.out.println("Spark InitialSetup and Dataset loading took: " + elapsedTimeSeconds + "\n");

		TileConstructionParameters paramsSparkTileStored = new TileConstructionParameters.Builder(false)
				.rangeMakingMode(RangeMakingMode.FIXED)
				.numBinsX(NUM_BINS_X)
				.numBinsY(NUM_BINS_Y)
				.build();
		startTime = System.currentTimeMillis();
		double sparkTileStoredResult = tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.calculateKendallTau(column1, column2, paramsSparkTileStored);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		printResults("Spark: Simple Structure + Stored Tiles", filePath, sparkTileStoredResult, elapsedTimeSeconds);

		Thread deleteThread = new Thread(() -> {
			boolean deletionFlag = tilesWithSimplePointChecksSparkReaderStoredTilesKendallCalculator.deleteSubFolders(new File(WriterSetup.getOutputExecDir()));
			System.err.println("Cleanup of tiles at " + WriterSetup.getOutputExecDir() + " was " + deletionFlag);
		});
		deleteThread.start();
	}


	private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
		System.out.println("\n\n" + " ----- ");
		System.out.println(methodName + " method for file " + filePath);
		System.out.println(methodName + " Kendall tau value:\t" + kendallResult);
		System.out.println(methodName+" elapsed time (sec):\t" + elapsedTimeSeconds + " seconds");
		System.out.println(" ----- \n");
	}
}