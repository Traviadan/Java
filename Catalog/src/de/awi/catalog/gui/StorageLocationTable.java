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
import de.awi.catalog.models.StorageLocation;
import de.awi.catalog.models.StorageLocationModel;

public class StorageLocationTable extends AbstractTable {
	private static final long serialVersionUID = 1L;

	private EventListenerList stockpilingListeners = new EventListenerList();

	public StorageLocationTable() {
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
		StorageLocationModel model = (StorageLocationModel)getModel();
		Vector<Boolean> vis = model.getColumnVisibilities();
		
		for (String name: model.getColumnNames()) {
			int col = model.findColumn(name);
			if (!vis.get(col)) {
				getColumnModel().getColumn(col).setPreferredWidth(0);
				getColumnModel().getColumn(col).setMinWidth(0);
				getColumnModel().getColumn(col).setMaxWidth(0);
			} else {
				if (name == StorageLocation.NAME) {
					getColumnModel().getColumn(col).setPreferredWidth(150); // Name
					getColumnModel().getColumn(col).setMinWidth(100);
					getColumnModel().getColumn(col).setMaxWidth(200);
				} else if (name == StorageLocation.DESCRIPTION) {
					getColumnModel().getColumn(col).setPreferredWidth(280); // Description
					getColumnModel().getColumn(col).setMinWidth(150);
				}
			}
		}
	}
	
	public void addPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem stockpilingMenuItem = new JMenuItem("Lager zuordnen");
        stockpilingMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, StorageLocationModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), false));
        	}
        });
        popupMenu.add(stockpilingMenuItem);

        JMenuItem outsourceMenuItem = new JMenuItem("Zuordnung entfernen");
        outsourceMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, StorageLocationModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), true));
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
