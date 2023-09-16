package kendall;

import java.util.HashMap;

import kendall.KendallFactory.KendallCalculatorMethods;

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

public class KendallMethodsService {
    private final HashMap<String, IKendallCalculator> methods;
    KendallFactory factory = new KendallFactory();

    public KendallMethodsService() {
        methods = new HashMap<>();
        methods.put("Brophy", factory.createKendallCalculator(KendallCalculatorMethods.BROPHY));
        methods.put("BruteForce", factory.createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE));
        //Further methods can be added here
    }

    public IKendallCalculator getMethod(String method) {
        return methods.get(method);
    }
}
