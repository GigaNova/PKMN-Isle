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
		// TODO Auto-generated method stub
		
	}
}
