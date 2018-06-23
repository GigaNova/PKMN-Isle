package View;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import Model.Map;

public class MapPanel extends JPanel implements Observer {

	private Map map;
	
	public MapPanel(Map map) {
		this.map = map;
		this.map.addObserver(this);
		this.setSize();
		this.setBackground(Color.BLUE);
	}

	private void setSize() {
		this.setPreferredSize(new Dimension(map.getWidth() * Map.TILE_SIZE, map.getHeight() * Map.TILE_SIZE));
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		this.setSize();
		this.revalidate();
		this.repaint();
	}
}
