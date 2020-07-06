package de.awi.catalog.events;

import java.util.EventObject;

public class StockpilingEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private Object obj;
	private boolean outsorce;
	
	public StockpilingEvent(Object source, Object stockpilingObj, boolean outsource) {
		super(source);
		this.obj = stockpilingObj;
		this.outsorce = outsource;
	}
	
	public Object getStockpilingObject() {
		return obj;
	}
	
	public boolean isOutsourcing() {
		return this.outsorce;
	}

}
