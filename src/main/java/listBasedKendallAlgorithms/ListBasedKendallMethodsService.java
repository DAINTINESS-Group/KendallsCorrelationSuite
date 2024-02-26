package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.ListBasedKendallFactory.KendallCalculatorMethods;

public class ListBasedKendallMethodsService {
    private final ListBasedKendallFactory factory;

    public ListBasedKendallMethodsService() {
        this.factory = new ListBasedKendallFactory();
    }

    public IListBasedKendallCalculator getMethod(String method) {
        if ("Brophy".equals(method)) {
            return factory.createKendallCalculator(KendallCalculatorMethods.BROPHY);
        } else if ("BruteForce".equals(method)) {
            return factory.createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE);
        } else if ("Apache kendall".equals(method)) {
            return factory.createKendallCalculator(KendallCalculatorMethods.APACHE_KENDALL);
        }
        throw new IllegalArgumentException("Unknown method: " + method);
    }
}
