package de.awi.catalog.models;

import java.util.Map;

import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class ChecklistModel extends DbTableModel {
	private static final long serialVersionUID = 1L;
	
	public ChecklistModel() {
		super(Checklist.class);
	}

	protected String[] getFields() {
		String[] f = { Checklist.ID, Checklist.TESTNR, Checklist.DESCRIPTION, Checklist.TESTDATE, Checklist.EQUIPMENT, Checklist.TESTTYPE, Checklist.TESTDATE,
				Checklist.CHECKER, Checklist.RESULT, Checklist.COMMENT, Checklist.HOUSING, Checklist.PLUG, Checklist.CABLE, Checklist.SWITCH,
				Checklist.SOCKET, Checklist.SETTING, Checklist.FITNESS, Checklist.PERIPHERALS, Checklist.GENERAL, Checklist.PROTECTIVE_GROUND,
				Checklist.PROTECTIVE_GROUND_VOLTAGE, Checklist.INSULATION, Checklist.INSULATION_VOLTAGE, Checklist.CURRENT, Checklist.CURRENT_AMPERE };
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
