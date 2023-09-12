package model;

public class KendallFactory {

    public ICalculator createKendallCalculator(String method) {
        switch(method) {
            case "BruteForce":
                return new BruteForceCalculator();
            case "Brophy":
                return new BrophyCalculator();
            default:
                throw new IllegalArgumentException(
                        String.format("%s is not a supported calculation method.", method));
        }

    }
}
