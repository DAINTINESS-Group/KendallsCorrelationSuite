package tiles.algos;

import java.util.Locale;

public class CalculationTimer {
    private static double compareWithSelfTime  = 0.0;
    private static double compareWithEastTime  = 0.0;
    private static double compareWithSouthTime  = 0.0;
    private static double processNonCrossTime  = 0.0;

    public CalculationTimer() {
    }

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

    public static void reset() {
        compareWithSelfTime  = 0.0;
        compareWithEastTime  = 0.0;
        compareWithSouthTime = 0.0;
        processNonCrossTime  = 0.0;
    	
    }
    @Override
    public String toString() {
        return "CalculationTimer:\n{" +
                "Compare With Self Time = " + String.format(Locale.US, "%.3f", compareWithSelfTime) + " seconds" +
                ",\nCompare With East Time = " + String.format(Locale.US, "%.3f", compareWithEastTime) + " seconds" +
                ",\nCompare With South Time = " + String.format(Locale.US, "%.3f", compareWithSouthTime) + " seconds" +
                ",\nProcess Non Cross Time = " + String.format(Locale.US, "%.3f", processNonCrossTime) + " seconds }" +
                ",\nTotal Processing took: " + String.format(Locale.US, "%.3f", (compareWithSelfTime + compareWithEastTime + compareWithSouthTime + processNonCrossTime));
    }

}
