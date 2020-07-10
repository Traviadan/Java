package de.awi.catalog.models;

import java.util.List;
import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class DeviceChecklistsModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public DeviceChecklistsModel() {
		super(DeviceChecklists.class);
	}

	protected String[] getFields() {
		String[] f = { DeviceChecklists.ID, DeviceChecklists.CHECKLISTID };
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
				case 0:	return "Test-Nr.";
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
			if (joinedData.keySet().contains(Checklist.class)) cc++;
		}
		return cc;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex >= super.getColumnCount() 
				&& joinedData.size() > 0) {
			List<Map<String, Object>> l;
			switch (columnIndex - super.getColumnCount()) {
				case 0:	l = joinedData.get(Checklist.class);
						return l.get(rowIndex).get(String.format("%s_%s", Db.getTableName(Checklist.class), Checklist.TESTNR));
				default: return null;
			}
		} else {
			return super.getValueAt(rowIndex, columnIndex);
		}
	}
}
