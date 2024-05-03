package util.tiles;

public class ITileFactory {
	public ITile createTile(ITileType type, int row, int column) {
		switch(type) {
			case SIMPLE: 
				return new TileSimple(row, column);
			case WITH_COUNTER_MAPS:
				 return new TileWithCounters(row, column);
			case STORED_SIMPLE: 
				return new TileStored(row, column);
			default: return new TileSimple(row, column);
		}
	}
}
