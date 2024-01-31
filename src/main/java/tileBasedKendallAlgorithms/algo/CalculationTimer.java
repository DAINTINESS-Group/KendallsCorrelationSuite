package tileBasedKendallAlgorithms.algo;

public class CalculationTimer {
    private static double compareWithSelfTime;
    private static double compareWithEastTime;
    private static double compareWithSouthTime;
    private static double processNonCrossTime;

    public CalculationTimer() {}

    public static void incrementCompareWithSelfTime(double time) {
        compareWithSelfTime += time;
    }

    public static void incrementCompareWithEastTime(double time) {
        compareWithEastTime += time;
    }

    public static void incrementCompareWithSouthTime(double time) {
        compareWithSouthTime += time;
    }

    public static void incrementCompareWithNonCrossTime(double time) {
        processNonCrossTime += time;
    }

    @Override
    public String toString() {
        return "CalculationTimer\n{" +
                "  \ncompareWithSelfTime = " + compareWithSelfTime + " seconds" +
                ", \ncompareWithEastTime = " + compareWithEastTime + " seconds" +
                ", \ncompareWithSouthTime = " + compareWithSouthTime + " seconds" +
                ", \nprocessNonCrossTime = " + processNonCrossTime + " seconds" +
                "\n}";
    }
}
