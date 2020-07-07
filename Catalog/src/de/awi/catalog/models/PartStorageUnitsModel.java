package de.awi.catalog.models;

import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class PartStorageUnitsModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public PartStorageUnitsModel() {
		super(PartStorageUnits.class);
	}

	protected String[] getFields() {
		String[] f = { PartStorageUnits.ID, PartStorageUnits.STORAGEUNITID, PartStorageUnits.AMOUNT };
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
