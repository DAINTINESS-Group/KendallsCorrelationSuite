package client;


import listBasedKendallAlgorithms.IListBasedKendallCalculator;
import listBasedKendallAlgorithms.ListBasedKendallFactory;
import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import listBasedKendallAlgorithms.listBasedReader.Reader;

import java.io.IOException;

public class ClientDebug {
    public static void main(String[] args) throws IOException  {

        Reader reader = new Reader();

        String filePath = "src\\test\\resources\\testInput\\TauAData.tsv";
        String column1 = "X";
        String column2 = "Y";
        String delimiter = "\t";

        ColumnPair columnPair = reader.read(filePath, column1, column2, delimiter);
        ListBasedKendallFactory methods = new ListBasedKendallFactory();

        /* APACHE */
        IListBasedKendallCalculator apacheKendall = methods.createKendallCalculatorByString("Apache kendall");
        double apacheResult = apacheKendall.calculateKendall(columnPair);
        printResults("Apache", filePath, apacheResult, Double.NaN);

        /* TILES WITH LISTS*/
        IListBasedKendallCalculator lbtbMgr = methods.createKendallCalculatorByString("ListBasedTiles");
        //ListBasedTileBasedKendallManager lbtbMgr = new ListBasedTileBasedKendallManager();
        double listTileKendallResult =lbtbMgr.calculateKendall(columnPair);
       printResults("List Tiles", filePath, listTileKendallResult, Double.NaN);

       /* BRUTE */
       IListBasedKendallCalculator bruteForceTauA = methods.createKendallCalculatorByString("BruteForce");
       double actualBruteForce = bruteForceTauA.calculateKendall(columnPair);
       printResults("Brute Force", filePath, actualBruteForce, Double.NaN);      

       /* BROPHY */
       IListBasedKendallCalculator brophyKendallTauB = methods.createKendallCalculatorByString("Brophy");
       double actualBrophy = brophyKendallTauB.calculateKendall(columnPair);
       printResults("Brophy", filePath, actualBrophy, Double.NaN);
       
       
    }//end main


	private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
        System.out.println("\n ----- \n" + methodName + " method for file " + filePath);
        System.out.println(methodName + " Kendall tau value:\t" + kendallResult);
        System.out.println(methodName+" elapsed time (sec):\t" + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
	}
}//end class