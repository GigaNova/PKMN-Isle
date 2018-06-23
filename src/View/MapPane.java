package View;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import Model.Map;

public class MapPane extends JPanel {

	public MapPane(Map map) {
		this.setLayout(new GridBagLayout());
		this.add(new MapPanel(map));
		
		this.setPreferredSize(new Dimension(Map.MAX_MAP_SIZE * Map.TILE_SIZE, Map.MAX_MAP_SIZE * Map.TILE_SIZE));
	}
	
}
