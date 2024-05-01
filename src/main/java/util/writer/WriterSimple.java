package util.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriterSimple {

	protected String filePath;
	protected BufferedWriter fileWriter; 

	public WriterSimple (String fullFileName) throws IOException{
		String thisFile = (new File(fullFileName)).getName();
		String slashedPath = getDir(fullFileName) + File.separator;
		filePath = slashedPath + thisFile;
//System.err.println(slashedPath + "    " + thisFile + "    " + filePath);
		initWriter();
	}
	
	private String getDir(String path) {
		String parent = (new File(path)).getParent();
		File directory = new File(parent);
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory.getPath();
	}
	
	
	private void initWriter() throws IOException{
		try {
			fileWriter = new BufferedWriter(new FileWriter(filePath));
		} catch (IOException e) {
			String msg = e.toString() + "\n... when opening the writer";;
			throw new IOException(msg);
		}
	}
	

	public void writeHeader(String header) throws IOException{
		writeWithEOL(header);
	}
	
	public void writeWithEOL(String stuff) throws IOException{
		if(stuff.endsWith("\n"))
			;
		else
			stuff.concat("\n");
		writeText(stuff);
	}
	
	public void writeText(String stuff) throws IOException{
		try {
			fileWriter.write(stuff);
		} catch (IOException e) {
			String msg = e.toString() + "\n... when writing " + stuff;
			throw new IOException(msg);
		}
	}
	
	public void closeFile() throws IOException{
		try {
			fileWriter.close();
		} catch (IOException e) {
			String msg = e.toString() + "\n... when closing the writer";
			throw new IOException(msg);
		}
	}

	

	



}