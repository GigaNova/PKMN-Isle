package Model;

import java.util.Observable;
import java.util.Random;

public class Map extends Observable {
	public final static int MAX_MAP_SIZE = 64;
	public final static int MIN_MAP_SIZE = 16;
	public final static int TILE_SIZE = 16;

	public static final int MIN_OCTAVE = 1;
	public static final int MAX_OCTAVE = 10;
	
	private final static int MAX_ROCKS = 24;
	private final static int MAX_BEACH_ROCKS = 24;
	private final static int MAX_FLOWERS = 48;
	
	private final static double SEA_LINE = 0.5;
	private final static double BEACH_LINE = 0.675;
	private final static double MOUNTAIN_SECOND_LINE = 0.82;
	
	private final static int CLEANUP_COUNT = 40;
	
	private int height;
	private int width;
	private int octaves;
	private float roughness;
	private float scale;
	
	private boolean grassEnabled;
	private boolean flowersEnabled;
	private boolean secondMountainEnabled;
	private boolean beachRocksEnabled;
	private boolean showPermissions;
	private boolean surfAble;
	
	private TileType[][] tiles;
	private Permission[][] permissions;
	
	public Map() {
		this.height = (MAX_MAP_SIZE / 2) + (MIN_MAP_SIZE / 2);
		this.width = (MAX_MAP_SIZE / 2) + (MIN_MAP_SIZE / 2);
		this.octaves = 4;
		this.roughness = 0.66f;
		this.scale = 0.012f;
		this.grassEnabled = false;
		this.secondMountainEnabled = true;
		this.beachRocksEnabled = false;
		this.flowersEnabled = true;
		this.showPermissions = false;
		this.surfAble = true;
		this.resetTiles();
	}

	private void resetTiles() {
		this.tiles = new TileType[this.width][this.height];
		this.permissions = new Permission[this.width][this.height];

		for (int i = 0; i < this.width; ++i) {
			for (int j = 0; j < this.height; ++j) {
				this.tiles[i][j] = TileType.SEA;
				this.permissions[i][j] = Permission.FOUR;
			}
		}
	}

	public void generateMap() {
		Random random = new Random(System.nanoTime());
		float[][] noise = SimplexNoise.generateOctavedSimplexNoise(this.width, this.height, this.octaves,
				this.roughness, this.scale, (short) random.nextInt(Short.MAX_VALUE + 1));

		float min = 0;
		float max = 0;
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {

				if (noise[i][j] < min) {
					min = noise[i][j];
				} else if (noise[i][j] > max) {
					max = noise[i][j];
				}
			}
		}

