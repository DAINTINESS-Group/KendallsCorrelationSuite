package tiles.algos;

public class CorrelationStatistics {

    protected long concordantPairsCount;
    protected long discordantPairsCount;
    protected long tiedXPairsCount;
    protected long tiedYPairsCount;

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

    public void incrementTiedXCount(double amount) {
        tiedXPairsCount += amount;
    }

    public void incrementTiedYCount() {
        tiedYPairsCount++;
    }

    public void incrementTiedYCount(double amount) {
        tiedYPairsCount += amount;
    }

    public double calculateCorrelationResult() {
        double nominator = (double) (concordantPairsCount - discordantPairsCount);
        double nonTiedCount = (double) (concordantPairsCount + discordantPairsCount);
        double pairsProduct = (nonTiedCount + tiedXPairsCount) * (nonTiedCount + tiedYPairsCount);
        double denominator = Math.sqrt(pairsProduct);

        return nominator / denominator;
    }

    @Override
    public String toString() {
        return "CorrelationStatistics:\n{ " +
                "ConcordantPairsCount = " + concordantPairsCount +
                ",\nDiscordantPairsCount = " + discordantPairsCount +
                ",\nTiedXPairsCount = " + tiedXPairsCount +
                ",\nTiedYPairsCount = " + tiedYPairsCount +
                " }\n";
    }
}
