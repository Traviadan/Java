package de.awi.catalog.models;

import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class StorageModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public StorageModel() {
		super(Storage.class);
	}

	protected String[] getFields() {
		String[] f = { Storage.ID, Storage.NAME, Storage.DESCRIPTION };
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