		float divisor = max - min;
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				noise[i][j] = (noise[i][j] - min) / divisor;
			}
		}

		float[][] gradient = this.generateGradient();
		for (int x = 0; x < noise.length; x++) {
			for (int y = 0; y < noise[x].length; y++) {
				float f = noise[x][y] - gradient[x][y];

				if (f <= SEA_LINE) {
					this.tiles[x][y] = TileType.SEA;
				} else if (f <= BEACH_LINE) {
					this.tiles[x][y] = TileType.BEACH;
				} else {
					if(f >= MOUNTAIN_SECOND_LINE && this.secondMountainEnabled) {
						this.tiles[x][y] = TileType.MOUNTAIN_LV2;
					}
					else{
						this.tiles[x][y] = TileType.MOUNTAIN;
					}
				}
			}
		}

		for(int i = 0; i < CLEANUP_COUNT; ++i) {
			for (int x = 0; x < tiles.length; x++) {
				for (int y = 0; y < tiles[x].length; y++) {
					if (tiles[x][y] == TileType.MOUNTAIN) {
						tiles[x][y] = cleanUpMountain(x, y);
					}
					else if (tiles[x][y] == TileType.MOUNTAIN_LV2) {
						tiles[x][y] = cleanUpMountainLeveled(x, y);
					}
					else if (tiles[x][y] == TileType.BEACH) {
						tiles[x][y] = cleanUpBeach(x, y);
					}
				}
			}	
		}
		
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y] == TileType.MOUNTAIN) {
					tiles[x][y] = setMountain(x, y);
				}
				else if (tiles[x][y] == TileType.MOUNTAIN_LV2) {
					tiles[x][y] = setMountainLeveled(x, y);
				}
				else if(tiles[x][y] == TileType.BEACH) {
					tiles[x][y] = setBeach(x, y);
				}
			}
		}

		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y] != TileType.BEACH && tiles[x][y] != TileType.SEA) {
					TileType type = setSecondMountain(x, y);
					if (type != null) {
						tiles[x][y] = type;
					}
				}
			}
		}

		if(this.grassEnabled && !this.secondMountainEnabled) {
			this.fillGrass();
		}
		
		for(int i = 0; i < MAX_ROCKS; ++i) {
			this.tryPlaceRock();
		}
		
		if(this.beachRocksEnabled) {
			this.tryPlaceBeachRocks();			
		}

		this.buildPermissionLayer();
		
		this.setChanged();
		this.notifyObservers();
	}

	private void buildPermissionLayer() {
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				TileType type = tiles[x][y];
				if(TileType.isBeach(type) || TileType.isGrass(type)) {
					this.permissions[x][y] = Permission.C;
				}
				else if(type == TileType.SEA && this.surfAble) {
					this.permissions[x][y] = Permission.FOUR;
				}
				else {
					this.permissions[x][y] = Permission.ONE;
				}
			}
		}
	}

	private void fillGrass() {
		Random random = new Random();
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if(tiles[x][y] == TileType.MOUNTAIN) {
					int i = random.nextInt(4);
					switch(i) {
					case 0:
						tiles[x][y] = TileType.GRASS_1;
						break;
					case 1:
						tiles[x][y] = TileType.GRASS_2;
						break;
					case 2:
						tiles[x][y] = TileType.GRASS_3;
						break;
					case 3:
						tiles[x][y] = TileType.GRASS_4;
						break;
					}
				}
				else if(tiles[x][y] == TileType.MOUNTAIN_U) {
					tiles[x][y] = TileType.GRASS_UP;
				}
			}
		}
		
		if(this.flowersEnabled) {
			for(int i = 0; i < MAX_FLOWERS; ++i) {
				int randX = random.nextInt(this.width - 4) + 4;
				int randY = random.nextInt(this.height - 4) + 4;
				if(TileType.isGrass(tiles[randX][randY])) {
					tiles[randX][randY] = TileType.FLOWER;
				}
			}	
		}
	}

	private void tryPlaceBeachRocks() {
		Random random = new Random();
		for(int i = 0; i < MAX_BEACH_ROCKS; ++i) {
			int randX = random.nextInt(this.width - 4) + 4;
			int randY = random.nextInt(this.height - 4) + 4;
			if(tiles[randX][randY] == TileType.BEACH) {
				tiles[randX][randY] = TileType.BEACH_ROCK;
			}
		}	
	}
	
	private void tryPlaceRock() {
		Random random = new Random();
		int randX = random.nextInt(this.width - 2);
		int randY = random.nextInt(this.height - 2);
		
		if(randX + 1 > this.width) {
			return;
		}
		else if(randY + 1 > this.height) {
			return;
		}
		else if(tiles[randX][randY] != TileType.SEA) {
			return;
		}
		else if(tiles[randX + 1][randY] != TileType.SEA) {
			return;
		}
		else if(tiles[randX][randY + 1] != TileType.SEA) {
			return;
		}
		else if(tiles[randX + 1][randY + 1] != TileType.SEA) {
			return;
		}
		
		int rock_version = (Math.random() <= 0.5) ? 1 : 2;
		
		if(rock_version == 1) {
			tiles[randX][randY] = TileType.ROCK_UL;
			tiles[randX + 1][randY] = TileType.ROCK_UR;
			tiles[randX][randY + 1] = TileType.ROCK_LL;
			tiles[randX + 1][randY + 1] = TileType.ROCK_LR;
		}
		else {
			tiles[randX][randY] = TileType.ROCK_2_UL;
			tiles[randX + 1][randY] = TileType.ROCK_2_UR;
			tiles[randX][randY + 1] = TileType.ROCK_2_LL;
			tiles[randX + 1][randY + 1] = TileType.ROCK_2_LR;
		}
	}

	public float[][] generateGradient(){
		int centerX = this.width / 2;
		int centerY = this.height / 2;

		float[][] valueArray = new float[this.width][this.height];
		for (int x = 0; x < this.width; x++) {
		    for (int y = 0; y < this.height; y++) {
		        float distanceX = (centerX - x) * (centerX - x);
		        float distanceY = (centerY - y) * (centerY - y);
		        float distanceToCenter = (float)Math.sqrt(distanceX + distanceY);
		        distanceToCenter = distanceToCenter / 128.0f;

		        valueArray[x][y] = distanceToCenter;
		    }
		}
		
		return valueArray;
	}

	private TileType cleanUpMountain(int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		int s = 0;
		int t = 0;
		if (x - 1 >= 0) {
			if(tiles[x - 1][y] == TileType.BEACH) {
				++s;
			}
		}
		if (y - 1 >= 0) {
			if(tiles[x][y - 1] == TileType.BEACH) {
				++t;
			}
		}
		if (x + 1 < this.width) {
			if(tiles[x + 1][y] == TileType.BEACH) {
				++s;
			}
		}
		if (y + 1 < this.height) {
			if(tiles[x][y + 1] == TileType.BEACH) {
				++t;
			}
		}
	
		if(s == 2 || t == 2) {
			return TileType.BEACH;
		}
		return TileType.MOUNTAIN;
	}
	
	private TileType cleanUpMountainLeveled(int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		int s = 0;
		int t = 0;
		int b = 0;
		if (x - 1 >= 0) {
			if(tiles[x - 1][y] == TileType.MOUNTAIN) {
				++s;
			}
			else if(tiles[x - 1][y] == TileType.BEACH) {
				++b;
			}
		}
		if (y - 1 >= 0) {
			if(tiles[x][y - 1] == TileType.MOUNTAIN) {
				++t;
			}
			else if(tiles[x][y - 1] == TileType.BEACH) {
				++b;
			}
		}
		if (x + 1 < this.width) {
			if(tiles[x + 1][y] == TileType.MOUNTAIN) {
				++s;
			}
			else if(tiles[x + 1][y] == TileType.BEACH) {
				++b;
			}
		}
		if (y + 1 < this.height) {
			if(tiles[x][y + 1] == TileType.MOUNTAIN) {
				++t;
			}
			else if(tiles[x][y + 1] == TileType.BEACH) {
				++b;
			}
		}
	
		if(b >= 1 || s == 2 || t == 2) {
			return TileType.MOUNTAIN;
		}
		return TileType.MOUNTAIN_LV2;
	}
	
	private TileType cleanUpBeach(int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		int j = 0;
		int s = 0;
		int t = 0;
		if (x - 1 >= 0) {
			if(tiles[x - 1][y] == TileType.SEA) {
				++s;
			}
			else if(tiles[x - 1][y] == TileType.MOUNTAIN) {
				++j;
			}
		}
		if (y - 1 >= 0) {
			if(tiles[x][y - 1] == TileType.SEA) {
				++t;
			}
			else if(tiles[x][y - 1] == TileType.MOUNTAIN) {
				++j;
			}
		}
		if (x + 1 < this.width) {
			if(tiles[x + 1][y] == TileType.SEA) {
				++s;
			}
			else if(tiles[x + 1][y] == TileType.MOUNTAIN) {
				++j;
			}
		}
		if (y + 1 < this.height) {
			if(tiles[x][y + 1] == TileType.SEA) {
				++t;
			}
			else if(tiles[x][y + 1] == TileType.MOUNTAIN) {
				++j;
			}
		}
	
		if(s == 2 || t == 2) {
			return TileType.SEA;
		}
		else if(j >= 3) {
			return TileType.MOUNTAIN;
		}
		return TileType.BEACH;
	}
	
	private TileType setMountain(int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		if (x - 1 >= 0) {
			tileL = tiles[x - 1][y];
		}
		if (y - 1 >= 0) {
			tileU = tiles[x][y - 1];
		}
		if (x + 1 < this.width) {
			tileR = tiles[x + 1][y];
		}
		if (y + 1 < this.height) {
			tileD = tiles[x][y + 1];
		}
		
		if (TileType.isBeach(tileU) && TileType.isBeach(tileR)) {
			return TileType.MOUNTAIN_RCU;
		} else if (TileType.isBeach(tileU) && TileType.isBeach(tileL)) {
			return TileType.MOUNTAIN_LCU;
		} else if (TileType.isBeach(tileD) && TileType.isBeach(tileR)) {
			return TileType.MOUNTAIN_RCL;
		} else if (TileType.isBeach(tileD) && TileType.isBeach(tileL)) {
			return TileType.MOUNTAIN_LCL;
		} else if (TileType.isBeach(tileU)) {
			return TileType.MOUNTAIN_U;
		} else if (TileType.isBeach(tileD)) {
			return TileType.MOUNTAIN_D;
		} else if (TileType.isBeach(tileR)) {
			return TileType.MOUNTAIN_R;
		} else if (TileType.isBeach(tileL)) {
			return TileType.MOUNTAIN_L;
		}

		return TileType.MOUNTAIN;
	}
	
	private TileType setMountainLeveled(int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		if (x - 1 >= 0) {
			tileL = tiles[x - 1][y];
		}
		if (y - 1 >= 0) {
			tileU = tiles[x][y - 1];
		}
		if (x + 1 < this.width) {
			tileR = tiles[x + 1][y];
		}
		if (y + 1 < this.height) {
			tileD = tiles[x][y + 1];
		}

		if (TileType.isMountain(tileU) && TileType.isMountain(tileR)) {
			return TileType.MOUNTAIN_LV2_RCU;
		} else if (TileType.isMountain(tileU) && TileType.isMountain(tileL)) {
			return TileType.MOUNTAIN_LV2_LCU;
		} else if (TileType.isMountain(tileD) && TileType.isMountain(tileR)) {
			return TileType.MOUNTAIN_LV2_RCL;
		} else if (TileType.isMountain(tileD) && TileType.isMountain(tileL)) {
			return TileType.MOUNTAIN_LV2_LCL;
		} else if (TileType.isMountain(tileU)) {
			return TileType.MOUNTAIN_LV2_U;
		} else if (TileType.isMountain(tileD)) {
			return TileType.MOUNTAIN_LV2_D;
		} else if (TileType.isMountain(tileR)) {
			return TileType.MOUNTAIN_LV2_R;
		} else if (TileType.isMountain(tileL)) {
			return TileType.MOUNTAIN_LV2_L;
		}

		return TileType.MOUNTAIN_LV2;
	}
	
	private TileType setBeach(int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		if (x - 1 >= 0) {
			tileL = tiles[x - 1][y];
		}
		if (y - 1 >= 0) {
			tileU = tiles[x][y - 1];
		}
		if (x + 1 < this.width) {
			tileR = tiles[x + 1][y];
		}
		if (y + 1 < this.height) {
			tileD = tiles[x][y + 1];
		}

		if (tileU == TileType.SEA && tileR == TileType.SEA) {
			return TileType.BEACH_RCU;
		} else if (tileU == TileType.SEA && tileL == TileType.SEA) {
			return TileType.BEACH_LCU;
		} else if (tileD == TileType.SEA && tileR == TileType.SEA) {
			return TileType.BEACH_RCL;
		} else if (tileD == TileType.SEA && tileL == TileType.SEA) {
			return TileType.BEACH_LCL;
		} else if (tileU != TileType.SEA && tileL == TileType.SEA) {
			return TileType.BEACH_L;
		} else if (tileU != TileType.SEA && tileR == TileType.SEA) {
			return TileType.BEACH_R;
		} else if (tileU == TileType.SEA) {
			return TileType.BEACH_U;
		} else if (tileD == TileType.SEA) {
			return TileType.BEACH_D;
		}

		return TileType.BEACH;
	}

	private TileType setSecondMountain(int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		if (x - 1 >= 0) {
			tileL = tiles[x - 1][y];
		}
		if (y - 1 >= 0) {
			tileU = tiles[x][y - 1];
		}
		if (x + 1 < this.width) {
			tileR = tiles[x + 1][y];
		}
		if (y + 1 < this.height) {
			tileD = tiles[x][y + 1];
		}

		if (tileD == TileType.MOUNTAIN_L && tileL == TileType.MOUNTAIN_D
				|| tileD == TileType.MOUNTAIN_L && tileL == TileType.MOUNTAIN_LCL
				|| tileD == TileType.MOUNTAIN_LCL && tileL == TileType.MOUNTAIN_LCL
				|| tileD == TileType.MOUNTAIN_LCL && tileL == TileType.MOUNTAIN_L
				|| tileD == TileType.MOUNTAIN_LCL && tileL == TileType.MOUNTAIN_D
				|| tileD == TileType.MOUNTAIN_LV2_L && tileL == TileType.MOUNTAIN_LV2_D
				|| tileD == TileType.MOUNTAIN_LV2_L && tileL == TileType.MOUNTAIN_LV2_LCL
				|| tileD == TileType.MOUNTAIN_LV2_LCL && tileL == TileType.MOUNTAIN_LV2_LCL
				|| tileD == TileType.MOUNTAIN_LV2_LCL && tileL == TileType.MOUNTAIN_LV2_L
				|| tileD == TileType.MOUNTAIN_LV2_LCL && tileL == TileType.MOUNTAIN_LV2_D) {
			return TileType.MOUNTAIN_SPL;
		}
		else if (tileD == TileType.MOUNTAIN_R && tileR == TileType.MOUNTAIN_D
				|| tileD == TileType.MOUNTAIN_R && tileR == TileType.MOUNTAIN_RCL
				|| tileD == TileType.MOUNTAIN_RCL && tileR == TileType.MOUNTAIN_RCL
				|| tileD == TileType.MOUNTAIN_RCL && tileR == TileType.MOUNTAIN_R
				|| tileD == TileType.MOUNTAIN_RCL && tileR == TileType.MOUNTAIN_D
				|| tileD == TileType.MOUNTAIN_LV2_R && tileR == TileType.MOUNTAIN_LV2_D
				|| tileD == TileType.MOUNTAIN_LV2_R && tileR == TileType.MOUNTAIN_LV2_RCL
				|| tileD == TileType.MOUNTAIN_LV2_RCL && tileR == TileType.MOUNTAIN_LV2_RCL
				|| tileD == TileType.MOUNTAIN_LV2_RCL && tileR == TileType.MOUNTAIN_LV2_R
				|| tileD == TileType.MOUNTAIN_LV2_RCL && tileR == TileType.MOUNTAIN_LV2_D) {
			return TileType.MOUNTAIN_SPR;
		}

		return null;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		this.resetTiles();
		this.setChanged();
		this.notifyObservers();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		this.resetTiles();
		this.setChanged();
		this.notifyObservers();
	}

	public TileType[][] getTiles() {
		return this.tiles;
	}

	public Permission[][] getPermissions() {
		return this.permissions;
	}
	
	public int getOctaves() {
		return octaves;
	}

	public void setOctaves(int octaves) {
		this.octaves = octaves;
		this.setChanged();
		this.notifyObservers();
	}

	public void setMountainLevels(boolean selected) {
		this.secondMountainEnabled = selected;
	}
	
	public float getRoughness() {
		return roughness;
	}

	public void setRoughness(float roughness) {
		this.roughness = roughness;
		this.setChanged();
		this.notifyObservers();
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
		this.setChanged();
		this.notifyObservers();
	}

	public void setGrass(boolean selected) {
		this.grassEnabled = selected;
	}
	
	public void setFlowers(boolean selected) {
		this.flowersEnabled = selected;
	}

	public void setBeachRocks(boolean selected) {
		this.beachRocksEnabled = selected;
	}
	
	public boolean showPermissions() {
		return showPermissions;
	}

	public void setPermissions(boolean selected) {
		this.showPermissions = selected;
		this.setChanged();
		this.notifyObservers();
	}

	public void setSurfable(boolean selected) {
		this.surfAble = selected;
	}

	public boolean isSurfable() {
		return this.surfAble;
	}
}
