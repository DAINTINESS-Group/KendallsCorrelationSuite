package listBasedKendallAlgorithms;

public class ListBasedKendallFactory {

    public IListBasedKendallCalculator createKendallCalculator(KendallCalculatorMethods method) {
        switch (method) {
            case BRUTEFORCE:
                return new BruteForceNoTiesKendallCalculator();
            case BROPHY:
                return new BrophyKendallCalculator();
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }
    }

    public enum KendallCalculatorMethods {BRUTEFORCE, BROPHY, TILE_METHOD}
}
