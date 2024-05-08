package tiles.algos;

public class CorrelationStatisticsMergeSort extends CorrelationStatistics{
	protected int numTuples;
	protected final long numPairs;
	
	public CorrelationStatisticsMergeSort(int numTuples){
		this.numTuples = numTuples;
		this.numPairs = numOfAllPairs(numTuples);
	}
    protected long numOfAllPairs(long n) {
        return n * (n - 1) / 2L;
    }
    
    public long getAllPairs() {
    	return this.numPairs;
    }
    public double calculateCorrelationResult() {
        double nominator = (double) (concordantPairsCount - discordantPairsCount);
        double pairsProduct = (double) (this.numPairs- tiedXPairsCount) * (this.numPairs - tiedYPairsCount);
        double denominator = Math.sqrt(pairsProduct);

        return nominator / denominator;
    }

    @Override
    public String toString() {
        return "CorrelationStatisticsMergeSort:\n{ " +
        		"Total #pairs = " + this.numPairs + "\n" +
                "Concordant - Discordant PairsCount = " + (concordantPairsCount - discordantPairsCount) +
                ",\nTiedXPairsCount = " + tiedXPairsCount +
                ",\nTiedYPairsCount = " + tiedYPairsCount +
                " }\n";
    }

    
}//end class
