package de.awi.catalog.models;

import de.traviadan.lib.db.DataFieldGetter;

public class Device {
	public enum Type {
		Electric, Mechanic, Accessoire
	}
	public enum Protection {
		Class1, Class2
	}
	
	private int id;
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

	@DataFieldGetter(name="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getSerialnr() {
		return serialnr;
	}
	public void setSerialnr(String serialnr) {
		this.serialnr = serialnr;
	}
	public String getUnitnr() {
		return unitnr;
	}
	public void setUnitnr(String unitnr) {
		this.unitnr = unitnr;
	}
	public Device.Type getType() {
		return type;
	}
	public void setType(Device.Type type) {
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
	public Device.Protection getProtection() {
		return protection;
	}
	public void setProtection(Device.Protection protection) {
		this.protection = protection;
	}
	public int getLocationid() {
		return locationid;
	}
	public void setLocationid(int locationid) {
		this.locationid = locationid;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getProjectid() {
		return projectid;
	}
	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}
	public int getStorageunitid() {
		return storageunitid;
	}
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
