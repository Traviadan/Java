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
	public static final String WEIGHT = "weight";
	public static final String PRICE = "price";
	
	public enum Type {
		NA (""), Electronic ("Elektronikteil"), Mechanic ("Mechanikteil"), Accessoire ("Zubehör");
		private String entry;
		private Type(String entry) { this.entry = entry; }
		@Override
		public String toString() { return this.entry; }
	}
	
	protected int id;
	protected String name;
	protected String serialnr;
	protected String partnr;
	protected Material.Type type;
	protected String description;
	protected String manufacturer;
	protected int storageunitid;
	protected double weight;
	protected double price;
	
	
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
	abstract public int getTypeDb();
	
	abstract public Material.Type getType();
	
	@DbFieldSetter(name=TYPE)
	abstract public void setTypeDb(int type);
	
	abstract public void setType(Material.Type type);
	
	@DbFieldGetter(name=DESCRIPTION, title="Beschreibung")
	abstract public String getDescription();
	
	@DbFieldSetter(name=DESCRIPTION)
	abstract public void setDescription(String description);
	
	@DbFieldGetter(name=MANUFACTURER, title="Hersteller")
	abstract public String getManufacturer();
	
	@DbFieldSetter(name=MANUFACTURER)
	abstract public void setManufacturer(String manufacturer);
	
	@DbFieldGetter(name=STORAGEUNITID, visibility=false, title="Lagereinheit-Nr.")
	abstract public int getStorageunitid();
	
	@DbFieldSetter(name=STORAGEUNITID)
	abstract public void setStorageunitid(int storageunitid);

	@DbFieldGetter(name=WEIGHT, title="Gewicht")
	abstract public double getWeight();

	@DbFieldSetter(name=WEIGHT)
	abstract public void setWeight(double weight);

	@DbFieldGetter(name=PRICE, title="Preis")
	abstract public double getPrice();

	@DbFieldSetter(name=PRICE)
	abstract public void setPrice(double price);
}
