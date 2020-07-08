package de.awi.catalog;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.EventListenerList;

import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.gui.MaterialTypeTableCellRenderer;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.DeviceModel;
import de.awi.catalog.models.Material;
import de.awi.catalog.models.Part;
import de.awi.catalog.models.PartModel;

public class PartTable extends AbstractTable {
	private static final long serialVersionUID = 1L;
	private EventListenerList stockpilingListeners = new EventListenerList();
	
	public PartTable() {
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
		PartModel model = (PartModel)getModel();
		Vector<Boolean> vis = model.getColumnVisibilities();
		
		for (String name: model.getColumnNames()) {
			int col = model.findColumn(name);
			if (!vis.get(col)) {
				getColumnModel().getColumn(col).setPreferredWidth(0);
				getColumnModel().getColumn(col).setMinWidth(0);
				getColumnModel().getColumn(col).setMaxWidth(0);
			} else {
				if (name == Part.NAME) {
					getColumnModel().getColumn(col).setPreferredWidth(150); // Name
					getColumnModel().getColumn(col).setMinWidth(100);
				} else if (name == Part.DESCRIPTION) {
					getColumnModel().getColumn(col).setPreferredWidth(280); // Description
					getColumnModel().getColumn(col).setMinWidth(150);
				} else if (name == Material.TYPE) {
					getColumnModel().getColumn(col).setPreferredWidth(90); // Material Type
					getColumnModel().getColumn(col).setMinWidth(50);
					getColumnModel().getColumn(col).setCellRenderer(new MaterialTypeTableCellRenderer());
				}
			}
		}
	}
	
	public void addPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem stockpilingMenuItem = new JMenuItem("Einlagern");
        stockpilingMenuItem.addActionListener((e) -> {
        	if (getSelectedRow() >= 0) {
        		String amount = JOptionPane.showInputDialog(this, "Menge");
        		try {
        			double a = Double.parseDouble(amount);
        			notifyStockpiling(new StockpilingEvent(this, PartModel.class.cast(getModel()).getObjectAtRow(getSelectedRow()), a, false));
        		} catch (NullPointerException | NumberFormatException ex) {
        			return;
        		}
        		
        	}
        });
        popupMenu.add(stockpilingMenuItem);

        JMenuItem deleteMenuItem = new JMenuItem("L�schen");
        deleteMenuItem.addActionListener((e) -> {
        		System.out.println("Deleting...");
        });
        popupMenu.add(deleteMenuItem);

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
