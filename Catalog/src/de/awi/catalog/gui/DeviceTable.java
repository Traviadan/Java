package de.awi.catalog.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.Device;
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
			} else {
				if (name == Device.NAME) {
					getColumnModel().getColumn(col).setPreferredWidth(150); // Name
					getColumnModel().getColumn(col).setMinWidth(100);
				} else if (name == Device.DESCRIPTION) {
					getColumnModel().getColumn(col).setPreferredWidth(280); // Description
					getColumnModel().getColumn(col).setMinWidth(150);
				} else if (name == Device.UNITNR) {
					getColumnModel().getColumn(col).setPreferredWidth(80); // Identnr
					getColumnModel().getColumn(col).setMinWidth(50);
				} else if (name == Device.TYPE) {
					getColumnModel().getColumn(col).setPreferredWidth(90); // Device Type
					getColumnModel().getColumn(col).setMinWidth(50);
					getColumnModel().getColumn(col).setCellRenderer(new DeviceTypeTableCellRenderer());
				} else if (name == Device.PROTECTION) {
					getColumnModel().getColumn(col).setPreferredWidth(120); // Protection
					getColumnModel().getColumn(col).setMinWidth(70);
					getColumnModel().getColumn(col).setCellRenderer(new DeviceProtectionTableCellRenderer());
				}
			}
		}
	}
	
	@Override
	public ListSelectionListener getListSelectionListener() {
		return new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (e.getSource() instanceof DefaultListSelectionModel) {
						if (((DefaultListSelectionModel)e.getSource()).getSelectedItemsCount() == 0) {
							stockpilingMenuItem.setEnabled(false);
						} else {
							stockpilingMenuItem.setEnabled(true);
						}
					}
				}
			}
		};
	}
	
	public void addPopupMenu() {
        popupMenu = new JPopupMenu();
        
        stockpilingMenuItem = new JMenuItem("Einpacken");
        stockpilingMenuItem.setToolTipText("Das Gerät in die asugewählte Lagereinheit einpacken");
        stockpilingMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, DeviceModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), false));
        	}
        });
        stockpilingMenuItem.setEnabled(false);
        popupMenu.add(stockpilingMenuItem);

        outsourceMenuItem = new JMenuItem("Auspacken");
        outsourceMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		notifyStockpiling(new StockpilingEvent(this, DeviceModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), true));
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
}
