package kendall;

import reader.ColumnPair;

import java.util.ArrayList;
import java.util.Objects;

public class BrophyCalculator implements IKendallCalculator{

    @Override
    public double calculateKendall(ColumnPair pair) {
        int size = pair.getXColumn().size();
        ArrayList<Double> x = pair.getXColumn();
        ArrayList<Double> y = pair.getYColumn();

        int s = 0;
        
        /**
         * FIX FIX FIX
         * TODO: why bother using tiedSumX without using it? Y also.
         * Is it useless? Is it forgotten? Fix anyway...
         */
        int t, tiedCountX = 0, tiedSumX = 0;
        int u, tiedCountY = 0, tiedSumY = 0;

        for (int i = 0; i < size-1; i++) {
            t = 0;
            u = 0;
            for (int j = i+1; j <= size-1; j++) {
                double a = (x.get(j) - x.get(i)) * (y.get(j) - y.get(i));
                if (a != 0) {
                    s += Math.signum(a);
                }
                else {
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
            tiedSumX += t*(t-1);
            tiedSumY += u*(u-1);
        }
        int totalPairs = size*(size-1)/2;
        int b = (totalPairs - tiedCountX) * (totalPairs - tiedCountY);

        return s/Math.sqrt(b);
    }

    //TODO Calculate p-value, Variance, etc...

}
