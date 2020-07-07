package de.awi.catalog.events;

import java.util.EventObject;

public class StockpilingEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private Object obj;
	private boolean outsorce;
	private double amount;
	
	public StockpilingEvent(Object source, Object stockpilingObj, boolean outsource) {
		super(source);
		this.obj = stockpilingObj;
		this.outsorce = outsource;
		this.amount = 0.0d;
	}

	public StockpilingEvent(Object source, Object stockpilingObj, double amount, boolean outsource) {
		this(source, stockpilingObj, outsource);
		this.amount = amount;
	}

	public Object getStockpilingObject() {
		return obj;
	}
	
	public boolean isOutsourcing() {
		return this.outsorce;
	}
	
	public double getAmount() {
		return this.amount;
	}

}
