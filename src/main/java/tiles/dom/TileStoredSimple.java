package tiles.dom;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import tiles.dom.writer.CsvReaderSimplest;
import tiles.dom.writer.WriterSetup;
import tiles.dom.writer.WriterSimple;

public class TileStoredSimple implements Serializable, ITile {

    private static final long serialVersionUID = 4581980665140154578L;
    private static final String DELIMITER = "\t";
    
	private final int row;
    private final int column;
    private List<DoublePair> valuePairs;
    private long pairCounter = 0;
    
    private String fileName = null;
    private String fullFilePath = null;
    private WriterSimple writer = null;
    private boolean loadedStatus = false;
    
    public TileStoredSimple(int row, int column) {
        this.valuePairs = new ArrayList<>();
        this.row = row;
        this.column = column;
        
        fileName = "tile_" + row +"_" + column + ".tsv";
        fullFilePath = WriterSetup.getOutputExecDir() + File.separator + fileName;      
        try {
			writer = new WriterSimple(fullFilePath);
		} catch (IOException e) {
			String msg = e.toString() + "\n CANNOT SUPPORT TILE CREATION. EXITING";
			System.err.println(msg);
			System.exit(-1);
		}
    }

    @Override
	public boolean isEmpty() {
        return pairCounter == 0;
    }

    @Override
	public long getCount() {
        return pairCounter;
    }

    @Override
	public List<DoublePair> getValuePairs() {
    	int linesRead = -1;
    	if(loadedStatus == false) {
    		CsvReaderSimplest reader = new CsvReaderSimplest();
    		linesRead = reader.feedMyList(fullFilePath, DELIMITER, this.valuePairs);
    		loadedStatus = true;
    		if(linesRead != pairCounter)
    			System.err.println("For file " + fileName + 
    					" lines read: " + linesRead +
    					" instead of expected: " + pairCounter);
    	}
        return valuePairs;
    }
    
    @Override
	public void addValuePair(DoublePair pair) {
        //valuePairs.add(pair);
    	String line = pair.getX() + DELIMITER + pair.getY(); 
    	try {
			writer.writeWithEOL(line);
		} catch (IOException e) {
			String msg = e.toString() + "\n CANNOT SUPPORT TILE " + line + " POPULATION. EXITING";
			System.err.println(msg);
			System.exit(-1);		}
        pairCounter++;
    }

    @Override
	public int getRow() {
        return row;
    }

    @Override
	public int getColumn() {
        return column;
    }

//    @Override
//    public String toString() {
//        return "TileInMemSimple{" +
//                "row=" + row +
//                ", column=" + column +
//                ", pairCounter=" + pairCounter +
//                '}';
//    }
    
	@Override
	public SortedMap<Double, Integer> getOccurencesPerX() {
		return null;
	}

	@Override
	public SortedMap<Double, Integer> getOccurencesPerY() {
		return null;
	}

    public void finalizeTilePopulation() {
    	if(null == writer)
    		return;
    	try {
			writer.closeFile();
		} catch (IOException e) {
			String msg = e.toString() + "\n CANNOT SUPPORT WRITER.CLOSE() FOR TILE " + fileName + " . EXITING";
			System.err.println(msg);
			System.exit(-1);
		}
    }


	@Override
	public int setValuePairs(List<DoublePair> pairList) {
		this.valuePairs = pairList;
		return this.valuePairs.size();
	}
    
    @Override
    public String toString() {
    	String vStr="";
    	for(DoublePair p: getValuePairs()) {
    		vStr = vStr + p.toString();
    	}
    	
        return "TileInMemSimple{" +
                "row=" + row +
                ", column=" + column +
                ", pairs=" + vStr +
                '}';
    }

}
