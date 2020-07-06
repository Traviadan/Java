package de.awi.catalog;

import java.util.Vector;

import de.awi.catalog.models.StorageUnit;
import de.awi.catalog.models.StorageUnitModel;

public class StorageUnitTable extends AbstractTable {
	private static final long serialVersionUID = 1L;

	public StorageUnitTable() {
		super();
		setRowSelectionAllowed(true);
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
				}
			}
		}
	}
	
}
