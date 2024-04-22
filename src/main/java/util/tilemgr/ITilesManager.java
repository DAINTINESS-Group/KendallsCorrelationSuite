package util.tilemgr;

import util.tiles.Tile;

public interface ITilesManager {

	Tile[][] createTilesArray();

	int calculateRangesCount(double rangeWidth, double min, double max);

}