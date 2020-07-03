package de.awi.catalog.models;

import java.util.List;
import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;
import de.traviadan.lib.db.DbTableName;

public class DeviceModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public enum Fields {
		Id (Device.ID), Name (Device.NAME), Description (Device.DESCRIPTION), Manufacturer (Device.MANUFACTURER), UnitNr (Device.UNITNR),
		SerialNr (Device.SERIALNR), Type(Device.TYPE), Protection(Device.PROTECTION), Interval(Device.INTERVAL), StorageunitId (Device.STORAGEUNITID),
		ProjectId (Device.PROJECTID), LocationId (Device.LOCATIONID);
		
		private String name;
		private Fields(String name) {
			this.name = name;
		}
		public String getFieldName() {
			return this.name;
		}
	}
	
	public DeviceModel() {
		super(Device.class);
	}

	@Override
	protected void initColumns() {
		Map<String, Map<String, Object>> unsorted = Db.getColumnProperties(thisClass);
		for (Fields field: Fields.values()) {
			properties.put(field.getFieldName(), unsorted.get(field.getFieldName()));
		}
		super.initColumns();
	}

	@Override
	public String getColumnName(int column) {
		if (column >= super.getColumnCount() 
				&& joinedData.keySet().contains(StorageUnit.class)) {
			return "Lagereinheit";
		} else {
			String columnName = (String)columns.keySet().toArray()[column];
			Map<String, Object> prop = properties.get(columnName);
			return (String)prop.get(Db.TITLE);
		}
	}
	
	@Override
	public int getColumnCount() {
		int cc = super.getColumnCount();
		if (joinedData.size() == 0)
			return cc;
		
		if (joinedData.keySet().contains(StorageUnit.class)) {
			cc++;
		}
		
		return cc;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex >= super.getColumnCount() 
				&& joinedData.keySet().contains(StorageUnit.class)) {
			String tableName = StorageUnit.class.getAnnotation(DbTableName.class).name();
			List<Map<String, Object>> l = joinedData.get(StorageUnit.class);
			System.out.println(l);
			return l.get(rowIndex).get(String.format("%s.name", tableName));
		} else {
			return super.getValueAt(rowIndex, columnIndex);
		}
	}
	
}
