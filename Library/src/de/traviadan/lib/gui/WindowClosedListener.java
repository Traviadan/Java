package de.traviadan.lib.gui;

import java.awt.event.WindowEvent;

public class WindowClosedListener extends WindowAdapter {
	@Override public void windowOpened( WindowEvent event ) {
		if(event.getWindow() instanceof WindowFrame) {
			WindowFrame wf = (WindowFrame)event.getWindow();
			wf.opened();
		}
	}
}
