package tiles.dom;

import java.io.Serializable;

public class ITileFactory implements Serializable{
	private static final long serialVersionUID = -637091881044696511L;

	public ITile createTile(ITileType type, int row, int column) {
		switch(type) {
			case SIMPLE: 
				return new TileInMemSimple(row, column);
			case WITH_COUNTER_MAPS:
				 return new TileInMemWCounters(row, column);
			case STORED_SIMPLE: 
				return new TileStoredSimple(row, column);
			default: return new TileInMemSimple(row, column);
		}
	}
}
