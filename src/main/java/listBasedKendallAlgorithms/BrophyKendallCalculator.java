package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;

import java.util.Objects;

/*
 * THis class calculates kendall's tau b following the algorithm by ALFRED L. BROPHY */
public class BrophyKendallCalculator implements IListBasedKendallCalculator {

    private double concordantPairs = 0.0;
    private double discordantPairs = 0.0;
    private double tiedPairsSumX = 0.0;
    private double tiedPairsSumY = 0.0;
    private double tauB = 0.0;
    private double pValue = 0.0;

    @Override
    public double calculateKendall(ColumnPair pair) {
        double[] xColumn = pair.getXColumn().stream().mapToDouble(Double::doubleValue).toArray();
        double[] yColumn = pair.getYColumn().stream().mapToDouble(Double::doubleValue).toArray();
        double size = xColumn.length;

        // Variables for tied ranks
        double tiedPairsInX = 0;
        double tiedPairsInY = 0;

        // Main loop to calculate concordance, discordance, and ties
        for (int i = 0; i < size - 1; i++) {
            double tiedRanksX = 0;
            double tiedRanksY = 0;
            for (int j = i + 1; j < size; j++) {
                double deltaX = xColumn[i] - xColumn[j];
                double deltaY = yColumn[i] - yColumn[j];
                double product = deltaX * deltaY;

                if (product != 0) {
                    if (product > 0)
                        concordantPairs++;
                    else
                        discordantPairs++;
                } else {
                    if (Objects.equals(xColumn[i], xColumn[j])) {
                        tiedRanksX++;
                        tiedPairsInX++;
                    }
                    if (Objects.equals(yColumn[i], yColumn[j])) {
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

        tauB = (concordantPairs - discordantPairs) / Math.sqrt(adjustmentForTies);

        double zScore = (Math.abs(concordantPairs - discordantPairs) - 1.0) / Math.sqrt(denominator);
        pValue = calculatePValue(zScore);

        return tauB;
    }

    /**
     * Calculates the p-value based on the Z-score using a normal approximation.
     *
     * @param zScore The Z-score for which to calculate the p-value.
     * @return The calculated p-value.
     */
    private double calculatePValue(double zScore) {
        double pValue = 0.5 - Math.sqrt(1 - Math.exp(-zScore * (0.6366198 - zScore * (0.009564224 - zScore * 0.0004)))) / 2;
        if (zScore < 0) {
            return 1.0 - pValue;
        }
        return pValue;
    }

    public String toString() {
        return "Brophy's { Kendall's Tau b: " + tauB +
                ", ConcordantPairs: " + concordantPairs +
                ", DiscordantPairs: " + discordantPairs +
                ", Tied pairs on X: " + tiedPairsSumX +
                ", Tied pairs on Y: " + tiedPairsSumY +
                ", pValue: " + pValue +
                " }";
    }
}
