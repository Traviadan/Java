package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbTableJoin;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableName;

@DbTableJoin(table = {StorageUnit.class}, using= {"storageunitid"})
@DbTableName(name="devices")
public class Device {
	public static final String ID = "deviceid";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String SERIALNR = "serialnr";
	public static final String UNITNR = "unitnr";
	public static final String TYPE = "type";
	public static final String MANUFACTURER = "manufacturer";
	public static final String PROTECTION = "protection";
	public static final String LOCATIONID = "locationid";
	public static final String INTERVAL = "interval";
	public static final String PROJECTID = "projectid";
	public static final String STORAGEUNITID = "storageunitid";

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
	private int id;
	private String name;
	private String serialnr;
	private String unitnr;
	private Device.Type type;
	private String description;
	private String manufacturer;
	private Device.Protection protection;
	private int locationid;
	private int interval;
	private int projectid;
	private int storageunitid;

	public Device() {
		id = 0;
		name = "";
		serialnr = "";
		unitnr = "";
		type = Type.NA;
		description = "";
		manufacturer = "";
		protection = Protection.NA;
		locationid = 0;
		interval = 0;
		projectid = 0;
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
		return this.name;
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

	@DbFieldGetter(name=UNITNR, title="Teile-Nr.")
	public String getUnitnr() {
		return unitnr;
	}
	@DbFieldSetter(name=UNITNR)
	public void setUnitnr(String unitnr) {
		this.unitnr = unitnr;
	}

	@DbFieldGetter(name=TYPE, title="Gerätetyp")
	public int getTypeDb() {
		return type.ordinal();
	}
	public Device.Type getType() {
		return type;
	}
	@DbFieldSetter(name=TYPE)
	public void setTypeDb(int ord) {
		this.type = Device.Type.values()[ord];
	}
	public void setType(Device.Type type) {
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

	@DbFieldGetter(name=STORAGEUNITID, visibility=false, title="LagereinheitId")
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
		Device other = (Device) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
