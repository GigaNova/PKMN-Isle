package Model;

import java.util.Observable;

public class Map extends Observable{
	public final static int MAX_MAP_SIZE = 64;
	public final static int MIN_MAP_SIZE = 16;
	public final static int TILE_SIZE = 16;
	
	private int height;
	private int width;
	
	public Map() {
		this.height = (MAX_MAP_SIZE / 2) + (MIN_MAP_SIZE / 2);
		this.width = (MAX_MAP_SIZE / 2) + (MIN_MAP_SIZE / 2);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		this.setChanged();
		this.notifyObservers();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		this.setChanged();
		this.notifyObservers();
	}
}
