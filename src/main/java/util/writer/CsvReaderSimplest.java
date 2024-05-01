package util.writer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import util.common.DoublePair;

public class CsvReaderSimplest {

    public int feedMyList(String filePath, String delimiter, List<DoublePair> valuePairs) {
    	int linecount = 0;
        String line = "";
        BufferedReader br = null;
        try {
        	br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] data = line.split(delimiter);
                double x = Double.parseDouble(data[0]);
                double y = Double.parseDouble(data[1]);                
                valuePairs.add(new DoublePair(x,y));
                linecount++;
            }

        } catch (IOException e) {
        	String msg = e.toString();
        	System.err.println("Error in file "+ filePath + "\n" + msg);
        	System.exit(-1);

        } catch(NumberFormatException e) {
        	String msg = e.toString();
        	System.err.println("Error in line: " + linecount + " in file "+ filePath + "\n" + msg);
        	System.exit(-1);
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
            	String msg = ex.toString();
            	System.err.println("Cannot close the buffered reader?\n" + msg);
            }
        }
        return linecount;
    }
	
}
