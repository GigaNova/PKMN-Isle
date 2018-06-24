package View;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import Controller.Controller;
import Model.Map;

public class GrassPanel extends JPanel {

	public GrassPanel(Controller controller, Map map) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("Grass Settings"));
		
		JCheckBox grassBox = new JCheckBox("Enable Grass");
		grassBox.setSelected(false);
		grassBox.addActionListener((e)->{
			controller.setGrass(grassBox.isSelected());
		});
		this.add(grassBox);
		
		JCheckBox flowerBox = new JCheckBox("Enable Flowers");
		flowerBox.setSelected(true);
		flowerBox.addActionListener((e)->{
			controller.setFlowers(flowerBox.isSelected());
		});
		this.add(flowerBox);
	}
	
}
