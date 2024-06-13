package listBasedKendallAlgorithms;

import util.TileConstructionParameters;

public class IListBasedKendallFactory {
    public enum KendallCalculatorMethods {BRUTEFORCE, BROPHY, APACHE, SIMPLE_TILES_LIST, BANDS_WITH_MEMORY, MERGESORT, SIMPLE_SORTERS}
       
    public IListBasedKendallCalculator createKendallCalculator(KendallCalculatorMethods method, TileConstructionParameters parameters) {
        switch (method) {
            case BRUTEFORCE:
                return new BruteForceNoTiesKendallCalculator();
            case BROPHY:
                return new BrophyKendallCalculator();
            case APACHE:
                return new ApacheCommonsKendall();
            case SIMPLE_TILES_LIST:
            	return new TilesWithSimplePointChecksListReaderKendallCalculator(parameters);
            case BANDS_WITH_MEMORY:
            	return new TileBandsWithMemoryKendallCalculator(parameters);	
            case MERGESORT:
            	return new TilesMergeSortListReaderKendallCalculator(parameters);
            case SIMPLE_SORTERS:
            	return new TilesWithSimpleSortersListReaderKendallCalculator(parameters);            	
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }
    }


}
