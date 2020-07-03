package de.awi.catalog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.EventListenerList;

import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.DeviceModel;

public class DeviceTable extends AbstractTable {
	private static final long serialVersionUID = 1L;
	private EventListenerList stockpilingListeners = new EventListenerList();
	
	public DeviceTable() {
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
		DeviceModel model = (DeviceModel)getModel();
		Vector<Boolean> vis = model.getColumnVisibilities();
		
		for (String name: model.getColumnNames()) {
			int col = model.findColumn(name);
			if (!vis.get(col)) {
				getColumnModel().getColumn(col).setPreferredWidth(0);
				getColumnModel().getColumn(col).setMinWidth(0);
				getColumnModel().getColumn(col).setMaxWidth(0);
			}
		}
		getColumnModel().getColumn(1).setPreferredWidth(150); // Name
		getColumnModel().getColumn(1).setMinWidth(100);
		getColumnModel().getColumn(2).setPreferredWidth(280); // Description
		getColumnModel().getColumn(2).setMinWidth(150);
	}
	
	public void addPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem stockpilingMenuItem = new JMenuItem("Einlagern");
        stockpilingMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, DeviceModel.class.cast(getModel()).getObjectAtRow(getSelectedRow())));
        	}
        });
        popupMenu.add(stockpilingMenuItem);

        JMenuItem maximizeMenuItem = new JMenuItem("Delete");
        maximizeMenuItem.addActionListener((e) -> {
        		System.out.println("Deleting...");
        });
        popupMenu.add(maximizeMenuItem);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
}
