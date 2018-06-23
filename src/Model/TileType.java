package Model;

public enum TileType {
	SEA(188, 86),
	BEACH(1, 460),
	BEACH_RCU(69, 477),
	BEACH_LCU(52, 477),
	BEACH_RCL(35, 477),
	BEACH_LCL(18, 477),
	BEACH_U(52, 460),
	BEACH_R(35, 460),
	BEACH_L(18, 460),
	BEACH_D(69, 460),
	BEACH_SPL(324, 341),
	BEACH_SPR(341, 341),
	
	MOUNTAIN(358, 273),
	MOUNTAIN_RCU(426, 681),
	MOUNTAIN_LCU(375, 681),
	MOUNTAIN_RCL(426, 715),
	MOUNTAIN_LCL(375, 715),
	MOUNTAIN_U(409, 681),
	MOUNTAIN_R(426, 698),
	MOUNTAIN_L(375, 698),
	MOUNTAIN_D(341, 307),
	MOUNTAIN_SPL(324, 341),
	MOUNTAIN_SPR(341, 341);
	
	private final int x;
	private final int y;

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
