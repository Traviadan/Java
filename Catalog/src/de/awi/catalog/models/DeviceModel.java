package de.awi.catalog.models;

import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

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

	
}
