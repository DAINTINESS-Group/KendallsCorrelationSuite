package tiles.tilemgr.rangemaker;

import java.io.Serializable;

public class RangeMakerResult implements Serializable{
	private static final long serialVersionUID = 8875557647891915627L;
	int numberOfBinsX;
	double rangeWidthX;
	int numberOfBinsY;
	double rangeWidthY;
	
	public void setNumberOfBinsX(int numberOfBinsX) {
		this.numberOfBinsX = numberOfBinsX;
	}
	public void setRangeWidthX(double rangeWidthX) {
		this.rangeWidthX = rangeWidthX;
	}
	public void setNumberOfBinsY(int numberOfBinsY) {
		this.numberOfBinsY = numberOfBinsY;
	}
	public void setRangeWidthY(double rangeWidthY) {
		this.rangeWidthY = rangeWidthY;
	}
	
	public int getNumberOfBinsX() {
		return numberOfBinsX;
	}
	public double getRangeWidthX() {
		return rangeWidthX;
	}
	public int getNumberOfBinsY() {
		return numberOfBinsY;
	}
	public double getRangeWidthY() {
		return rangeWidthY;
	}
	
	@Override
	public String toString() {
		return "RangeMakerResult [numberOfBinsX=" + numberOfBinsX + ", rangeWidthX=" + rangeWidthX + ", numberOfBinsY="
				+ numberOfBinsY + ", rangeWidthY=" + rangeWidthY + "]";
	}


}
