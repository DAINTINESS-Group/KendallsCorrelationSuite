package tileBasedKendallAlgorithms.algo;

public class BinCalculatorFactory {

    public enum BinCalculatorMethods {SQUARE_ROOT};
    public BinCalculatorFactory(){};

    public SquareRootBinCalculator createBinCalculator(BinCalculatorMethods method) {
        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }

        switch (method) {
            case SQUARE_ROOT:
                return new SquareRootBinCalculator();
            // More methods can be added here
            default:
                throw new IllegalArgumentException("Unsupported bin calculator method: " + method);
        }
    }
}
