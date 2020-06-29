package de.awi.catalog;

import javax.swing.JTable;

public class DeviceTable extends JTable {
	private static final long serialVersionUID = 1L;

	public DeviceTable() {
		super();
	}
	
	public void setColWidths() {
		System.out.println(getColumnModel().getColumn(1).getHeaderValue());
		getColumnModel().getColumn(1).setPreferredWidth(200);
		getColumnModel().getColumn(1).setMinWidth(150);
	}
}
