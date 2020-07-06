package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableJoin;
import de.traviadan.lib.db.DbTableName;

@DbTableJoin(table = {Storage.class}, using= {StorageLocation.STORAGEID})
@DbTableName(name="storagelocations")
public class StorageLocation {
	public static final String ID = "storagelocationid";
	public static final String STORAGEID = "storageid";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	
	private int id;
	private int storageid;
	private String name;
	private String description;
	
	@DbFieldGetter(name=ID, title="Id", visibility=false, constraint="PRIMARY KEY")
	public int getId() {
		return id;
	}
	@DbFieldSetter(name=ID)
	public void setId(int id) {
		this.id = id;
	}
	
	@DbFieldGetter(name=STORAGEID, visibility=false, title="LagerId")
	public int getStorageid() {
		return storageid;
	}
	@DbFieldSetter(name=STORAGEID)
	public void setStorageid(int storageid) {
		this.storageid = storageid;
	}

	@DbFieldGetter(name=NAME, title="Bezeichnung")
	public String getName() {
		return name;
	}
	@DbFieldSetter(name=NAME)
	public void setName(String name) {
		this.name = name;
	}
	
	@DbFieldGetter(name=DESCRIPTION, title="Beschreibung")
	public String getDescription() {
		return description;
	}
	@DbFieldSetter(name=DESCRIPTION)
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StorageLocation other = (StorageLocation) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
