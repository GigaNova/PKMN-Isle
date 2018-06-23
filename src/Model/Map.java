package Model;

import java.util.Observable;
import java.util.Random;

public class Map extends Observable {
	public final static int MAX_MAP_SIZE = 64;
	public final static int MIN_MAP_SIZE = 16;
	public final static int TILE_SIZE = 16;

	public static final int MIN_OCTAVE = 1;
	public static final int MAX_OCTAVE = 16;
	
	private final static double SEA_LINE = 0.5;
	private final static double BEACH_LINE = 0.7;

	private final static int CLEANUP_COUNT = 40;
	
	private int height;
	private int width;
	private int octaves;
	private float roughness;
	private float scale;

	private TileType[][] tiles;

	public Map() {
		this.height = (MAX_MAP_SIZE / 2) + (MIN_MAP_SIZE / 2);
		this.width = (MAX_MAP_SIZE / 2) + (MIN_MAP_SIZE / 2);
		this.octaves = 4;
		this.roughness = 0.66f;
		this.scale = 0.012f;
		this.resetTiles();
	}

	private void resetTiles() {
		this.tiles = new TileType[this.width][this.height];

		for (int i = 0; i < this.width; ++i) {
			for (int j = 0; j < this.height; ++j) {
				this.tiles[i][j] = TileType.SEA;
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
					this.tiles[x][y] = TileType.MOUNTAIN;
				}
			}
		}

		for(int i = 0; i < CLEANUP_COUNT; ++i) {
			for (int x = 0; x < tiles.length; x++) {
				for (int y = 0; y < tiles[x].length; y++) {
					if (tiles[x][y] == TileType.MOUNTAIN) {
						tiles[x][y] = cleanUpMountain(tiles, x, y);
					}
					else if (tiles[x][y] == TileType.BEACH) {
						tiles[x][y] = cleanUpBeach(tiles, x, y);
					}
				}
			}	
		}
		
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y] == TileType.MOUNTAIN) {
					tiles[x][y] = setMountain(tiles, x, y);
				}
				else if(tiles[x][y] == TileType.BEACH) {
					tiles[x][y] = setBeach(tiles, x, y);
				}
			}
		}

		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y] != TileType.BEACH && tiles[x][y] != TileType.SEA) {
					TileType type = setSecondMountain(tiles, x, y);
					if (type != null) {
						tiles[x][y] = type;
					}
				}
			}
		}

		this.setChanged();
		this.notifyObservers();
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

	private TileType cleanUpMountain(TileType[][] tiles, int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		int i = 0;
		if (x - 1 >= 0) {
			if(tiles[x - 1][y] == TileType.BEACH) {
				++i;
			}
		}
		if (y - 1 >= 0) {
			if(tiles[x][y - 1] == TileType.BEACH) {
				++i;
			}
		}
		if (x + 1 < this.width) {
			if(tiles[x + 1][y] == TileType.BEACH) {
				++i;
			}
		}
		if (y + 1 < this.height) {
			if(tiles[x][y + 1] == TileType.BEACH) {
				++i;
			}
		}
	
		if(i >= 3) {
			return TileType.BEACH;
		}
		return TileType.MOUNTAIN;
	}
	
	private TileType cleanUpBeach(TileType[][] tiles, int x, int y) {
		TileType tileU = null;
		TileType tileL = null;
		TileType tileD = null;
		TileType tileR = null;

		int i = 0;
		int j = 0;
		if (x - 1 >= 0) {
			if(tiles[x - 1][y] == TileType.SEA) {
				++i;
			}
			else if(tiles[x - 1][y] == TileType.MOUNTAIN) {
				++j;
			}
		}
		if (y - 1 >= 0) {
			if(tiles[x][y - 1] == TileType.SEA) {
				++i;
			}
			else if(tiles[x][y - 1] == TileType.MOUNTAIN) {
				++j;
			}
		}
		if (x + 1 < this.width) {
			if(tiles[x + 1][y] == TileType.SEA) {
				++i;
			}
			else if(tiles[x + 1][y] == TileType.MOUNTAIN) {
				++j;
			}
		}
		if (y + 1 < this.height) {
			if(tiles[x][y + 1] == TileType.SEA) {
				++i;
			}
			else if(tiles[x][y + 1] == TileType.MOUNTAIN) {
				++j;
			}
		}
	
		if(i >= 3) {
			return TileType.SEA;
		}
		else if(j >= 3) {
			return TileType.MOUNTAIN;
		}
		return TileType.BEACH;
	}
	
	private TileType setMountain(TileType[][] tiles, int x, int y) {
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

		if (tileU == TileType.BEACH && tileR == TileType.BEACH) {
			return TileType.MOUNTAIN_RCU;
		} else if (tileU == TileType.BEACH && tileL == TileType.BEACH) {
			return TileType.MOUNTAIN_LCU;
		} else if (tileD == TileType.BEACH && tileR == TileType.BEACH) {
			return TileType.MOUNTAIN_RCL;
		} else if (tileD == TileType.BEACH && tileL == TileType.BEACH) {
			return TileType.MOUNTAIN_LCL;
		} else if (tileU != TileType.BEACH && tileL == TileType.BEACH) {
			return TileType.MOUNTAIN_L;
		} else if (tileU != TileType.BEACH && tileR == TileType.BEACH) {
			return TileType.MOUNTAIN_R;
		} else if (tileU == TileType.BEACH) {
			return TileType.MOUNTAIN_U;
		} else if (tileD == TileType.BEACH) {
			return TileType.MOUNTAIN_D;
		}

		return TileType.MOUNTAIN;
	}
	
	private TileType setBeach(TileType[][] tiles, int x, int y) {
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

	private TileType setSecondMountain(TileType[][] tiles, int x, int y) {
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
				|| tileD == TileType.MOUNTAIN_LCL && tileL == TileType.MOUNTAIN_D) {
			return TileType.MOUNTAIN_SPL;
		}
		else if (tileD == TileType.MOUNTAIN_R && tileR == TileType.MOUNTAIN_D
				|| tileD == TileType.MOUNTAIN_R && tileR == TileType.MOUNTAIN_RCL
				|| tileD == TileType.MOUNTAIN_RCL && tileR == TileType.MOUNTAIN_RCL
				|| tileD == TileType.MOUNTAIN_RCL && tileR == TileType.MOUNTAIN_R
				|| tileD == TileType.MOUNTAIN_RCL && tileR == TileType.MOUNTAIN_D) {
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
		return tiles;
	}

	public int getOctaves() {
		return octaves;
	}

	public void setOctaves(int octaves) {
		this.octaves = octaves;
		this.setChanged();
		this.notifyObservers();
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
}
