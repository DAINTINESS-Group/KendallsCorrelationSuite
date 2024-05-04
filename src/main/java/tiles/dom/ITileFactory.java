package tiles.dom;

public class ITileFactory {
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
