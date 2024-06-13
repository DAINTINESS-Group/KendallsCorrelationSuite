package listBasedKendallAlgorithms;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.util.Pair;

import common.ColumnPair;

public class ApacheCommonsKendallDebugged implements IListBasedKendallCalculator{
    public double calculateKendall(ColumnPair pair) {
        double[] arr1 = pair.getXColumn().stream().mapToDouble(Double::doubleValue).toArray();
        double[] arr2 = pair.getYColumn().stream().mapToDouble(Double::doubleValue).toArray();

        return this.correlation(arr1, arr2);
    }

    
    
    /**
     * Computes the Kendall's Tau rank correlation coefficient between the two arrays.
     *
     * @param xArray first data array
     * @param yArray second data array
     * @return Returns Kendall's Tau rank correlation coefficient for the two arrays
     * @throws DimensionMismatchException if the arrays lengths do not match
     */
    public double correlation(final double[] xArray, final double[] yArray)
            throws DimensionMismatchException {

        if (xArray.length != yArray.length) {
            throw new DimensionMismatchException(xArray.length, yArray.length);
        }

        final int n = xArray.length;
        final long numPairs = sum(n - 1);

        @SuppressWarnings("unchecked")
        Pair<Double, Double>[] pairs = new Pair[n];
        for (int i = 0; i < n; i++) {
            pairs[i] = new Pair<>(xArray[i], yArray[i]);
        }

        Arrays.sort(pairs, new Comparator<Pair<Double, Double>>() {
            /** {@inheritDoc} */
            @Override
            public int compare(Pair<Double, Double> pair1, Pair<Double, Double> pair2) {
                int compareFirst = pair1.getFirst().compareTo(pair2.getFirst());
                return compareFirst != 0 ? compareFirst : pair1.getSecond().compareTo(pair2.getSecond());
            }
        });

        long tiedXPairs = 0;
        long tiedXYPairs = 0;
        long consecutiveXTies = 1;
        long consecutiveXYTies = 1;
        Pair<Double, Double> prev = pairs[0];
        for (int i = 1; i < n; i++) {
            final Pair<Double, Double> curr = pairs[i];
            if (curr.getFirst().equals(prev.getFirst())) {
                consecutiveXTies++;
                if (curr.getSecond().equals(prev.getSecond())) {
                    consecutiveXYTies++;
                } else {
                    tiedXYPairs += sum(consecutiveXYTies - 1);
                    consecutiveXYTies = 1;
                }
            } else {
                tiedXPairs += sum(consecutiveXTies - 1);
                consecutiveXTies = 1;
                tiedXYPairs += sum(consecutiveXYTies - 1);
                consecutiveXYTies = 1;
            }
            prev = curr;
        }
        tiedXPairs += sum(consecutiveXTies - 1);
        tiedXYPairs += sum(consecutiveXYTies - 1);

        long swaps = 0;
        @SuppressWarnings("unchecked")
        Pair<Double, Double>[] pairsDestination = new Pair[n];
        for (int segmentSize = 1; segmentSize < n; segmentSize <<= 1) {
            for (int offset = 0; offset < n; offset += 2 * segmentSize) {
                int i = offset;
                final int iEnd = Math.min(i + segmentSize, n);
                int j = iEnd;
                final int jEnd = Math.min(j + segmentSize, n);

                int copyLocation = offset;
                while (i < iEnd || j < jEnd) {
                    if (i < iEnd) {
                        if (j < jEnd) {
                            if (pairs[i].getSecond().compareTo(pairs[j].getSecond()) <= 0) {
                                pairsDestination[copyLocation] = pairs[i];
                                i++;
                            } else {
                                pairsDestination[copyLocation] = pairs[j];
                                j++;
                                swaps += iEnd - i;
                            }
                        } else {
                            pairsDestination[copyLocation] = pairs[i];
                            i++;
                        }
                    } else {
                        pairsDestination[copyLocation] = pairs[j];
                        j++;
                    }
                    copyLocation++;
                }
            }
            final Pair<Double, Double>[] pairsTemp = pairs;
            pairs = pairsDestination;
            pairsDestination = pairsTemp;
        }

        long tiedYPairs = 0;
        long consecutiveYTies = 1;
        prev = pairs[0];
        for (int i = 1; i < n; i++) {
            final Pair<Double, Double> curr = pairs[i];
            if (curr.getSecond().equals(prev.getSecond())) {
                consecutiveYTies++;
            } else {
                tiedYPairs += sum(consecutiveYTies - 1);
                consecutiveYTies = 1;
            }
            prev = curr;
        }
        tiedYPairs += sum(consecutiveYTies - 1);

        final long concordantMinusDiscordant = numPairs - tiedXPairs - tiedYPairs + tiedXYPairs - 2 * swaps;
        final double nonTiedPairsMultiplied = (numPairs - tiedXPairs) * (double) (numPairs - tiedYPairs);
        
        
  System.err.println("Apache Commons{ \n" +
		  "NumPairs:\t" + numPairs + "\n" +
		  "swaps:\t" + swaps + "\n" +
		  "tiedXPairs:\t" + tiedXPairs + "\n" +
		  "tiedYPairs:\t" + tiedYPairs + "\n" +
		  "tiedXYPairs:\t" + tiedXYPairs + "\n" +
		  "C-D:\t" + concordantMinusDiscordant + "\n" +
		  "}");      
  
  return concordantMinusDiscordant / Math.sqrt(nonTiedPairsMultiplied);
    }

    /**
     * Returns the sum of the number from 1 .. n according to Gauss' summation formula:
     * \[ \sum\limits_{k=1}^n k = \frac{n(n + 1)}{2} \]
     *
     * @param n the summation end
     * @return the sum of the number from 1 to n
     */
    private static long sum(long n) {
        return n * (n + 1) / 2L;
    }
    
}//end class
