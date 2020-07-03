package de.awi.catalog.events;

import java.util.EventObject;

public class StockpilingEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private Object obj;
	
	public StockpilingEvent(Object source, Object stockpilingObj) {
		super(source);
		this.obj = stockpilingObj;
	}
	
	public Object getStockpilingObject() {
		return obj;
	}

}
