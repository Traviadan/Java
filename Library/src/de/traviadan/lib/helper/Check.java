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

	public static float forFloat(JTextField val) {
		float i = 0.0f;
		try{
			i = Float.parseFloat(val.getText());
		} catch (NumberFormatException e) {
			val.setText("0.0");
		}
		return i;
	}

	public static double forDouble(JTextField val) {
		double i = 0.0d;
		try{
			i = Double.parseDouble(val.getText());
		} catch (NumberFormatException e) {
			val.setText("0.0");
		}
		return i;
	}
}
