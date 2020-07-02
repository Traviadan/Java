package de.awi.catalog;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import de.awi.catalog.models.Device;
import de.awi.catalog.models.DeviceModel;
import de.traviadan.lib.db.Db;

public class DeviceTable extends JTable {
	private static final long serialVersionUID = 1L;

	public DeviceTable() {
		super();
		setRowSelectionAllowed(true);
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
		System.out.println(getColumnModel().getColumn(1).getHeaderValue());
		getColumnModel().getColumn(1).setPreferredWidth(200);
		getColumnModel().getColumn(1).setMinWidth(150);
	}
	
}
