package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableName;

@DbTableName(name="storageunits")
public class StorageUnit {
	public enum Type {
		AluBox, CagePallet, Rack, Shelf 
	}
	private int storageunitid;
	private String name;
	private String description;
	private Type type;
	private int width;
	private int length;
	private int height;
	private int weight;
	
	@DbFieldGetter(name="storageunitid", title="Id", visibility=false, constraint="PRIMARY KEY")
	public int getStorageunitid() {
		return storageunitid;
	}

	@DbFieldSetter(name="storageunitid")
	public void setStorageunitid(int storageunitid) {
		this.storageunitid = storageunitid;
	}
	
	@DbFieldGetter(name="name", title="Bezeichnung")
	public String getName() {
		return name;
	}
	
	@DbFieldSetter(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	@DbFieldGetter(name="description", title="Beschreibung")
	public String getDescription() {
		return description;
	}
	
	@DbFieldSetter(name="description")
	public void setDescription(String description) {
		this.description = description;
	}
	
	@DbFieldGetter(name="type", title="Lagertyp")
	public int getTypeDb() {
		return type.ordinal();
	}
	
	public Type getType() {
		return type;
	}
	
	@DbFieldSetter(name="type")
	public void setTypeDb(int ord) {
		this.type = Type.values()[ord];
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	@DbFieldGetter(name="width", title="Breite")
	public int getWidth() {
		return width;
	}
	
	@DbFieldSetter(name="width")
	public void setWidth(int width) {
		this.width = width;
	}
	
	@DbFieldGetter(name="length", title="Länge")
	public int getLength() {
		return length;
	}
	
	@DbFieldSetter(name="length")
	public void setLength(int length) {
		this.length = length;
	}
	
	@DbFieldGetter(name="height", title="Höhe")
	public int getHeight() {
		return height;
	}
	
	@DbFieldSetter(name="height")
	public void setHeight(int height) {
		this.height = height;
	}
	
	@DbFieldGetter(name="weight", title="Gewicht")
	public int getWeight() {
		return weight;
	}
	
	@DbFieldSetter(name="weight")
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
