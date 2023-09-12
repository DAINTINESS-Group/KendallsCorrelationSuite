package model;

import reader.ColumnPair;

import java.util.ArrayList;

public class BruteForceCalculator implements ICalculator{

    public double calculateKendall(ColumnPair pair) {
        int n = pair.getXColumn().size();
        double denominator = (double) (n * (n-1)) / 2.0;
        double numerator = calculateNumerator(pair);

        return numerator / denominator;
    }

    private double calculateNumerator(ColumnPair pair) {
        double numerator = 0.0;

        ArrayList<Double> x = pair.getXColumn();
        ArrayList<Double> y = pair.getYColumn();

        for (int i = 1; i < x.size(); i++) {
            for (int j = 0; j < i; j++) {
                double diffX = pair.getXColumn().get(i) - pair.getXColumn().get(j);
                double diffY = pair.getYColumn().get(i) - pair.getYColumn().get(j);

                numerator += Math.signum(diffX) * Math.signum(diffY);
            }
        }

        return numerator;
    }
}
