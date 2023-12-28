package listBasedKendallAlgorithms;

import java.util.HashMap;

import listBasedKendallAlgorithms.ListBasedKendallFactory.KendallCalculatorMethods;

/**
 * Το σκέτο factory γιατί δε μας κάνει? οέο?
 * Χρειάζεται κάτι αυτή η κλάση?
 * 
 * For the moment, I renamed it as "service" just in case we use it as service.
 * But it is not obvious to me what it does...
 * 
 * @author pvassil
 *
 */

public class ListBasedKendallMethodsService {
    private final HashMap<String, IListBasedKendallCalculator> methods;
    ListBasedKendallFactory factory = new ListBasedKendallFactory();

    public ListBasedKendallMethodsService() {
        methods = new HashMap<>();
        methods.put("Brophy", factory.createKendallCalculator(KendallCalculatorMethods.BROPHY));
        methods.put("BruteForce", factory.createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE));
        //methods.put("Tile_Method", factory.createKendallCalculator(KendallCalculatorMethods.TILE_METHOD));
        //Further methods can be added here
    }

    public IListBasedKendallCalculator getMethod(String method) {
        return methods.get(method);
    }
}
