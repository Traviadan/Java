package de.traviadan.lib.helper;

import javax.swing.JTextField;

public class Check {

	public static int forInteger(JTextField val) {
		int i = 0;
		try{
			i = Integer.parseInt(val.getText());
		} catch (NumberFormatException e) {
			val.setText("0");
		}
		return i;
	}
}
