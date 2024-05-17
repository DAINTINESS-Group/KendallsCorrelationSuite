package listBasedKendallAlgorithms;

import util.TileConstructionParameters;

public class IListBasedKendallFactory {
    public enum KendallCalculatorMethods {BRUTEFORCE, BROPHY, APACHE, SIMPLE_TILES_LIST, BANDS_WITH_MEMORY, MERGESORT}
    
//    public IListBasedKendallCalculator createKendallCalculatorByString(String method, TileConstructionParameters parameters) {
//        
//        if ("Brophy".equals(method)) {
//            return createKendallCalculator(KendallCalculatorMethods.BROPHY, parameters);
//        } else if ("BruteForce".equals(method)) {
//            return createKendallCalculator(KendallCalculatorMethods.BRUTEFORCE, parameters);
//        } else if ("Apache kendall".equals(method)) {
//            return createKendallCalculator(KendallCalculatorMethods.APACHE, parameters);
//        } else if ("ListBasedTiles".equals(method)) {
//            return createKendallCalculator(KendallCalculatorMethods.SIMPLE_TILES_LIST, parameters);
//        }else if ("BandsWithMemory".equals(method)) {
//            return createKendallCalculator(KendallCalculatorMethods.BANDS_WITH_MEMORY, parameters);
//        }else if ("MergeSort".equals(method)) {
//            return createKendallCalculator(KendallCalculatorMethods.MERGESORT, parameters);
//        }
//        throw new IllegalArgumentException(
//        		String.format("%s is not a supported calculation method.", method));
//    }
    
    public IListBasedKendallCalculator createKendallCalculator(KendallCalculatorMethods method, TileConstructionParameters parameters) {
        switch (method) {
            case BRUTEFORCE:
                return new BruteForceNoTiesKendallCalculator();
            case BROPHY:
                return new BrophyKendallCalculator();
            case APACHE:
                return new ApacheCommonsKendall();
            case SIMPLE_TILES_LIST:
            	return new TilesWithSimplePointChecksListReaderKendallCalculator();
            case BANDS_WITH_MEMORY:
            	return new TileBandsWithMemoryKendallCalculator(parameters);	
            case MERGESORT:
            	return new TilesMergeSortListReaderKendallCalculator();
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }
    }


}
