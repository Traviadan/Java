package de.awi.catalog.gui;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;

abstract public class AbstractTable extends JTable {
	private static final long serialVersionUID = 1L;
	protected JPopupMenu popupMenu;
	protected JMenuItem stockpilingMenuItem;
	protected JMenuItem outsourceMenuItem;


	abstract public ListSelectionListener getListSelectionListener();
	
	abstract public void setupColumns();
}
