package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;

public abstract class Material {
	public static final String ID = "partid";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String SERIALNR = "serialnr";
	public static final String PARTNR = "partnr";
	public static final String TYPE = "type";
	public static final String MANUFACTURER = "manufacturer";
	public static final String STORAGEUNITID = "storageunitid";
	
	protected int id;
	protected String name;
	protected String serialnr;
	protected String partnr;
	protected String type;
	protected String description;
	protected String manufacturer;
	protected int storageunitid;
	
	@DbFieldGetter(name=ID, title="Id", visibility=false, constraint="PRIMARY KEY")
	abstract public int getId();
	
	@DbFieldSetter(name=ID)
	abstract public void setId(int id);
	
	@DbFieldGetter(name=NAME, title="Name")
	abstract public String getName();
	
	@DbFieldSetter(name=NAME)
	abstract public void setName(String name);
	
	@DbFieldGetter(name=SERIALNR, title="Serien-Nr.")
	abstract public String getSerialnr();
	
	@DbFieldSetter(name=SERIALNR)
	abstract public void setSerialnr(String serialnr);
	
	@DbFieldGetter(name=PARTNR, title="Teile-Nr.")
	abstract public String getPartnr();
	
	@DbFieldSetter(name=PARTNR)
	abstract public void setPartnr(String partnr);
	
	@DbFieldGetter(name=TYPE, title="Typ")
	abstract public String getType();
	
	@DbFieldSetter(name=TYPE)
	abstract public void setType(String type);
	
	@DbFieldGetter(name=DESCRIPTION, title="Beschreibung")
	abstract public String getDescription();
	
	@DbFieldSetter(name=DESCRIPTION)
	abstract public void setDescription(String description);
	
	@DbFieldGetter(name=MANUFACTURER, title="Hersteller")
	abstract public String getManufacturer();
	
	@DbFieldSetter(name=MANUFACTURER)
	abstract public void setManufacturer(String manufacturer);
	
	@DbFieldGetter(name=STORAGEUNITID, title="Lagereinheit-Nr.")
	abstract public int getStorageunitid();
	
	@DbFieldSetter(name=STORAGEUNITID)
	abstract public void setStorageunitid(int storageunitid);
}
