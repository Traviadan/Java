package de.awi.catalog;

import java.util.Vector;

import javax.swing.JTable;

import de.awi.catalog.models.DeviceModel;

public class DeviceTable extends AbstractTable {
	private static final long serialVersionUID = 1L;

	public DeviceTable() {
		super();
		setRowSelectionAllowed(true);
	}
	
	public void setupColumns() {
		DeviceModel model = (DeviceModel)getModel();
		Vector<Boolean> vis = model.getColumnVisibilities();
		
		for (String name: model.getColumnNames()) {
			int col = model.findColumn(name);
			if (!vis.get(col)) {
				getColumnModel().getColumn(col).setPreferredWidth(0);
				getColumnModel().getColumn(col).setMinWidth(0);
				getColumnModel().getColumn(col).setMaxWidth(0);
			}
		}
		getColumnModel().getColumn(1).setPreferredWidth(150); // Name
		getColumnModel().getColumn(1).setMinWidth(100);
		getColumnModel().getColumn(2).setPreferredWidth(280); // Description
		getColumnModel().getColumn(2).setMinWidth(150);
	}
	
}
