package de.awi.catalog.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.traviadan.lib.db.Db;

public class PartModel extends MaterialModel {
	private static final long serialVersionUID = 1L;
	
	public PartModel() {
		super(Part.class);
	}

	protected String[] getFields() {
		String[] f = { Part.VOLTAGE, Part.CURRENT };
		return f;
	}

	@Override
	protected void initColumns() {
		initMaterialColumns();
		String[] f = getFields();
		Map<String, Map<String, Object>> unsorted = Db.getColumnProperties(thisClass);
		for (String field : f) {
			properties.put(field, unsorted.get(field));
		}
		super.initColumns();
	}
	
}
