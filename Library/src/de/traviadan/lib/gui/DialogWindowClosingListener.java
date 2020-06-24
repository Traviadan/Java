package de.traviadan.lib.gui;

import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

public class DialogWindowClosingListener extends WindowAdapter {
	@Override public void windowClosing( WindowEvent event ) {
		if(event.getWindow() instanceof WindowFrame) {
			int choice = JOptionPane.showOptionDialog(
					event.getWindow(),
					"Wirklich schliessen?",
					"Quit?",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null, null, null);
			if(choice == JOptionPane.YES_OPTION) {
				WindowFrame win = (WindowFrame)event.getWindow();
				win.dispose();
				System.exit(0);
			}
		}
	}
}
