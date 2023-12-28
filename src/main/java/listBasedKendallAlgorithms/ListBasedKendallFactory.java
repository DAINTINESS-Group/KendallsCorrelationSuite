package listBasedKendallAlgorithms;

public class ListBasedKendallFactory {

	public enum KendallCalculatorMethods {BRUTEFORCE, BROPHY, TILE_METHOD};
	
    public IListBasedKendallCalculator createKendallCalculator(KendallCalculatorMethods method) {
        switch(method) {
            case BRUTEFORCE:
                return new BruteForceCalculator();
            case BROPHY:
                return new BrophyCalculator();
//            case TILE_METHOD:
//                return (IListBasedKendallCalculator) new TileMethodKendallCalculator();
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }
    }
}
