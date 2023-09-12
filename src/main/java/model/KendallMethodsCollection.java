package model;

import java.util.HashMap;

public class KendallMethodsCollection {
    private final HashMap<String, ICalculator> methods;
    KendallFactory factory = new KendallFactory();

    public KendallMethodsCollection() {
        methods = new HashMap<>();
        methods.put("Brophy", factory.createKendallCalculator("Brophy"));
        methods.put("BruteForce", factory.createKendallCalculator("BruteForce"));
        //Further methods can be added here
    }

    public ICalculator getMethod(String method) {
        return methods.get(method);
    }
}
