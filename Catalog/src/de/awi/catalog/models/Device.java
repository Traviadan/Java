package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbTableJoin;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableName;

@DbTableJoin(table = {StorageUnit.class}, using= {"storageunitid"})
@DbTableName(name="devices")
public class Device extends Material {
	public static final String UNITNR = "unitnr";
	public static final String TYPE = "type";
	public static final String PROTECTION = "protection";
	public static final String LOCATIONID = "locationid";
	public static final String INTERVAL = "interval";
	public static final String PROJECTID = "projectid";

	public enum Type {
		NA (""), Electric ("Elektrogerät"), Mechanic ("Mechanikteil"), Accessoire ("Zubehör");
		private String entry;
		private Type(String entry) { this.entry = entry; }
		@Override
		public String toString() { return this.entry; }
	}
	public enum Protection {
		NA (""), Class1 ("Schutzklasse 1"), Class2 ("Schutzklasse 2");
		private String entry;
		private Protection(String entry) { this.entry = entry; }
		@Override
		public String toString() { return this.entry; }
	}
	private String unitnr;
	private Device.Type deviceType;
	private Device.Protection protection;
	private int locationid;
	private int interval;
	private int projectid;

	public Device() {
		id = 0;
		name = "";
		serialnr = "";
		unitnr = "";
		deviceType = Type.NA;
		description = "";
		manufacturer = "";
		protection = Protection.NA;
		locationid = 0;
		interval = 0;
		projectid = 0;
		storageunitid = 0;
	}
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getSerialnr() {
		return serialnr;
	}
	@Override
	public void setSerialnr(String serialnr) {
		this.serialnr = serialnr;
	}

	@Override
	public String getDescription() {
		return description;
	}
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getManufacturer() {
		return manufacturer;
	}
	@Override
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Override
	public String getPartnr() {
		return this.partnr;
	}
	@Override
	public void setPartnr(String partnr) {
		this.partnr = partnr;
	}

	@Override
	public String getType() {
		return this.type;
	}
	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int getStorageunitid() {
		return storageunitid;
	}
	@Override
	public void setStorageunitid(int storageunitid) {
		this.storageunitid = storageunitid;
	}

	@DbFieldGetter(name=UNITNR, title="Identnr.", order=1)
	public String getUnitnr() {
		return unitnr;
	}
	@DbFieldSetter(name=UNITNR)
	public void setUnitnr(String unitnr) {
		this.unitnr = unitnr;
	}

	@DbFieldGetter(name=TYPE, title="Gerätetyp")
	public int getDeviceTypeDb() {
		return deviceType.ordinal();
	}
	public Device.Type getDeviceType() {
		return deviceType;
	}
	@DbFieldSetter(name=TYPE)
	public void setDeviceTypeDb(int ord) {
		this.deviceType = Device.Type.values()[ord];
	}
	public void setDeviceType(Device.Type type) {
		this.deviceType = type;
	}

	@DbFieldGetter(name=PROTECTION, title="Schutzklasse")
	public int getProtectionDb() {
		return protection.ordinal();
	}
	public Device.Protection getProtection() {
		return protection;
	}
	@DbFieldSetter(name=PROTECTION)
	public void setProtectionDb(int ord) {
		this.protection = Device.Protection.values()[ord];
	}
	public void setProtection(Device.Protection protection) {
		this.protection = protection;
	}

	@DbFieldGetter(name=LOCATIONID, visibility=false, title="LagerId")
	public int getLocationid() {
		return locationid;
	}
	@DbFieldSetter(name=LOCATIONID)
	public void setLocationid(int locationid) {
		this.locationid = locationid;
	}

	@DbFieldGetter(name=INTERVAL, title="Prüfintervall")
	public int getInterval() {
		return interval;
	}
	@DbFieldSetter(name=INTERVAL)
	public void setInterval(int interval) {
		this.interval = interval;
	}

	@DbFieldGetter(name=PROJECTID, visibility=false, title="ProjektId")
	public int getProjectid() {
		return projectid;
	}
	@DbFieldSetter(name=PROJECTID)
	public void setProjectid(int projectid) {
		this.projectid = projectid;
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
		Device other = (Device) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
