package de.awi.catalog.interfaces;

import java.util.EventListener;

import de.awi.catalog.events.StockpilingEvent;

public interface StockpilingListener extends EventListener {
	void updateStorage(StockpilingEvent event);
}
