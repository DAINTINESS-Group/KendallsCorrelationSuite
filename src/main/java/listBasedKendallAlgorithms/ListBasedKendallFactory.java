package listBasedKendallAlgorithms;

public class ListBasedKendallFactory {

    public IListBasedKendallCalculator createKendallCalculatorByString(String method) {
        
        if ("Brophy".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.BROPHY);
        } else if ("BruteForce".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE);
        } else if ("Apache kendall".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.APACHE_KENDALL);
        } else if ("ListBasedTiles".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.TILES_LIST);
        }
        throw new IllegalArgumentException(
        		String.format("%s is not a supported calculation method.", method));
    }
    
    public IListBasedKendallCalculator createKendallCalculator(KendallCalculatorMethods method) {
        switch (method) {
            case BRUTEFORCE:
                return new BruteForceNoTiesKendallCalculator();
            case BROPHY:
                return new BrophyKendallCalculator();
            case APACHE_KENDALL:
                return new ApacheCommonsKendall();
            case TILES_LIST:
            	return new ListBasedTileBasedKendallManager();
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }
    }

    public enum KendallCalculatorMethods {BRUTEFORCE, BROPHY, APACHE_KENDALL, TILES_LIST}
}
