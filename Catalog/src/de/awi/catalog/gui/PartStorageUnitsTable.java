package de.awi.catalog.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionListener;

import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.PartStorageUnits;
import de.awi.catalog.models.PartStorageUnitsModel;
import de.awi.catalog.models.StorageUnit;

public class PartStorageUnitsTable extends AbstractTable {
	private static final long serialVersionUID = 1L;

	private EventListenerList stockpilingListeners = new EventListenerList();
	
	public PartStorageUnitsTable() {
		super();
		setRowSelectionAllowed(true);
	}
	
	public void addStockpilingListener( StockpilingListener listener ) {
		stockpilingListeners.add( StockpilingListener.class, listener );
	}
	
	public void removeStockpilingListener( StockpilingListener listener ) {
	    stockpilingListeners.remove( StockpilingListener.class, listener );
	}
	
	protected synchronized void notifyStockpiling( StockpilingEvent event ) {
	    for ( StockpilingListener l : stockpilingListeners.getListeners( StockpilingListener.class ) )
	    l.updateStorage(event);
	}
	
	public void setupColumns() {
		PartStorageUnitsModel model = (PartStorageUnitsModel)getModel();
		Vector<Boolean> vis = model.getColumnVisibilities();
		
		for (String name: model.getColumnNames()) {
			int col = model.findColumn(name);
			if (!vis.get(col)) {
				getColumnModel().getColumn(col).setPreferredWidth(0);
				getColumnModel().getColumn(col).setMinWidth(0);
				getColumnModel().getColumn(col).setMaxWidth(0);
			} else {
				if (name == PartStorageUnits.AMOUNT) {
					getColumnModel().getColumn(col).setPreferredWidth(40); // Menge
					getColumnModel().getColumn(col).setMinWidth(30);
				} else if (name == StorageUnit.NAME) {
					getColumnModel().getColumn(col).setPreferredWidth(120); // Lagereinheit
					getColumnModel().getColumn(col).setMinWidth(90);
				}
			}
		}
	}
	
	public void addPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem stockpilingMenuItem = new JMenuItem("Lagermenge ändern");
        stockpilingMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, PartStorageUnitsModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), false));
        	}
        });
        popupMenu.add(stockpilingMenuItem);

        JMenuItem outsourceMenuItem = new JMenuItem("Auslagern");
        outsourceMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, PartStorageUnitsModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), true));
        	}
        });
        popupMenu.add(outsourceMenuItem);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 && getSelectedRow() >= 0) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

	@Override
	public ListSelectionListener getListSelectionListener() {
		// TODO Auto-generated method stub
		return null;
	}
}
