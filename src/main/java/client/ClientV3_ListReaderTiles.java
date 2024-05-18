package client;

import listBasedKendallAlgorithms.*;
import listBasedKendallAlgorithms.IListBasedKendallFactory.KendallCalculatorMethods;
import listBasedKendallAlgorithms.reader.Reader;
import util.TileConstructionParameters;
import util.TileConstructionParameters.RangeMakingMode;

import java.io.IOException;

import common.ColumnPair;

//import java.io.IOException;


public class ClientV3_ListReaderTiles {
    public static void main(String[] args) throws IllegalArgumentException, IOException {

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
//        
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

         
		/* APACHE */
		startTime = System.currentTimeMillis();
		IListBasedKendallCalculator apacheKendall = methods.createKendallCalculator(KendallCalculatorMethods.APACHE, null);
		double apacheResult = apacheKendall.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0;
		printResults("Apache", filePath, apacheResult, elapsedTimeSeconds);
		checkMemory();

		int NUM_BINS_X = 100;
		int NUM_BINS_Y = 100;
		TileConstructionParameters paramsTileList = new TileConstructionParameters.Builder(false)
//				.rangeMakingMode(RangeMakingMode.SCOTT_RULE)
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
		checkMemory();

		/* SIMPLE TILES WITH MERGESORT*/
		startTime = System.currentTimeMillis();
		IListBasedKendallCalculator msMgr	= methods.createKendallCalculator(KendallCalculatorMethods.MERGESORT, paramsTileList);
		double msTileKendallResult 	= msMgr.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
		printResults("Simple Tiles, SortMerge", filePath, msTileKendallResult, elapsedTimeSeconds);
		checkMemory();
		
		/* TILES WITH MEMORY*/
		startTime = System.currentTimeMillis();
		IListBasedKendallCalculator bwmMgr = methods.createKendallCalculator(KendallCalculatorMethods.BANDS_WITH_MEMORY, paramsTileList);
		double bandsWithMemoryKendallResult =bwmMgr.calculateKendall(columnPair);
		endTime = System.currentTimeMillis();
		elapsedTimeSeconds = (endTime - startTime) / 1000.0; 
		printResults("Bands With Memory", filePath, bandsWithMemoryKendallResult, elapsedTimeSeconds);
		checkMemory();
		
    }//end main


    private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
    	System.out.println("\n\n" + " ----- ");
        System.out.println(methodName + " method for file " + filePath);
        System.out.println(methodName + " Kendall tau value:\t" + kendallResult);
        System.out.println(methodName+" elapsed time (sec):\t" + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
	}
    
	private static void checkMemory() {
		System.out.println("Calling garbageCollector");
		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		long total = runtime.totalMemory();
		long free = runtime.freeMemory();
		long usedMemory= total - free;
		System.out.println("Used/Free/Total Memory (MB): "+ bytesToMegabytes(usedMemory) + 
				"\t" + bytesToMegabytes(free) + "\t" + bytesToMegabytes(total));
	}


	public static long bytesToMegabytes(long bytes) {
		final long MEGABYTE = 1024L * 1024L;
		return bytes / MEGABYTE;
	}
    
}//end class