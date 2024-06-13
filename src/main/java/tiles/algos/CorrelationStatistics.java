package tiles.algos;

public class CorrelationStatistics {

    protected long concordantPairsCount;
    protected long discordantPairsCount;
    protected long tiedXPairsCount;
    protected long tiedYPairsCount;
    protected long tiedXYPairsCount;
    
    public CorrelationStatistics() {
        concordantPairsCount = 0;
        discordantPairsCount = 0;
        tiedXPairsCount = 0;
        tiedYPairsCount = 0;
        tiedXYPairsCount = 0;
    }

    public void incrementConcordantCount() {
        concordantPairsCount += 1L;
    }

    public void incrementDiscordantCount() {
        discordantPairsCount += 1L;
    }

    public void incrementConcordantCount(long amount) {
        concordantPairsCount += amount;
    }

    public void incrementDiscordantCount(long amount) {
        discordantPairsCount += amount;
    }

    public void incrementTiedXCount() {
        tiedXPairsCount += 1L;
    }

    public void incrementTiedXCount(long amount) {
        tiedXPairsCount += amount;
    }

    public void incrementTiedYCount() {
        tiedYPairsCount += 1L;
    }

    public void incrementTiedYCount(long amount) {
        tiedYPairsCount += amount;
    }

    public void incrementTiedXYCount() {
        tiedXYPairsCount += 1L;
    }

    public void incrementTiedXYCount(long amount) {
        tiedXYPairsCount += amount;
    }
    
    
    public double calculateCorrelationResult() {
        double nominator = (double) (concordantPairsCount - discordantPairsCount);
        double nonTiedCount = (double) (concordantPairsCount + discordantPairsCount);
        double pairsProduct = (nonTiedCount + tiedXPairsCount) * (nonTiedCount + tiedYPairsCount);
        double denominator = Math.sqrt(pairsProduct);
System.err.println("CorrelationStatistics.calculaterCorrelationResult() -> " + this.toString());
        return nominator / denominator;
    }

    @Override
    public String toString() {
        return "CorrelationStatistics:\n{ " + 
                "ConcordantPairsCount: \t" + concordantPairsCount + 
                "\nDiscordantPairsCount: \t" + discordantPairsCount + 
                "\nTiedXPairsCount: \t" + tiedXPairsCount + 
                "\nTiedYPairsCount: \t" + tiedYPairsCount + 
                "\nTiedXYPairsCount: \t" + tiedXYPairsCount + 
                "\n}\n";
    }
}
