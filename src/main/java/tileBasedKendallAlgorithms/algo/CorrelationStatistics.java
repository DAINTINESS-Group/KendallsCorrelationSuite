package tileBasedKendallAlgorithms.algo;

public class CorrelationStatistics {

    private long concordantPairsCount;
    private long discordantPairsCount;
    private long tiedXPairsCount;
    private long tiedYPairsCount;

    public CorrelationStatistics() {
        concordantPairsCount = 0;
        discordantPairsCount = 0;
        tiedXPairsCount = 0;
        tiedYPairsCount = 0;
    }

    public void incrementConcordantCount() {
        concordantPairsCount++;
    }

    public void incrementDiscordantCount() {
        discordantPairsCount++;
    }

    public void incrementConcordantCount(long amount) {
        concordantPairsCount += amount;
    }

    public void incrementDiscordantCount(long amount) {
        discordantPairsCount += amount;
    }

    public void incrementTiedXCount() {
        tiedXPairsCount++;
    }

    public void incrementTiedYCount() {
        tiedYPairsCount++;
    }

    public double calculateCorrelationResult() {
        long nominator = concordantPairsCount - discordantPairsCount;
        long NonTiedCount = concordantPairsCount + discordantPairsCount;
        double denominator = Math.sqrt((NonTiedCount + tiedXPairsCount) * (NonTiedCount + tiedYPairsCount));

        return nominator / denominator;
    }

    @Override
    public String toString() {
        return "CorrelationStatistics{ " +
                "concordantPairsCount=" + concordantPairsCount +
                ", discordantPairsCount=" + discordantPairsCount +
                ", tiedXPairsCount=" + tiedXPairsCount +
                ", tiedYPairsCount=" + tiedYPairsCount +
                ", Correlation Result = " + calculateCorrelationResult() +
                '}';
    }
}
