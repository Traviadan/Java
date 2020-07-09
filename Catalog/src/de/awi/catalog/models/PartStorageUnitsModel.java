package de.awi.catalog.models;

import java.util.List;
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
	
	@Override
	public String getColumnName(int column) {
		if (column >= super.getColumnCount() 
				&& joinedData.size() > 0) {
			switch (column - super.getColumnCount()) {
				case 0:	return "Lagereinheit";
				case 1: return "Lagerort";
				case 2: return "Lager";
				default: return "";
			}
		} else {
			return super.getColumnName(column);
		}
	}
	
	@Override
	public int getColumnCount() {
		int cc = super.getColumnCount();
		if (joinedData.size() == 0) {
			return cc;
		} else {
			if (joinedData.keySet().contains(StorageUnit.class)) cc++;
			if (joinedData.keySet().contains(StorageLocation.class)) cc++;
			if (joinedData.keySet().contains(Storage.class)) cc++;
		}
		return cc;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex >= super.getColumnCount() 
				&& joinedData.size() > 0) {
			List<Map<String, Object>> l;
			switch (columnIndex - super.getColumnCount()) {
				case 0:	l = joinedData.get(StorageUnit.class);
						return l.get(rowIndex).get(String.format("%s_%s", Db.getTableName(StorageUnit.class), StorageUnit.NAME));

				case 1: l = joinedData.get(StorageLocation.class);
						return l.get(rowIndex).get(String.format("%s_%s", Db.getTableName(StorageLocation.class), StorageLocation.NAME));
				case 2: l = joinedData.get(Storage.class);
						return l.get(rowIndex).get(String.format("%s_%s", Db.getTableName(Storage.class), Storage.NAME));
				default: return null;
			}
		} else {
			return super.getValueAt(rowIndex, columnIndex);
		}
	}
}
