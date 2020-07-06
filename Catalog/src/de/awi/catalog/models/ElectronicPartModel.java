package de.awi.catalog.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.traviadan.lib.db.Db;

public class ElectronicPartModel extends MaterialModel {
	private static final long serialVersionUID = 1L;
	
	public ElectronicPartModel() {
		super(ElectronicPart.class);
	}

	protected String[] getFields() {
		String[] f = { ElectronicPart.VOLTAGE, ElectronicPart.CURRENT };
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
