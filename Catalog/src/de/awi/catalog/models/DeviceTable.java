package de.awi.catalog.models;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class DeviceTable extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public DeviceTable() {
		super(Device.class);
	}
	
	public void populate(Db db) {
		
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
