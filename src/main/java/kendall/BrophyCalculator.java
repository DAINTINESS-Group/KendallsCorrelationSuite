package kendall;

import model.ColumnPair;

import java.util.ArrayList;
import java.util.Objects;

public class BrophyCalculator implements IKendallCalculator {

    @Override
    public double calculateKendall(ColumnPair pair) {
        int size = pair.getXColumn().size();
        ArrayList<Double> x = pair.getXColumn();
        ArrayList<Double> y = pair.getYColumn();

        int s = 0;

        int t, tiedCountX = 0, tiedSumX = 0;
        int u, tiedCountY = 0, tiedSumY = 0;

        for (int i = 0; i < size - 1; i++) {
            t = 0;
            u = 0;
            for (int j = i + 1; j < size; j++) {
                double a = (x.get(j) - x.get(i)) * (y.get(j) - y.get(i));

                if (a != 0) {
                    s += Math.signum(a);
                } else {
                    if (Objects.equals(x.get(i), x.get(j))) {
                        t++;
                        tiedCountX++;
                    }
                    if (Objects.equals(y.get(i), y.get(j))) {
                        u++;
                        tiedCountY++;
                    }
                }
            }
            tiedSumX += t * (t - 1);
            tiedSumY += u * (u - 1);
        }

        int totalPairs = (size * (size - 1)) / 2;
        long b = (long) (totalPairs - tiedCountX) * (totalPairs - tiedCountY);

        double l = size * (size - 1) * (size - 2);
        double v = ((l / 3 - tiedSumX) * (l / 3 - tiedSumY)) / l + (double) b / totalPairs;

        double kendallTau = s / Math.sqrt(b);

        // Calculate p-value using Normal approximation
        double z = (Math.abs(s) - 1.0) / Math.sqrt(v);
        double pValue = 0.5 - Math.sqrt(1 - Math.exp(-z * (0.6366198 - z * (0.009564224 - z * 0.0004)))) / 2;
        if (z < 0) {
            pValue = 1.0 - pValue;
        }

        // Print results
        System.out.println("Kendall Tau: " + kendallTau);
        System.out.println("p-Value: " + pValue);
        System.out.println("Z: " + z);

        return kendallTau;
    }
}
