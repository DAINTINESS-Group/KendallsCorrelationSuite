package reader;

import org.sparkproject.guava.io.Files;
import org.apache.spark.sql.SparkSession;

public class IDatasetReaderFactory {
    private final SparkSession sparkSession;

    public IDatasetReaderFactory(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    public IDatasetReader createDatasetReader(String path) {
        String fileExtension = Files.getFileExtension(path);

        switch(fileExtension) {
            case DatasetReaderConstants.TSV:
                return new DatasetReader(sparkSession, path, DatasetReaderConstants.TSV_DELIMITER);
            case DatasetReaderConstants.CSV:
                return new DatasetReader(sparkSession, path, DatasetReaderConstants.CSV_DELIMITER);
        }
        throw new IllegalArgumentException(String.format("File %s is not a supported dataset type.", path));
    }

}
