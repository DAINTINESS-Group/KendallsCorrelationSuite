package listBasedKendallAlgorithms;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

import java.util.ArrayList;

public class ApacheCommonsKendall {
    public double calculateKendallTau(ArrayList<Double> list1, ArrayList<Double> list2) {
        double[] arr1 = list1.stream().mapToDouble(Double::doubleValue).toArray();
        double[] arr2 = list2.stream().mapToDouble(Double::doubleValue).toArray();

        KendallsCorrelation kendallsCorrelation = new KendallsCorrelation();
        return kendallsCorrelation.correlation(arr1, arr2);
    }
}
