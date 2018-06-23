package View;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import Controller.Controller;
import Model.Map;

public class ControlPanel extends JPanel {
	private Controller controller;
	
	public ControlPanel(Controller controller, Map map) {
		this.controller = controller;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.add(new JLabel("Width"));
		JSlider mapWidthSlider = new JSlider(JSlider.HORIZONTAL, Map.MIN_MAP_SIZE, Map.MAX_MAP_SIZE, map.getWidth());
		mapWidthSlider.addChangeListener((e) ->{
			controller.setWidth(mapWidthSlider.getValue());
		});
		mapWidthSlider.setMajorTickSpacing(10);
		mapWidthSlider.setMinorTickSpacing(1);
		mapWidthSlider.setPaintTicks(true);
		mapWidthSlider.setPaintLabels(true);
		this.add(mapWidthSlider);
		
		this.add(new JLabel("Height"));
		JSlider mapHeightSlider = new JSlider(JSlider.HORIZONTAL, Map.MIN_MAP_SIZE, Map.MAX_MAP_SIZE, map.getHeight());
		mapHeightSlider.addChangeListener((e) ->{
			controller.setHeight(mapHeightSlider.getValue());
		});
		mapHeightSlider.setMajorTickSpacing(10);
		mapHeightSlider.setMinorTickSpacing(1);
		mapHeightSlider.setPaintTicks(true);
		mapHeightSlider.setPaintLabels(true);
		this.add(mapHeightSlider);
		
		JButton generateButton = new JButton("Generate");
		generateButton.addActionListener((e) ->{
			controller.generateMap();
		});
		this.add(generateButton);
	}
	
}
