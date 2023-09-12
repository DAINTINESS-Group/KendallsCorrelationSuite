package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    public ColumnPair read(String filePath, String xColumnName, String yColumnName) {
        ColumnPair columnPair = new ColumnPair();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = reader.readLine().split(";");

            int xColumnIndex = -1;
            int yColumnIndex = -1;

            // Find the column indices for the specified column names
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase(xColumnName)) {
                    xColumnIndex = i;
                }
                if (headers[i].equalsIgnoreCase(yColumnName)) {
                    yColumnIndex = i;
                }
            }

            if (xColumnIndex == -1 || yColumnIndex == -1) {
                throw new IllegalArgumentException("Column names not found in the header.");
            }

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(";");
                columnPair.addX(Double.parseDouble(row[xColumnIndex]));
                columnPair.addY(Double.parseDouble(row[yColumnIndex]));
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return columnPair;
    }
}

