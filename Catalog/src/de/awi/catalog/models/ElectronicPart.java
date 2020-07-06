package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableJoin;
import de.traviadan.lib.db.DbTableName;

@DbTableJoin(table = {StorageUnit.class}, using= {"storageunitid"})
@DbTableName(name="electronicparts")
public class ElectronicPart extends Material {
	public static final String VOLTAGE = "voltage";
	public static final String CURRENT = "current";
	
	private int voltage;
	private int current;
	
	public ElectronicPart() {
		id = 0;
		name = "";
		serialnr = "";
		type = "";
		description = "";
		manufacturer = "";
		storageunitid = 0;
		voltage = 0;
		current = 0;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getSerialnr() {
		return serialnr;
	}
	public void setSerialnr(String serialnr) {
		this.serialnr = serialnr;
	}

	public String getPartnr() {
		return partnr;
	}
	public void setPartnr(String partnr) {
		this.partnr = partnr;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getStorageunitid() {
		return storageunitid;
	}
	public void setStorageunitid(int storageunitid) {
		this.storageunitid = storageunitid;
	}

	@DbFieldGetter(name=VOLTAGE, title="Spannung")
	public int getVoltage() {
		return voltage;
	}
	@DbFieldSetter(name=VOLTAGE)
	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}

	@DbFieldGetter(name=CURRENT, title="Strom")
	public int getCurrent() {
		return current;
	}
	@DbFieldSetter(name=CURRENT)
	public void setCurrent(int current) {
		this.current = current;
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
		ElectronicPart other = (ElectronicPart) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
