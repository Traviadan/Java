package de.awi.catalog.models;

import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public abstract class MaterialModel extends DbTableModel {
	private static final long serialVersionUID = 1L;

	public MaterialModel(Class<?> c) {
		super(c);
	}

	protected String[] getMaterialFields() {
		String[] f = {Material.ID, Material.NAME, Material.DESCRIPTION, Material.MANUFACTURER,
				Material.PARTNR, Material.SERIALNR, Material.TYPE, Material.STORAGEUNITID };
		return f;
	}

	protected void initMaterialColumns() {
		Map<String, Map<String, Object>> unsorted = Db.getColumnProperties(Material.class);
		for (String field : getMaterialFields()) {
			properties.put(field, unsorted.get(field));
		}
	}
	
}
