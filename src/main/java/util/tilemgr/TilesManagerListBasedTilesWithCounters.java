package util.tilemgr;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import util.tiles.ITile;
import util.tiles.ITileFactory;
import util.tiles.ITileType;

public class TilesManagerListBasedTilesWithCounters extends TilesManagerListBased {
	
	public TilesManagerListBasedTilesWithCounters(ColumnPair pair) {
		super(pair);
        this.tileType = ITileType.WITH_COUNTER_MAPS;
        this.tileFactory = new ITileFactory();

	}
	
	@Override
	protected void initializeTilesArray() {
	    tiles = new ITile[this.numOfBinsY][this.numOfBinsX];    	
	    for (int row = 0; row < numOfBinsY; row++) {
	        for (int col = 0; col < numOfBinsX; col++) {
	            tiles[row][col] = tileFactory.createTile(this.tileType, row, col); 
	        }
	    }
	}
}
