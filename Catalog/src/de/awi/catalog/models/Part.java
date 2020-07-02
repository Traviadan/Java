package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableJoin;
import de.traviadan.lib.db.DbTableName;

@DbTableJoin(table = {StorageUnit.class}, using= {"storageunitid"})
@DbTableName(name="parts")
public class Part {
	public static final String ID = "partid";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String SERIALNR = "serialnr";
	public static final String PARTNR = "partnr";
	public static final String TYPE = "type";
	public static final String MANUFACTURER = "manufacturer";
	public static final String STORAGEUNITID = "storageunitid";

	private int id;
	private String name;
	private String serialnr;
	private String partnr;
	private String type;
	private String description;
	private String manufacturer;
	private int storageunitid;
	
	public Part() {
		id = 0;
		name = "";
		serialnr = "";
		type = "";
		description = "";
		manufacturer = "";
		storageunitid = 0;
	}

	@DbFieldGetter(name=ID, title="Id", visibility=false, constraint="PRIMARY KEY")
	public int getId() {
		return id;
	}
	@DbFieldSetter(name=ID)
	public void setId(int id) {
		this.id = id;
	}
	
	@DbFieldGetter(name=NAME, title="Name")
	public String getName() {
		return name;
	}
	
	@DbFieldSetter(name=NAME)
	public void setName(String name) {
		this.name = name;
	}

	@DbFieldGetter(name=SERIALNR, title="Serien-Nr.")
	public String getSerialnr() {
		return serialnr;
	}
	@DbFieldSetter(name=SERIALNR)
	public void setSerialnr(String serialnr) {
		this.serialnr = serialnr;
	}

	@DbFieldGetter(name=PARTNR, title="Teile-Nr.")
	public String getPartnr() {
		return partnr;
	}
	@DbFieldSetter(name=PARTNR)
	public void setPartnr(String partnr) {
		this.partnr = partnr;
	}

	@DbFieldGetter(name=TYPE, title="Typ")
	public String getType() {
		return type;
	}
	@DbFieldSetter(name=TYPE)
	public void setType(String type) {
		this.type = type;
	}

	@DbFieldGetter(name=DESCRIPTION, title="Beschreibung")
	public String getDescription() {
		return description;
	}
	@DbFieldSetter(name=DESCRIPTION)
	public void setDescription(String description) {
		this.description = description;
	}

	@DbFieldGetter(name=MANUFACTURER, title="Hersteller")
	public String getManufacturer() {
		return manufacturer;
	}
	@DbFieldSetter(name=MANUFACTURER)
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@DbFieldGetter(name=STORAGEUNITID, title="Lagereinheit-Nr.")
	public int getStorageunitid() {
		return storageunitid;
	}
	@DbFieldSetter(name=STORAGEUNITID)
	public void setStorageunitid(int storageunitid) {
		this.storageunitid = storageunitid;
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
		Part other = (Part) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
