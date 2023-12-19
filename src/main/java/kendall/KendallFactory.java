package kendall;

public class KendallFactory {

	public enum KendallCalculatorMethods {BRUTEFORCE, BROPHY, TILE_METHOD};
	
    public IKendallCalculator createKendallCalculator(KendallCalculatorMethods method) {
        switch(method) {
            case BRUTEFORCE:
                return new BruteForceCalculator();
            case BROPHY:
                return new BrophyCalculator();
//            case TILE_METHOD:
//                return (IKendallCalculator) new TileMethodKendallCalculator();
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }
    }
}
