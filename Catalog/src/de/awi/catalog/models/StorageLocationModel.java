package de.awi.catalog.models;

import java.util.List;
import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;
import de.traviadan.lib.db.DbTableName;

public class StorageLocationModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public StorageLocationModel() {
		super(StorageLocation.class);
	}

	protected String[] getFields() {
		String[] f = { StorageLocation.ID, StorageLocation.STORAGEID, StorageLocation.NAME, StorageLocation.DESCRIPTION };
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
				&& joinedData.keySet().contains(Storage.class)) {
			return "Lager";
		} else {
			return super.getColumnName(column);
		}
	}
	
	@Override
	public int getColumnCount() {
		int cc = super.getColumnCount();
		if (joinedData.size() == 0) return cc;
		else if (joinedData.keySet().contains(Storage.class)) cc++;
		return cc;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex >= super.getColumnCount() 
				&& joinedData.keySet().contains(Storage.class)) {
			String tableName = Storage.class.getAnnotation(DbTableName.class).name();
			List<Map<String, Object>> l = joinedData.get(Storage.class);
			return l.get(rowIndex).get(String.format("%s_%s", tableName, Storage.NAME));
		} else {
			return super.getValueAt(rowIndex, columnIndex);
		}
	}

}
