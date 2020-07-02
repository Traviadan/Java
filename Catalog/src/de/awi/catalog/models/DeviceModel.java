package de.awi.catalog.models;

import java.lang.reflect.Method;
import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class DeviceModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public enum Fields {
		Id ("deviceid"), Name ("name"), Description ("description"), Manufacturer ("manufacturer"), UnitNr ("unitnr"),
		SerialNr ("serialnr"), Type("type"), Protection("protection"), Interval("interval"), StorageunitId ("storageunitid"),
		ProjectId ("projectid"), LocationId ("locationid");
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
	public String getColumnName(int column) {
		String header = (String)columns.keySet().toArray()[column];
		return header;
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
