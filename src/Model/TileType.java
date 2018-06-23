package Model;

public enum TileType {
	SEA(188, 86),
	BEACH(1, 460),
	MOUNTAIN(358, 273);
	
	private final int x; // in kilograms
	private final int y; // in meters

	TileType(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
