package listBasedKendallAlgorithms;

import java.util.Objects;

import common.ColumnPair;

/*
 * This class calculates kendall's tau b following the algorithm by ALFRED L. BROPHY */
public class BrophyKendallCalculator implements IListBasedKendallCalculator {
	private long totalPairs = 0L;
    private long concordantPairs = 0L;
    private long discordantPairs = 0L;
    private long tiedPairsInX = 0L;
    private long tiedPairsInY = 0L;
    private long tiedPairsInXY = 0L;
    
//    private double tiedPairsSumX = 0.0;
//    private double tiedPairsSumY = 0.0;
    private double tauB = 0.0;

    @Override
    public double calculateKendall(ColumnPair pair) {
        double[] xColumn = pair.getXColumn().stream().mapToDouble(Double::doubleValue).toArray();
        double[] yColumn = pair.getYColumn().stream().mapToDouble(Double::doubleValue).toArray();
        double size = xColumn.length;

        // Main loop to calculate concordance, discordance, and ties
        for (int i = 0; i < size - 1; i++) {
//            double tiedRanksX = 0;
//            double tiedRanksY = 0;
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
                        //tiedRanksX++;
                        tiedPairsInX++;
                    }
                    if (Objects.equals(yColumn[i], yColumn[j])) {
                        //tiedRanksY++;
                        tiedPairsInY++;
                    }
                    if((Objects.equals(xColumn[i], xColumn[j]))&&(Objects.equals(yColumn[i], yColumn[j])))
                    	tiedPairsInXY++;
                }
            }
//            tiedPairsSumX += tiedRanksX * (tiedRanksX - 1);
//            tiedPairsSumY += tiedRanksY * (tiedRanksY - 1);
        }

        // Calculating totals and adjustments for ties
        double totalPairsDouble = (size * (size - 1) / 2); this.totalPairs = (long) totalPairsDouble;
        double adjustmentForTies = (totalPairsDouble - tiedPairsInX) * (totalPairsDouble - tiedPairsInY);

//        double correctionFactor = size * (size - 1) * (size - 2);
//        @SuppressWarnings("unused")
//		double denominator = ((correctionFactor / 3 - tiedPairsSumX) * (correctionFactor / 3 - tiedPairsSumY)) / correctionFactor
//                + adjustmentForTies / totalPairs;
        double nominator =((double)(concordantPairs - discordantPairs));
        double denominator = Math.sqrt(adjustmentForTies);
        tauB = nominator / denominator;
//System.err.println(nominator + "\t /" + denominator);
        return tauB;
    }

    public String toString() {
        return "Brophy's { Kendall's Tau b: " + tauB + "\n" +
        		", TotalPairs: " + totalPairs + "\n" +
                ", ConcordantPairs:\t" + concordantPairs + "\n" +
                ", DiscordantPairs:\t" + discordantPairs + "\n" +
                ", Tied pairs on X:\t" + tiedPairsInX + "\n" +
                ", Tied pairs on Y:\t" + tiedPairsInY + "\n" +
                ", Tied pairs on XY:\t" + tiedPairsInXY + "\n" +
//                ", Tied pairs on X: " + tiedPairsSumX +
//                ", Tied pairs on Y: " + tiedPairsSumY +
                " }";
    }
}
