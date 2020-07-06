package de.awi.catalog;

import java.util.Vector;

import de.awi.catalog.models.StorageLocation;
import de.awi.catalog.models.StorageLocationModel;

public class StorageLocationTable extends AbstractTable {
	private static final long serialVersionUID = 1L;

	public StorageLocationTable() {
		super();
		setRowSelectionAllowed(true);
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
	
}
