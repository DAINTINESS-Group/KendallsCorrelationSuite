package util.tilemgr;

import listBasedKendallAlgorithms.listBasedReader.ColumnPair;
import util.tiles.ITile;
import util.tiles.ITileFactory;
import util.tiles.ITileType;

public class TilesManagerListReaderTilesInMemSimple extends TilesManagerListBasedAbstractClass {
	
	public TilesManagerListReaderTilesInMemSimple(ColumnPair pair) {
		super(pair);
        this.tileType = ITileType.SIMPLE;
        this.tileFactory = new ITileFactory();

	}
	
	@Override
	protected void initializeTilesArray() {
	    tiles = new ITile[this.numOfBinsY][this.numOfBinsX];    	
	    for (int row = 0; row < numOfBinsY; row++) {
	        for (int col = 0; col < numOfBinsX; col++) {
	            tiles[row][col] = tileFactory.createTile(this.tileType, row, col); 
	            		//new TileInMemSimple(row, col);
	        }
	    }
	}
}
