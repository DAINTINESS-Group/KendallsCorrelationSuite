package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

public class ApacheCommonsKendall implements IListBasedKendallCalculator{
    public double calculateKendall(ColumnPair pair) {
        double[] arr1 = pair.getXColumn().stream().mapToDouble(Double::doubleValue).toArray();
        double[] arr2 = pair.getYColumn().stream().mapToDouble(Double::doubleValue).toArray();

        KendallsCorrelation kendallsCorrelation = new KendallsCorrelation();
        return kendallsCorrelation.correlation(arr1, arr2);
    }
}
