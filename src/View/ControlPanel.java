package View;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import Controller.Controller;
import Model.Map;

public class ControlPanel extends JPanel {
	private Controller controller;
	
	public ControlPanel(Controller controller, Map map) {
		this.controller = controller;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("MainSettings"));
		
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
		
		this.add(new JLabel("Octave"));
		JSlider mapOctaveSlider = new JSlider(JSlider.HORIZONTAL, Map.MIN_OCTAVE, Map.MAX_OCTAVE, map.getOctaves());
		mapOctaveSlider.addChangeListener((e) ->{
			controller.setOctave(mapOctaveSlider.getValue());
		});
		mapOctaveSlider.setMajorTickSpacing(2);
		mapOctaveSlider.setMinorTickSpacing(1);
		mapOctaveSlider.setPaintTicks(true);
		mapOctaveSlider.setPaintLabels(true);
		this.add(mapOctaveSlider);
		
		JSeparator MSeperator = new JSeparator(SwingConstants.HORIZONTAL);
		MSeperator.setMaximumSize( new Dimension(Integer.MAX_VALUE, 4) );
		this.add(MSeperator);
		
		JCheckBox checkbox = new JCheckBox("Multi-level Mountains");
		checkbox.setSelected(true);
		checkbox.addActionListener((e)->{
			controller.setMountainLevels(checkbox.isSelected());
		});
		this.add(checkbox);
		
		JCheckBox SCheckbox = new JCheckBox("Sea is surfable");
		SCheckbox.setSelected(true);
		SCheckbox.addActionListener((e)->{
			controller.setSurfable(SCheckbox.isSelected());
		});
		this.add(SCheckbox);
		
		JCheckBox RCheckbox = new JCheckBox("Rocks on beach");
		RCheckbox.setSelected(false);
		RCheckbox.addActionListener((e)->{
			controller.setBeachRocks(RCheckbox.isSelected());
		});
		this.add(RCheckbox);
		
		JSeparator GSeperator = new JSeparator(SwingConstants.HORIZONTAL);
		GSeperator.setMaximumSize( new Dimension(Integer.MAX_VALUE, 4) );
		this.add(GSeperator);
		
		this.add(new GrassPanel(controller, map));
		
		JSeparator PSeperator = new JSeparator(SwingConstants.HORIZONTAL);
		PSeperator.setMaximumSize( new Dimension(Integer.MAX_VALUE, 4) );
		this.add(PSeperator);
		
		JCheckBox PCheckbox = new JCheckBox("Show Permissions");
		PCheckbox.setSelected(false);
		PCheckbox.addActionListener((e)->{
			controller.setPermissions(PCheckbox.isSelected());
		});
		this.add(PCheckbox);
		
		JSeparator BSeperator = new JSeparator(SwingConstants.HORIZONTAL);
		BSeperator.setMaximumSize( new Dimension(Integer.MAX_VALUE, 4) );
		this.add(BSeperator);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		JButton generateButton = new JButton("Generate");
		generateButton.addActionListener((e) ->{
			controller.generateMap();
		});
		buttonPanel.add(generateButton);
		
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener((e) ->{
			controller.saveMap();
		});
		buttonPanel.add(saveButton);
		
		this.add(buttonPanel);
	}
	
}
