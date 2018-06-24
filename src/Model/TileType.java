package Model;

public enum TileType {
	SEA(188, 86, "2b", "11"),
	
	BEACH(1, 460, "15", "31"),
	BEACH_RCU(69, 477, "0e", "31"),
	BEACH_LCU(52, 477, "0c", "31"),
	BEACH_RCL(35, 477, "1e", "31"),
	BEACH_LCL(18, 477, "1c", "31"),
	BEACH_U(52, 460, "0d", "31"),
	BEACH_R(35, 460, "16", "31"),
	BEACH_L(18, 460, "14", "31"),
	BEACH_D(69, 460, "1d", "31"),
	
	MOUNTAIN(358, 273, "71", "04"),
	MOUNTAIN_RCU(426, 681, "9a", "04"),
	MOUNTAIN_LCU(375, 681, "98", "04"),
	MOUNTAIN_RCL(426, 715, "aa", "04"),
	MOUNTAIN_LCL(375, 715, "a8", "04"),
	MOUNTAIN_U(409, 681, "99", "04"),
	MOUNTAIN_R(426, 698, "a2", "04"),
	MOUNTAIN_L(375, 698, "a0", "04"),
	MOUNTAIN_D(341, 307, "79", "04"),
	MOUNTAIN_SPL(324, 341, "b3", "04"),
	MOUNTAIN_SPR(341, 341, "b2", "04"),
	
	MOUNTAIN_LV2(358, 273, "71", "04"),
	MOUNTAIN_LV2_RCU(358, 681, "6d", "04"),
	MOUNTAIN_LV2_LCU(307, 681, "6b", "04"),
	MOUNTAIN_LV2_RCL(358, 715, "7d", "04"),
	MOUNTAIN_LV2_LCL(307, 715, "7b", "04"),
	MOUNTAIN_LV2_U(341, 681, "6c", "04"),
	MOUNTAIN_LV2_R(358, 698, "75", "04"),
	MOUNTAIN_LV2_L(307, 698, "73", "04"),
	MOUNTAIN_LV2_D(341, 307, "7c", "04"),
	
	ROCK_UL(171, 239, "10", "05"),
	ROCK_UR(188, 239, "11", "05"),
	ROCK_LL(171, 256, "18", "05"),
	ROCK_LR(188, 256, "19", "05"), 
	
	ROCK_2_UL(171, 273, "CB", "05"),
	ROCK_2_UR(188, 273, "CC", "05"),
	ROCK_2_LL(171, 290, "D3", "05"),
	ROCK_2_LR(188, 290, "D4", "05"), 
	
	BEACH_ROCK(137, 18, "AF", "04"),
	
	GRASS_1(103, 1, "01", "04"),
	GRASS_2(103, 18, "08", "04"),
	GRASS_3(103, 35, "09", "04"),
	GRASS_4(120, 18, "10", "04"),
	GRASS_UP(341, 324, "33", "05"),
	FLOWER(120, 35, "04", "04");
	
	private final int x;
	private final int y;
	private final String tileByte;
	private final String perByte;

	private TileType(int x, int y, String hexOne, String hexTwo) {
		this.x = x;
		this.y = y;
		this.tileByte = hexOne;
		this.perByte = hexTwo;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public String getTileByte() {
		return tileByte;
	}

	public String getPerByte() {
		return perByte;
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
