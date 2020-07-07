package de.awi.catalog.models;

import java.util.List;
import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableName;

public class DeviceModel extends MaterialModel {
	private static final long serialVersionUID = 1L;
	
	public DeviceModel() {
		super(Device.class);
	}
	
	protected String[] getFields() {
		String[] f = { Device.UNITNR, Device.TYPE, Device.PROTECTION, Device.INTERVAL, Device.PROJECTID, Device.LOCATIONID };
		return f;
	}

	@Override
	protected void initColumns() {
		initMaterialColumns();
		Map<String, Map<String, Object>> unsorted = Db.getColumnProperties(thisClass);
		
		for (String field : getFields()) {
			properties.put(field, unsorted.get(field));
		}
		super.initColumns();
	}


	@Override
	public String getColumnName(int column) {
		if (column >= super.getColumnCount() 
				&& joinedData.keySet().contains(StorageUnit.class)) {
			return "Lagereinheit";
		} else {
			return super.getColumnName(column);
		}
	}
	
	@Override
	public int getColumnCount() {
		int cc = super.getColumnCount();
		if (joinedData.size() == 0) return cc;
		else if (joinedData.keySet().contains(StorageUnit.class)) cc++;
		return cc;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex >= super.getColumnCount() 
				&& joinedData.keySet().contains(StorageUnit.class)) {
			String tableName = StorageUnit.class.getAnnotation(DbTableName.class).name();
			List<Map<String, Object>> l = joinedData.get(StorageUnit.class);
			return l.get(rowIndex).get(String.format("%s_%s", tableName, StorageUnit.NAME));
		} else {
			return super.getValueAt(rowIndex, columnIndex);
		}
	}
	
}
