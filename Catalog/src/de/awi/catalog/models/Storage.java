package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableName;

@DbTableName(name="storages")
public class Storage {
	public static final String ID = "storageid";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	
	private int id;
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
		Storage other = (Storage) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
