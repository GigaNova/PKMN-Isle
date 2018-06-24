package View;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import Model.Map;
import Model.Permission;
import Model.TileType;

public class MapPanel extends JPanel implements Observer {

	private Map map;
	private Image tileSet;
	private Image permissions;

	public MapPanel(Map map) {
		this.map = map;
		this.map.addObserver(this);

		this.tileSet = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Resources/tileset.png"));
		this.permissions = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Resources/permissions.png"));

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

		this.paintMap(g);
		if(map.showPermissions()) {
			this.paintPermissions(g);
		}
	}

	private void paintMap(Graphics g) {
		TileType[][] mapTiles = map.getTiles();
		for (int i = 0; i < mapTiles.length; ++i) {
			for (int j = 0; j < mapTiles[i].length; ++j) {
				this.drawTile(g, mapTiles[i][j], i, j);
			}
		}
	}
	
	private void paintPermissions(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;	
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75F));
		Permission[][] permissions = map.getPermissions();
		for (int i = 0; i < permissions.length; ++i) {
			for (int j = 0; j < permissions[i].length; ++j) {
				this.drawPermission(g2, permissions[i][j], i, j);
			}
		}
	}

	private void drawTile(Graphics g, TileType tileType, int x, int y) {
		g.drawImage(this.tileSet, x * Map.TILE_SIZE, y * Map.TILE_SIZE, x * Map.TILE_SIZE + Map.TILE_SIZE,
				y * Map.TILE_SIZE + Map.TILE_SIZE, tileType.getX(), tileType.getY(), tileType.getX() + Map.TILE_SIZE,
				tileType.getY() + Map.TILE_SIZE, this);
	}
	
	private void drawPermission(Graphics g, Permission permission, int x, int y) {
		g.drawImage(this.permissions, x * Map.TILE_SIZE, y * Map.TILE_SIZE, x * Map.TILE_SIZE + Map.TILE_SIZE,
				y * Map.TILE_SIZE + Map.TILE_SIZE, permission.getX(), permission.getY(), permission.getX() + Map.TILE_SIZE,
				permission.getY() + Map.TILE_SIZE, this);
	}

}
