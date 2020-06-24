package de.traviadan.lib.gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class GuiFactory {
	private GuiFactory() {
		// Keine Objekte zulassen
	}
	
	public static JButton getButton(String text, ImageIcon img) {
		JButton btn = new JButton(text);
		if (img != null) {
			btn.setIcon(img);
		}
		return btn;
	}
	
	public static JTextPane getTextPane() {
		JTextPane txtPane = new JTextPane();
		return txtPane;
	}
	
	public static JTextPane getTextPane(int width, int height) {
		JTextPane txtPane = new JTextPane();
	    txtPane.setPreferredSize(new Dimension(width, height));
		return txtPane;
	}

	public static JScrollPane getScrollTextPane() {
	    return new JScrollPane(GuiFactory.getTextPane());
	}

	public static JScrollPane getScrollTextPane(int width, int height) {
	    return new JScrollPane(GuiFactory.getTextPane(width, height));
	}
}
