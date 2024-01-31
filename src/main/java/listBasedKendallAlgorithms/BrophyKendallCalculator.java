package listBasedKendallAlgorithms;

import java.util.ArrayList;
import java.util.Objects;

public class BrophyKendallCalculator implements IListBasedKendallCalculator {

    @Override
    public double calculateKendall(ColumnPair pair) {
        // Extracting columns and initializing key variables
        ArrayList<Double> xColumn = pair.getXColumn();
        ArrayList<Double> yColumn = pair.getYColumn();
        double size = xColumn.size();
        double concordantMinusDiscordant = 0;

        // Variables for tied ranks
        double tiedPairsInX = 0, tiedPairsSumX = 0;
        double tiedPairsInY = 0, tiedPairsSumY = 0;

        // Main loop to calculate concordance, discordance, and ties
        for (int i = 0; i < size - 1; i++) {
            double tiedRanksX = 0;
            double tiedRanksY = 0;
            for (int j = i + 1; j < size; j++) {
                double deltaX = xColumn.get(j) - xColumn.get(i);
                double deltaY = yColumn.get(j) - yColumn.get(i);
                double product = deltaX * deltaY;

                if (product != 0) {
                    concordantMinusDiscordant += Math.signum(product);
                } else {
                    if (Objects.equals(xColumn.get(i), xColumn.get(j))) {
                        tiedRanksX++;
                        tiedPairsInX++;
                    }
                    if (Objects.equals(yColumn.get(i), yColumn.get(j))) {
                        tiedRanksY++;
                        tiedPairsInY++;
                    }
                }
            }
            tiedPairsSumX += tiedRanksX * (tiedRanksX - 1);
            tiedPairsSumY += tiedRanksY * (tiedRanksY - 1);
        }

        // Calculating totals and adjustments for ties
        double totalPairs = size * (size - 1) / 2;
        double adjustmentForTies = (totalPairs - tiedPairsInX) * (totalPairs - tiedPairsInY);

        double correctionFactor = size * (size - 1) * (size - 2);
        double denominator = ((correctionFactor / 3 - tiedPairsSumX) * (correctionFactor / 3 - tiedPairsSumY)) / correctionFactor
                + adjustmentForTies / totalPairs;

        // Calculating Kendall Tau
        double kendallTau = concordantMinusDiscordant / Math.sqrt(adjustmentForTies);

        // Calculating p-value using Normal approximation
        double zScore = (Math.abs(concordantMinusDiscordant) - 1.0) / Math.sqrt(denominator);
        double pValue = calculatePValue(zScore);

        return kendallTau;
    }

    /**
     * Calculates the p-value based on the Z-score using a normal approximation.
     *
     * @param zScore The Z-score for which to calculate the p-value.
     * @return The calculated p-value.
     */
    private double calculatePValue(double zScore) {
        double firstPart = 0.5 - Math.sqrt(1 - Math.exp(-zScore * (0.6366198 - zScore * (0.009564224 - zScore * 0.0004)))) / 2;
        if (zScore < 0) {
            return 1.0 - firstPart;
        }
        return firstPart;
    }
}