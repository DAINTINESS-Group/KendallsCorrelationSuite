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
        printResults("Apache", filePath, apacheResult, 0.0);

        /* TILES WITH LISTS*/
        IListBasedKendallCalculator lbtbMgr = methods.createKendallCalculatorByString("ListBasedTiles");
        //ListBasedTileBasedKendallManager lbtbMgr = new ListBasedTileBasedKendallManager();
        double listTileKendallResult =lbtbMgr.calculateKendall(columnPair);

        
        printResults("List Tiles", filePath, listTileKendallResult, 0.0);
        
    }


	private static void printResults(String methodName, String filePath, double kendallResult, double elapsedTimeSeconds) {
		// Print the result
        System.out.println("\n\n" + methodName + " method for file " + filePath);
        System.out.println(methodName + " kendallValue:\t" + kendallResult);
        System.out.println(methodName+" elapsed time:\t" + elapsedTimeSeconds + " seconds");
        System.out.println(" ----- \n");
	}
}