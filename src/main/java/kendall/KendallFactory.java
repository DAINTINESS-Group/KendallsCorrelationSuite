package kendall;

public class KendallFactory {

	public enum KendallCalculatorMethods {BRUTEFORCE, BROPHY};
	
    public IKendallCalculator createKendallCalculator(KendallCalculatorMethods method) {
        switch(method) {
            case BRUTEFORCE:
                return new BruteForceCalculator();
            case BROPHY:
                return new BrophyCalculator();
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }
    }
}
