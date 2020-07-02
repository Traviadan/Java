package de.awi.catalog;

import javax.swing.JTable;

abstract public class AbstractTable extends JTable {

	private static final long serialVersionUID = 1L;

	abstract public void setupColumns();
}
