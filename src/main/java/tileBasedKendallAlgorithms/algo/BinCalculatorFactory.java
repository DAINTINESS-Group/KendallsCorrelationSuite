package tileBasedKendallAlgorithms.algo;

/**
 * The BinCalculatorFactory class provides a method to create instances of various
 * bin calculator implementations based on the specified method.
 */
public class BinCalculatorFactory {

    public enum BinCalculatorMethods {SQUARE_ROOT};
    public BinCalculatorFactory(){};

    /**
     * Creates and returns an instance of a bin calculator based on the specified method.
     * This method facilitates the creation of bin calculator instances without exposing
     * the instantiation logic to the client.
     *
     * @param method The bin calculator method to be used for creating the bin calculator instance.
     *               Must be one of the values defined in the BinCalculatorMethods enumeration.
     * @return An instance of a bin calculator corresponding to the specified method.
     * @throws IllegalArgumentException if the specified method is null or unsupported.
     */
    public SquareRootBinCalculator createBinCalculator(BinCalculatorMethods method) {
        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }

        switch (method) {
            case SQUARE_ROOT:
                return new SquareRootBinCalculator();
            default:
                throw new IllegalArgumentException("Unsupported bin calculator method: " + method);
        }
    }
}
