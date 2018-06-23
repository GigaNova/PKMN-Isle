package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import Model.Map;
import Model.TileType;

public class MapPanel extends JPanel implements Observer {

	private Map map;
	private Image tileSet;

	public MapPanel(Map map) {
		this.map = map;
		this.map.addObserver(this);

		this.tileSet = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("../Resource/tileset.png"));

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

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		TileType[][] mapTiles = map.getTiles();
		for (int i = 0; i < mapTiles.length; ++i) {
			for (int j = 0; j < mapTiles[i].length; ++j) {
				this.drawTile(g, mapTiles[i][j], i, j);
			}
		}
	}

	private void drawTile(Graphics g, TileType tileType, int x, int y) {
		g.drawImage(this.tileSet, x * Map.TILE_SIZE, y * Map.TILE_SIZE, x * Map.TILE_SIZE + Map.TILE_SIZE,
				y * Map.TILE_SIZE + Map.TILE_SIZE, tileType.getX(), tileType.getY(), tileType.getX() + Map.TILE_SIZE,
				tileType.getY() + Map.TILE_SIZE, this);
	}
}
