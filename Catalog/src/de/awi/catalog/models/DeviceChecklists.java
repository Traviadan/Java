package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableJoin;
import de.traviadan.lib.db.DbTableName;

@DbTableJoin(table = {Checklist.class}, using= {DeviceChecklists.CHECKLISTID})
@DbTableName(name="deviceschecklists")
public class DeviceChecklists {
	public static final String ID = "partid";
	public static final String CHECKLISTID = "checklistid";
	
	private int id;
	private int checklistid;
	
	public DeviceChecklists() {
		this.id = 0;
		this.checklistid = 0;
	}
	
	@DbFieldGetter(name=ID, title="DeviceId")
	public int getId() {
		return this.id;
	}
	@DbFieldSetter(name=ID)
	public void setId(int id) {
		this.id = id;
	}

	@DbFieldGetter(name=CHECKLISTID, title="ChecklistId")
	public int getChecklistId() {
		return this.checklistid;
	}
	@DbFieldSetter(name=CHECKLISTID)
	public void setChecklistId(int id) {
		this.checklistid = id;
	}
}
