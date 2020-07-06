package de.awi.catalog.models;

import java.util.ArrayList;
import java.util.List;
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

	protected String[] getFields() {
		String[] f = { StorageUnit.ID, StorageUnit.NAME, StorageUnit.DESCRIPTION, StorageUnit.TYPE, StorageUnit.LENGTH,
				StorageUnit.WIDTH, StorageUnit.HEIGHT, StorageUnit.WEIGHT };
		return f;
	}
	
	@Override
	protected void initColumns() {
		Map<String, Map<String, Object>> unsorted = Db.getColumnProperties(thisClass);
		for (String field : getFields()) {
			properties.put(field, unsorted.get(field));
		}
		super.initColumns();
	}
	
}
