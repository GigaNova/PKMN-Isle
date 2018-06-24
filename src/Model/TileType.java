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
	MOUNTAIN_SPR(341, 341),
	
	MOUNTAIN_LV2(358, 273),
	MOUNTAIN_LV2_RCU(358, 681),
	MOUNTAIN_LV2_LCU(307, 681),
	MOUNTAIN_LV2_RCL(358, 715),
	MOUNTAIN_LV2_LCL(307, 715),
	MOUNTAIN_LV2_U(341, 681),
	MOUNTAIN_LV2_R(358, 698),
	MOUNTAIN_LV2_L(307, 698),
	MOUNTAIN_LV2_D(341, 307),
	
	ROCK_UL(171, 239),
	ROCK_UR(188, 239),
	ROCK_LL(171, 256),
	ROCK_LR(188, 256), 
	
	ROCK_2_UL(171, 273),
	ROCK_2_UR(188, 273),
	ROCK_2_LL(171, 290),
	ROCK_2_LR(188, 290), 
	
	BEACH_ROCK(137, 18),
	
	GRASS_1(103, 1),
	GRASS_2(103, 18),
	GRASS_3(103, 35),
	GRASS_4(120, 18),
	GRASS_UP(341, 324),
	FLOWER(120, 35);
	
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
	
	public static boolean isBeach(TileType tileType) {
		if(tileType == null) return false;
		switch(tileType) {
		case BEACH:
		case BEACH_D:
		case BEACH_L:
		case BEACH_U:
		case BEACH_LCL:
		case BEACH_LCU:
		case BEACH_R:
		case BEACH_RCL:
		case BEACH_RCU:
		case BEACH_SPL:
		case BEACH_SPR:
			return true;
		default:
			return false;
		}
	}

	public static boolean isMountain(TileType tileType) {
		if(tileType == null) return false;
		switch(tileType) {
		case MOUNTAIN:
		case MOUNTAIN_D:
		case MOUNTAIN_L:
		case MOUNTAIN_U:
		case MOUNTAIN_LCL:
		case MOUNTAIN_LCU:
		case MOUNTAIN_R:
		case MOUNTAIN_RCL:
		case MOUNTAIN_RCU:
		case MOUNTAIN_SPL:
		case MOUNTAIN_SPR:
			return true;
		default:
			return false;
		}
	}

	public static boolean isGrass(TileType tileType) {
		if(tileType == null) return false;
		switch(tileType) {
		case GRASS_1:
		case GRASS_2:
		case GRASS_3:
		case GRASS_4:
			return true;
		default:
			return false;
		}
	}
}
