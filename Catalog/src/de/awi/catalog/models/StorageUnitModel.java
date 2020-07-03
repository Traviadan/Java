package de.awi.catalog.models;

import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class StorageUnitModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public enum Fields {
		Id (StorageUnit.ID), Name (StorageUnit.NAME), Description (StorageUnit.DESCRIPTION),
		Type(StorageUnit.TYPE), Length (StorageUnit.LENGTH), Width (StorageUnit.WIDTH), Height (StorageUnit.HEIGHT), Weight (StorageUnit.WEIGHT);

		private String name;
		private Fields(String name) {
			this.name = name;
		}
		public String getFieldName() {
			return this.name;
		}
	}
	
	public StorageUnitModel() {
		super(StorageUnit.class);
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
