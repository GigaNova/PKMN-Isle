package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import Controller.Controller;
import Main.MainClass;
import Model.Map;

public class GUI extends JFrame {

	private Map map;

	public GUI(Controller controller, Map map) {
		this.map = map;

		this.setResizable(false);
		this.updateTitle();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		this.add(new ControlPanel(controller, map), BorderLayout.WEST);
		this.add(new MapPane(map), BorderLayout.CENTER);

		this.pack();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

		this.setVisible(true);
	}

	public void updateTitle() {
		this.setTitle("PKMN - IsleGen Ver " + MainClass.VERSION_NUMBER + " (" + this.map.getWidth() + "x"
				+ this.map.getHeight() + ")");
	}
}
