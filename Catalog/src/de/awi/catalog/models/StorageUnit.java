package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableName;

@DbTableName(name="storageunits")
public class StorageUnit {
	public static final String ID = "storageunitid";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String WIDTH = "width";
	public static final String LENGTH = "length";
	public static final String HEIGHT = "height";
	public static final String WEIGHT = "weight";
	
	public enum Type {
		AluBox, CagePallet, Rack, Shelf 
	}
	private int id;
	private String name;
	private String description;
	private Type type;
	private int width;
	private int length;
	private int height;
	private int weight;
	
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
	
	@DbFieldGetter(name=TYPE, title="Lagertyp")
	public int getTypeDb() {
		return type.ordinal();
	}
	
	public Type getType() {
		return type;
	}
	
	@DbFieldSetter(name=TYPE)
	public void setTypeDb(int ord) {
		this.type = Type.values()[ord];
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	@DbFieldGetter(name=WIDTH, title="Breite")
	public int getWidth() {
		return width;
	}
	
	@DbFieldSetter(name=WIDTH)
	public void setWidth(int width) {
		this.width = width;
	}
	
	@DbFieldGetter(name=LENGTH, title="Länge")
	public int getLength() {
		return length;
	}
	
	@DbFieldSetter(name=LENGTH)
	public void setLength(int length) {
		this.length = length;
	}
	
	@DbFieldGetter(name=HEIGHT, title="Höhe")
	public int getHeight() {
		return height;
	}
	
	@DbFieldSetter(name=HEIGHT)
	public void setHeight(int height) {
		this.height = height;
	}
	
	@DbFieldGetter(name=WEIGHT, title="Gewicht")
	public int getWeight() {
		return weight;
	}
	
	@DbFieldSetter(name=WEIGHT)
	public void setWeight(int weight) {
		this.weight = weight;
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
		StorageUnit other = (StorageUnit) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
