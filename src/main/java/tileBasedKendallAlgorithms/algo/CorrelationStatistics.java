package tileBasedKendallAlgorithms.algo;

public class CorrelationStatistics {

    private double concordantPairsCount;
    private double discordantPairsCount;
    private double tiedXPairsCount;
    private double tiedYPairsCount;

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

    public void incrementConcordantCount(double amount) {
        concordantPairsCount += amount;
    }

    public void incrementDiscordantCount(double amount) {
        discordantPairsCount += amount;
    }

    public void incrementTiedXCount() {
        tiedXPairsCount++;
    }

    public void incrementTiedYCount() {
        tiedYPairsCount++;
    }

    public double calculateCorrelationResult() {
        double nominator = concordantPairsCount - discordantPairsCount;
        double nonTiedCount = concordantPairsCount + discordantPairsCount;
        double pairsProduct = (nonTiedCount + tiedXPairsCount) * (nonTiedCount + tiedYPairsCount);
        double denominator = Math.sqrt(pairsProduct);

        return nominator / denominator;
    }

    @Override
    public String toString() {
        return "CorrelationStatistics:\n{ " +
                "  \nconcordantPairsCount = " + concordantPairsCount +
                ", \ndiscordantPairsCount = " + discordantPairsCount +
                ", \ntiedXPairsCount = " + tiedXPairsCount +
                ", \ntiedYPairsCount = " + tiedYPairsCount +
                "\n}\n";
    }
}
