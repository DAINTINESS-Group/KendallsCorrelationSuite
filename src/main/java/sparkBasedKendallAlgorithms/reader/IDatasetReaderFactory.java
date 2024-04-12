package sparkBasedKendallAlgorithms.reader;

import static sparkBasedKendallAlgorithms.reader.DatasetReaderConstants.*;

import org.apache.spark.sql.SparkSession;
import org.sparkproject.guava.io.Files;

public class IDatasetReaderFactory {
    private final SparkSession sparkSession;

    public IDatasetReaderFactory(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    public IDatasetReader createDatasetReader(String path) {
        String fileExtension = Files.getFileExtension(path);

        switch (fileExtension) {
            case TSV:
                return new DatasetReader(sparkSession, path, TSV_DELIMITER);
            case CSV:
                return new DatasetReader(sparkSession, path, CSV_DELIMITER);
        }
        throw new IllegalArgumentException(String.format("File %s is not a supported dataset type.", path));
    }
}
