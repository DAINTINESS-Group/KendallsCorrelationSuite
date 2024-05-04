package client;


import listBasedKendallAlgorithms.IListBasedKendallCalculator;
import listBasedKendallAlgorithms.IListBasedKendallFactory;
import listBasedKendallAlgorithms.reader.Reader;

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

        Reader reader = new Reader();
        ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);
        IListBasedKendallFactory methods = new IListBasedKendallFactory();

        /* APACHE */
        IListBasedKendallCalculator apacheKendall = methods.createKendallCalculatorByString("Apache kendall");
        double apacheResult = apacheKendall.calculateKendall(columnPair);
        printResults("Apache", filePath, apacheResult, Double.NaN);

        /* SIMPLE TILES WITH LISTS*/
        IListBasedKendallCalculator lbtbMgr	= methods.createKendallCalculatorByString("ListBasedTiles");
        double simpleTileKendallResult 		= lbtbMgr.calculateKendall(columnPair);
        printResults("Simple Tiles, Simple Algo", filePath, simpleTileKendallResult, Double.NaN);

       /* LIST-BASED BANDS WITH MEMORY*/
       IListBasedKendallCalculator bwMMgr	= methods.createKendallCalculatorByString("BandsWithMemory");
       double bandsWithMemoryKendallResult	= bwMMgr.calculateKendall(columnPair);
       printResults("BandsWithMemory", filePath, bandsWithMemoryKendallResult, Double.NaN);
      
       
    }//end main


	private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
        System.out.println("\n ----- \n" + methodName + " method for file " + filePath);
        System.out.println(methodName + " Kendall tau value:\t" + kendallResult);
        System.out.println(methodName+" elapsed time (sec):\t" + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
	}
}//end class