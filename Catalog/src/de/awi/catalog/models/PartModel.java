package de.awi.catalog.models;

import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class PartModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public enum Fields {
		Id (Part.ID), Name (Part.NAME), Description (Part.DESCRIPTION), Manufacturer (Part.MANUFACTURER), PartNr (Part.PARTNR),
		SerialNr (Part.SERIALNR), Type(Part.TYPE), StorageunitId (Part.STORAGEUNITID);
		
		private String name;
		private Fields(String name) {
			this.name = name;
		}
		public String getFieldName() {
			return this.name;
		}
	}
	
	public PartModel() {
		super(Part.class);
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
