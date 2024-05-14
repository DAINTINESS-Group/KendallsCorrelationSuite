package util;

import java.io.Serializable;

/** A class that regulates the construction of ITileManager
 * 
 * The main idea is that we use Joshua Bloch's static builder to construct the parameter object.
 * Usage example:
 * public static void main(String[] args) {
 * TileConstructionParameters params = new TileConstructionParameters.Builder(true)
 *      .rangeMakingMode(RangeMakingMode.FIXED)
 *      .numBinsX(20)
 *      .numBinsY(15)
 *      .build();
 *
 * System.out.println(params);
 * }
 * 
 * @author pvassil
 *
 */
public class TileConstructionParameters implements Serializable{
	private static final long serialVersionUID = -6058775436185252082L;

	public enum RangeMakingMode {
        FIXED, SCOTT_RULE
    }

    private boolean debugModeOn; 
    private boolean experimentModeOn;
    private RangeMakingMode rangeMakingMode;
    private int numBinsX;
    private int numBinsY;

    public static class Builder {

        private boolean debugModeOn = false;
        private boolean experimentModeOn = false;
        private RangeMakingMode rangeMakingMode = RangeMakingMode.SCOTT_RULE;
        private int numBinsX = -1;
        private int numBinsY = -1;

        public Builder(boolean experimentModeOn) {
            this.experimentModeOn = experimentModeOn;
        }

        public Builder debugModeOn(boolean debugModeOn) {
        	this.debugModeOn = debugModeOn;
            return this;
        }
        
        public Builder rangeMakingMode(RangeMakingMode val) {
            rangeMakingMode = val;
            return this;
        }

        public Builder numBinsX(int val) {
            numBinsX = val;
            return this;
        }

        public Builder numBinsY(int val) {
            numBinsY = val;
            return this;
        }
        
        private void validate() {
            if (experimentModeOn) {
            	 debugModeOn = false;
            }
            
            if (rangeMakingMode == RangeMakingMode.SCOTT_RULE )  {
            	numBinsX = -1; 
            	numBinsY = -1;
            }
        }

        public TileConstructionParameters build() {
            TileConstructionParameters parameters = new TileConstructionParameters(this);
            validate();
            return parameters;
        }
    }

    private TileConstructionParameters(Builder builder) {
        debugModeOn = builder.debugModeOn;
        experimentModeOn = builder.experimentModeOn;
        rangeMakingMode = builder.rangeMakingMode;
        numBinsX = builder.numBinsX;
        numBinsY = builder.numBinsY;
    }




	public boolean isDebugModeOn() {
		return debugModeOn;
	}


	public boolean isExperimentModeOn() {
		return experimentModeOn;
	}


	public RangeMakingMode getRangeMakingMode() {
		return rangeMakingMode;
	}


	public int getNumBinsX() {
		return numBinsX;
	}


	public int getNumBinsY() {
		return numBinsY;
	}
	
    @Override
    public String toString() {
        return "TileConstructionParameters{" +
                "debugModeOn=" + debugModeOn +
                ", experimentModeOn=" + experimentModeOn +
                ", rangeMakingMode=" + rangeMakingMode +
                ", numBinsX=" + numBinsX +
                ", numBinsY=" + numBinsY +
                '}';
    }

	
}//end class
