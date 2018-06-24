package Controller;

import Model.Map;
import View.GUI;

public class Controller {

	private GUI gui;
	private Map map;
	
	public Controller() {
		this.map = new Map();
		this.gui = new GUI(this, this.map);
	}
	
	public void setHeight(int height) {
		this.gui.updateTitle();
		this.map.setHeight(height);
	}
	
	public void setWidth(int width) {
		this.gui.updateTitle();
		this.map.setWidth(width);
	}

	public void generateMap() {
		this.map.generateMap();
	}

	public void setOctave(int octaves) {
		this.map.setOctaves(octaves);
	}

	public void setMountainLevels(boolean selected) {
		this.map.setMountainLevels(selected);
	}

	public void setGrass(boolean selected) {
		this.map.setGrass(selected);
	}
	
	public void setFlowers(boolean selected) {
		this.map.setFlowers(selected);
	}
}
