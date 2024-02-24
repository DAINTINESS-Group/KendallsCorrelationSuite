package listBasedKendallAlgorithms;

import listBasedKendallAlgorithms.ListBasedKendallFactory.KendallCalculatorMethods;

import java.util.HashMap;

public class ListBasedKendallMethodsService {
    private final HashMap<String, IListBasedKendallCalculator> methods;
    ListBasedKendallFactory factory = new ListBasedKendallFactory();

    public ListBasedKendallMethodsService() {
        methods = new HashMap<>();
        methods.put("Brophy", factory.createKendallCalculator(KendallCalculatorMethods.BROPHY));
        methods.put("BruteForce", factory.createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE));
        methods.put("Apache kendall", factory.createKendallCalculator(KendallCalculatorMethods.APACHE_KENDALL));
    }

    public IListBasedKendallCalculator getMethod(String method) {
        return methods.get(method);
    }
}
