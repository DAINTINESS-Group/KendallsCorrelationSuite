package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.ListBasedKendallFactory.KendallCalculatorMethods;

public class ListBasedKendallMethodsService {

    public ListBasedKendallMethodsService() {
    }

    public IListBasedKendallCalculator getMethod(String method) {
        ListBasedKendallFactory factory = new ListBasedKendallFactory();
        if ("Brophy".equals(method)) {
            return factory.createKendallCalculator(KendallCalculatorMethods.BROPHY);
        } else if ("BruteForce".equals(method)) {
            return factory.createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE);
        } else if ("Apache kendall".equals(method)) {
            return factory.createKendallCalculator(KendallCalculatorMethods.APACHE_KENDALL);
        }
        else if ("ListBasedTiles".equals(method)) {
            return factory.createKendallCalculator(KendallCalculatorMethods.TILES_LIST);
        }
        throw new IllegalArgumentException("Unknown method: " + method);
    }
}
