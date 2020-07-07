package de.awi.catalog.models;

import java.util.List;
import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;
import de.traviadan.lib.db.DbTableName;

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
	
	@Override
	public String getColumnName(int column) {
		if (column >= super.getColumnCount() 
				&& joinedData.keySet().contains(StorageLocation.class)) {
			return "Lager";
		} else {
			return super.getColumnName(column);
		}
	}
	
	@Override
	public int getColumnCount() {
		int cc = super.getColumnCount();
		if (joinedData.size() == 0) return cc;
		else if (joinedData.keySet().contains(StorageLocation.class)) cc++;
		return cc;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex >= super.getColumnCount() 
				&& joinedData.keySet().contains(StorageLocation.class)) {
			String tableName = StorageLocation.class.getAnnotation(DbTableName.class).name();
			List<Map<String, Object>> l = joinedData.get(StorageLocation.class);
			return l.get(rowIndex).get(String.format("%s_%s", tableName, StorageLocation.NAME));
		} else {
			return super.getValueAt(rowIndex, columnIndex);
		}
	}
	
}
