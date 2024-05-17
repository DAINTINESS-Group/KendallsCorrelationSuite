package client;


import listBasedKendallAlgorithms.IListBasedKendallCalculator;
import listBasedKendallAlgorithms.IListBasedKendallFactory;
import listBasedKendallAlgorithms.IListBasedKendallFactory.KendallCalculatorMethods;
import listBasedKendallAlgorithms.reader.Reader;
import util.TileConstructionParameters;
import util.TileConstructionParameters.RangeMakingMode;

import java.io.IOException;

import common.ColumnPair;

public class ClientV0_DebugFastNewAlgo {
    public static void main(String[] args) throws IOException  {

        //String filePath = "src\\test\\resources\\testInput\\TauAData.tsv";
        //String filePath = "src\\test\\resources\\testInput\\TestFile.tsv";
        String filePath = "src\\test\\resources\\testInput\\ReferenceTestFile.tsv";
        String column1 = "X";
        String column2 = "Y";
        String delimiter = "\t";


		TileConstructionParameters paramsTileList = new TileConstructionParameters.Builder(false)
				.rangeMakingMode(RangeMakingMode.SCOTT_RULE)
				.build();

        Reader reader = new Reader();
        ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);
        IListBasedKendallFactory methods = new IListBasedKendallFactory();

        /* APACHE */
        IListBasedKendallCalculator apacheKendall = methods.createKendallCalculator(KendallCalculatorMethods.APACHE, null);
        double apacheResult = apacheKendall.calculateKendall(columnPair);
        printResults("Apache", filePath, apacheResult, Double.NaN);

        /* SIMPLE TILES WITH LISTS*/
        IListBasedKendallCalculator lbtbMgr	= methods.createKendallCalculator(KendallCalculatorMethods.SIMPLE_TILES_LIST, paramsTileList);
        double simpleTileKendallResult 		= lbtbMgr.calculateKendall(columnPair);
        printResults("Simple Tiles, Simple Algo", filePath, simpleTileKendallResult, Double.NaN);

       /* LIST-BASED BANDS WITH MEMORY*/
       IListBasedKendallCalculator bwMMgr	= methods.createKendallCalculator(KendallCalculatorMethods.BANDS_WITH_MEMORY, paramsTileList);
       double bandsWithMemoryKendallResult	= bwMMgr.calculateKendall(columnPair);
       printResults("BandsWithMemory", filePath, bandsWithMemoryKendallResult, Double.NaN);

       
       /* SIMPLE TILES WITH MERGESORT*/
       IListBasedKendallCalculator msMgr	= methods.createKendallCalculator(KendallCalculatorMethods.MERGESORT, paramsTileList);
       double msTileKendallResult 		= msMgr.calculateKendall(columnPair);
       printResults("Simple Tiles, SortMerge", filePath, msTileKendallResult, Double.NaN);

       
       
       
    }//end main


	private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
        System.out.println("\n ----- \n" + methodName + " method for file " + filePath);
        System.out.println(methodName + " Kendall tau value:\t" + kendallResult);
        System.out.println(methodName+" elapsed time (sec):\t" + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
	}
}//end class