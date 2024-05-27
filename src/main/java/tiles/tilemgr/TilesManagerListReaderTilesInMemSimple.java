package tiles.tilemgr;

import common.ColumnPair;
import tiles.dom.ITile;
import tiles.dom.ITileFactory;
import tiles.dom.ITileType;
import util.TileConstructionParameters;

public class TilesManagerListReaderTilesInMemSimple extends TilesManagerListBasedAbstractClass {
	private static final long serialVersionUID = -6083682790111358108L;

	public TilesManagerListReaderTilesInMemSimple(ColumnPair pair, TileConstructionParameters parameters) {
		super(pair, parameters);
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
