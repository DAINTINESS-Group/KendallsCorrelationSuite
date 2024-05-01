package util.writer;

public class WriterSetup {
	//ref mama dir for all output
	public static String OUTPUT_REF_DIR = "src/test/output/";
	//ATTN: To be set each time differently
	public static String OUTPUT_CUR_DIR = "";
	//once cur_dir has been set, this is ready
	//public static String OUTPUT_EXEC_DIR = OUTPUT_REF_DIR  + OUTPUT_CUR_DIR;
	public static String getOutputExecDir() { return (OUTPUT_REF_DIR  + OUTPUT_CUR_DIR);}
}
