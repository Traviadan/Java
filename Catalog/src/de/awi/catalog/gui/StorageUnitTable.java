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
import de.awi.catalog.models.StorageLocationModel;
import de.awi.catalog.models.StorageUnit;
import de.awi.catalog.models.StorageUnitModel;

public class StorageUnitTable extends AbstractTable {
	private static final long serialVersionUID = 1L;

	private EventListenerList stockpilingListeners = new EventListenerList();
	
	public StorageUnitTable() {
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
		StorageUnitModel model = (StorageUnitModel)getModel();
		Vector<Boolean> vis = model.getColumnVisibilities();
		
		for (String name: model.getColumnNames()) {
			int col = model.findColumn(name);
			if (!vis.get(col)) {
				getColumnModel().getColumn(col).setPreferredWidth(0);
				getColumnModel().getColumn(col).setMinWidth(0);
				getColumnModel().getColumn(col).setMaxWidth(0);
			} else {
				if (name == StorageUnit.NAME) {
					getColumnModel().getColumn(col).setPreferredWidth(150); // Name
					getColumnModel().getColumn(col).setMinWidth(100);
				} else if (name == StorageUnit.DESCRIPTION) {
					getColumnModel().getColumn(col).setPreferredWidth(280); // Description
					getColumnModel().getColumn(col).setMinWidth(150);
				} else if (name == StorageUnit.TYPE) {
					getColumnModel().getColumn(col).setPreferredWidth(100); // StorageUnit Type
					getColumnModel().getColumn(col).setMinWidth(70);
					getColumnModel().getColumn(col).setCellRenderer(new StorageUnitTypeTableCellRenderer());
				}
			}
		}
	}
	
	public void addPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem stockpilingMenuItem = new JMenuItem("Einlagern");
        stockpilingMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, StorageUnitModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), false));
        	}
        });
        popupMenu.add(stockpilingMenuItem);

        JMenuItem outsourceMenuItem = new JMenuItem("Auslagern");
        outsourceMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, StorageUnitModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), true));
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
