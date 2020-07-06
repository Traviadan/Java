package de.awi.catalog.models;

import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class StorageUnitModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public StorageUnitModel() {
		super(StorageUnit.class);
	}

	protected String[] getFields() {
		String[] f = { StorageUnit.ID, StorageUnit.STORAGELOCATIONID, StorageUnit.NAME, StorageUnit.DESCRIPTION, StorageUnit.TYPE,
				StorageUnit.LENGTH, StorageUnit.WIDTH, StorageUnit.HEIGHT, StorageUnit.WEIGHT };
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
