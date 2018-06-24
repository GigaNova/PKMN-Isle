package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;

import Model.Map;
import Model.TileType;
import View.GUI;

public class SaveController {

	private Map map;
	private GUI gui;

	public SaveController(GUI gui, Map map) {
		this.map = map;
		this.gui = gui;
	}

	public void saveMap() {
		String hexString = "";

		// Width byte
		hexString += Integer.toHexString(map.getWidth());
		// Filler bytes
		hexString += "00";
		hexString += "00";
		hexString += "00";
		// Height byte
		hexString += Integer.toHexString(map.getHeight());
		// Filler bytes
		hexString += "00";
		hexString += "00";
		hexString += "00";
		// Tileset #1
		hexString += "00";
		// Filler bytes
		hexString += "00";
		hexString += "00";
		hexString += "00";
		// Tileset #2
		hexString += "3D";
		// Filler bytes
		hexString += "00";
		hexString += "00";
		hexString += "00";
		// Standard bytes (No idea what these do)
		hexString += "02";
		hexString += "02";
		hexString += "c0";
		hexString += "00";
		hexString += "42";
		hexString += "50";
		hexString += "52";
		hexString += "45";
		hexString += "34";
		// Filler bytes
		hexString += "00";
		hexString += "00";
		hexString += "00";

		TileType[][] tiles = map.getTiles();
		for (int y = 0; y < map.getHeight(); ++y) {
			for(int x = 0; x < map.getWidth(); ++x) {
				hexString += tiles[x][y].getTileByte();
				hexString += tiles[x][y].getPerByte();
			}
		}
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specifiy where to save map");   
		int userSelection = fileChooser.showSaveDialog(gui);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    File fileToSave = fileChooser.getSelectedFile();
		    if(!fileToSave.getAbsolutePath().toLowerCase().endsWith(".map"))
		    {
		    	fileToSave = new File(fileToSave.getAbsolutePath() + ".map");
		    }
			try (FileOutputStream fos = new FileOutputStream(fileToSave.getAbsolutePath())) {
				fos.write(this.hexStringToByteArray(hexString));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
