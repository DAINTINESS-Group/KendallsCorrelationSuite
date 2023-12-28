package listBasedKendallAlgorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Reader {

    /**
     * Reads a CSV file, extracts data from specified columns, and stores it in a ColumnPair object.
     *
     * @param filePath    The path to the CSV file to be read.
     * @param xColumnName The name of the column to use for X-values.
     * @param yColumnName The name of the column to use for Y-values.
     * @param delimiter   The delimiter used in the CSV file (e.g., ",", ";", "\t").
     * @return A ColumnPair object containing the extracted X and Y values.
     * @throws IOException              If an I/O error occurs while reading the file.
     * @throws IllegalArgumentException If the specified column names are not found in the header or if
     *                                  there are not enough columns in the CSV data.
     */
    public ColumnPair read(String filePath, String xColumnName, String yColumnName, String delimiter) throws IOException, IllegalArgumentException {
        ColumnPair columnPair = new ColumnPair();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = reader.readLine().split(delimiter);

            if (headers.length < 2) {
                throw new IllegalArgumentException("Not enough columns in the CSV data.");
            }

            int xColumnIndex = -1;
            int yColumnIndex = -1;

            // Find the column indices for the specified column names
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equals(xColumnName)) {
                    xColumnIndex = i;
                }
                if (headers[i].equals(yColumnName)) {
                    yColumnIndex = i;
                }
            }

            if (xColumnIndex == -1 || yColumnIndex == -1) {
                throw new IllegalArgumentException("Column names not found in the header.");
            }

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(delimiter);

                // Skip lines with empty cells
                if (row.length <= Math.max(xColumnIndex, yColumnIndex) || row[xColumnIndex].isEmpty() || row[yColumnIndex].isEmpty()) {
                    continue;
                }

                try {
                    double xValue = Double.parseDouble(row[xColumnIndex]);
                    double yValue = Double.parseDouble(row[yColumnIndex]);

                    columnPair.addX(xValue);
                    columnPair.addY(yValue);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error parsing data in the CSV: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File not found: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Error reading the file: " + e.getMessage());
        }

        if (columnPair.getXColumn().isEmpty() && columnPair.getYColumn().isEmpty()) {
            throw new IOException("Empty file: " + filePath);
        }

        return columnPair;
    }
}
