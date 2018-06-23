package View;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import Controller.Controller;
import Model.Map;

public class GUI extends JFrame {

	private Map map;
	
	public GUI(Controller controller, Map map) {
		this.map = map;
		
		this.updateTitle();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		this.add(new ControlPanel(controller, map), BorderLayout.WEST);
		this.add(new MapPane(map), BorderLayout.CENTER);
		
		this.pack();
		this.setVisible(true);
	}

	public void updateTitle() {
		this.setTitle("PKMN - Island Generator (" + this.map.getWidth() + "x" + this.map.getHeight() + ")");
	}
}
