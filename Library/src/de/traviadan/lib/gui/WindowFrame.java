package de.traviadan.lib.gui;

import javax.swing.JFrame;

public class WindowFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public WindowFrame() {
		super();
		initFrame();
	}
	
	public WindowFrame(String title) {
		super(title);
		initFrame();
	}
	
	private void initFrame() {
		// Fenster mittig vom Bildschirm
		setLocationRelativeTo(null);
		
	    addWindowListener(new DialogWindowClosingListener());
	}

	protected void close() {
		dispose();
	}
	protected void close(int status) {
		dispose();
		System.exit(status);
	}
	
	protected void addTextPane(int width, int height, String position) {
	    add(GuiFactory.getScrollTextPane(width, height), position);
	}
}
