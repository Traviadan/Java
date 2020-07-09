package de.traviadan.lib.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
	
	public static void placeComponent(Container panel, GridBagLayout gbl,
			String labelText,
			Component comp,
			int gridx, int gridy) {

		GuiFactory.placeComponent(panel, gbl, labelText, comp,
				new int[] { GridBagConstraints.NONE, GridBagConstraints.HORIZONTAL },
				new int[] { GridBagConstraints.EAST, GridBagConstraints.WEST },
				new int[] { 1, 1 },
				new int[] { 1, 1 },
				new float[][] { {0.5f, 0.0f}, {1.0f, 0.0f} },
				new int[][] { {10, 3}, {10, 3} },
				new Insets[] { new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, 
				gridx, gridy);
	}

	public static void placeComponent(
			Container panel, GridBagLayout gbl, String labelText, Component comp,
			int[] fill,
			int[] anchor,
			int[] width,
			int[] height,
			float[][] weight,
			int[][] pad,
			int gridx, int gridy) {
		
		GuiFactory.placeComponent(panel, gbl, labelText, comp,
				fill, anchor, width, height, weight, pad,
				new Insets[] { new Insets(0, 0, 0, 0), new Insets(0, 0, 0, 0) }, 
				gridx, gridy);
	}

	public static void placeComponent(
			Container panel, GridBagLayout gbl, String labelText, Component comp,
			int[] fill,
			int[] anchor,
			int[] width,
			int[] height,
			float[][] weight,
			int[][] pad,
			Insets[] insets,
			int gridx, int gridy) {

		GridBagConstraints gbc;
		JLabel label = new JLabel(labelText); // Label für das Texteingabefeld
		gbc = new GridBagConstraints();
		gbc.insets = insets[0];
		gbc.fill = fill[0];
		gbc.gridx = gridx; gbc.gridy = gridy;
		gbc.gridwidth = width[0]; gbc.gridheight = height[0];
		gbc.weightx = weight[0][0]; gbc.weighty = weight[0][1];
		gbc.ipadx = pad[0][0]; gbc.ipady = pad[0][1];
		gbc.anchor = anchor[0];
		gbl.setConstraints(label, gbc);
		panel.add(label);
		
		gbc = new GridBagConstraints();
		gbc.insets = insets[1];
		gbc.fill = fill[1];
		gbc.gridx = gridx+1; gbc.gridy = gridy;
		gbc.gridwidth = width[1]; gbc.gridheight = height[0];
		gbc.weightx = weight[1][0]; gbc.weighty = weight[1][1];
		gbc.ipadx = pad[1][0]; gbc.ipady = pad[1][1];
		gbc.anchor = anchor[1];
		gbl.setConstraints(comp, gbc);
		panel.add(comp);
	}

}
