package de.awi.catalog.models;

import de.traviadan.lib.db.DbFieldGetter;
import de.traviadan.lib.db.DbFieldSetter;
import de.traviadan.lib.db.DbTableJoin;
import de.traviadan.lib.db.DbTableName;

@DbTableJoin(table = {StorageUnit.class}, using= {PartStorageUnits.STORAGEUNITID})
@DbTableName(name="partstorageunits")
public class PartStorageUnits {
	public static final String ID = "partid";
	public static final String STORAGEUNITID = "storageunitid";
	public static final String AMOUNT = "amount";
	
	private int id;
	private int storageunitid;
	private double amount;
	
	public PartStorageUnits() {
		id = 0;
		storageunitid = 0;
		amount = 0.0d;
	}
	
	@DbFieldGetter(name=ID, title="Id", visibility=false, constraint="PRIMARY KEY")
	public int getId() {
		return this.id;
	}
	@DbFieldSetter(name=ID)
	public void setId(int id) {
		this.id = id;
	}

	@DbFieldGetter(name=STORAGEUNITID, title="StorageUnitId", visibility=false)
	public int getStorageUnitId() {
		return this.storageunitid;
	}
	@DbFieldSetter(name=STORAGEUNITID)
	public void setStorageUnitId(int storageUnitId) {
		this.storageunitid = storageUnitId;
	}

	@DbFieldGetter(name=AMOUNT, title="Menge")
	public double getAmount() {
		return this.amount;
	}
	@DbFieldSetter(name=AMOUNT)
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
