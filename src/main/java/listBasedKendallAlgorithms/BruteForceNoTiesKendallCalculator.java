package listBasedKendallAlgorithms;

import java.util.ArrayList;

public class BruteForceNoTiesKendallCalculator implements IListBasedKendallCalculator{

    public double calculateKendall(ColumnPair pair) {
        long numPairs = pair.getXColumn().size();
        double denominator = ((double) numPairs * ((double) numPairs-1)) / 2.0;
        double numerator = calculateNumerator(pair);

        return numerator / denominator;
    }

    private double calculateNumerator(ColumnPair pair) {
        double numerator = 0.0;

        ArrayList<Double> x = pair.getXColumn();
        ArrayList<Double> y = pair.getYColumn();
        long numPairs = x.size();
        
        for (int i = 1; i < numPairs; i++) {
            for (int j = 0; j < i; j++) {
                double diffX = x.get(i) - x.get(j);
                double diffY = y.get(i) - y.get(j);

                numerator += Math.signum(diffX) * Math.signum(diffY);
            }
        }

        return numerator;
    }
}
