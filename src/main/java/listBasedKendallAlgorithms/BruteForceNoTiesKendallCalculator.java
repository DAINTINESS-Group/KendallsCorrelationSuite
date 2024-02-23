package listBasedKendallAlgorithms;

import java.util.ArrayList;

/*
 * This class calculates kendall's tau_a which means that it does not account for ties
 * In case there are tries in the dataset the result will not be accurate
 */
public class BruteForceNoTiesKendallCalculator implements IListBasedKendallCalculator {
    private double concordantPairs = 0.0;
    private double discordantPairs = 0.0;
    private double tauA = 0.0;

    public double calculateKendall(ColumnPair pair) {
        double numPairs = pair.getXColumn().size();
        calculateConcordantAndDiscordantPairs(pair);

        double denominator = (numPairs * (numPairs - 1)) / 2.0;
        double numerator = concordantPairs - discordantPairs;
        tauA = numerator / denominator;

        return tauA;
    }

    private void calculateConcordantAndDiscordantPairs(ColumnPair pair) {
        double[] x = pair.getXColumn().stream().mapToDouble(Double::doubleValue).toArray();
        double[] y = pair.getYColumn().stream().mapToDouble(Double::doubleValue).toArray();
        long numPairs = x.length;

        double diffX;
        double diffY;
        double product;

        for (int i = 1; i < numPairs; i++) {
            for (int j = 0; j < i; j++) {
                diffX = x[i] - x[j];
                diffY = y[i] - y[j];
                product = Math.signum(diffX) * Math.signum(diffY);
                if (product > 0)
                    concordantPairs++;
                else if (product < 0)
                    discordantPairs++;
            }
        }
    }

    @Override
    public String toString() {
        return "BruteForceNoTiesKendallCalculator{" +
                " ConcordantPairs: " + concordantPairs +
                ", DiscordantPairs: " + discordantPairs +
                ", BruteForce TauA: " + tauA +
                '}';
    }
}
