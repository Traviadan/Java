package de.awi.catalog.models;

import de.traviadan.lib.db.DbTableModel;

public class DeviceModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public DeviceModel() {
		super(Device.class);
	}

	@Override
	public String getColumnName(int column) {
		String header = (String)columns.keySet().toArray()[column];
		return header;
	}
	
}
