package listBasedKendallAlgorithms;

public class IListBasedKendallFactory {
    public enum KendallCalculatorMethods {BRUTEFORCE, BROPHY, APACHE_KENDALL, SIMPLE_TILES_LIST, BANDS_WITH_MEMORY, MERGESORT}
    
    public IListBasedKendallCalculator createKendallCalculatorByString(String method) {
        
        if ("Brophy".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.BROPHY);
        } else if ("BruteForce".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE);
        } else if ("Apache kendall".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.APACHE_KENDALL);
        } else if ("ListBasedTiles".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.SIMPLE_TILES_LIST);
        }else if ("BandsWithMemory".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.BANDS_WITH_MEMORY);
        }else if ("MergeSort".equals(method)) {
            return createKendallCalculator(KendallCalculatorMethods.MERGESORT);
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
            case SIMPLE_TILES_LIST:
            	return new TilesWithSimplePointChecksListReaderKendallCalculator();
            case BANDS_WITH_MEMORY:
            	return new TileBandsWithMemoryKendallCalculator();	
            case MERGESORT:
            	return new TilesMergeSortListReaderKendallCalculator();
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }
    }


}
