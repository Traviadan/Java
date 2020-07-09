package de.awi.catalog.gui;

import java.util.Vector;

import javax.swing.event.ListSelectionListener;

import de.awi.catalog.models.Storage;
import de.awi.catalog.models.StorageModel;

public class StorageTable extends AbstractTable {
	private static final long serialVersionUID = 1L;

	public StorageTable() {
		super();
		setRowSelectionAllowed(true);
	}
	
	public void setupColumns() {
		StorageModel model = (StorageModel)getModel();
		Vector<Boolean> vis = model.getColumnVisibilities();
		
		for (String name: model.getColumnNames()) {
			int col = model.findColumn(name);
			if (!vis.get(col)) {
				getColumnModel().getColumn(col).setPreferredWidth(0);
				getColumnModel().getColumn(col).setMinWidth(0);
				getColumnModel().getColumn(col).setMaxWidth(0);
			} else {
				if (name == Storage.NAME) {
					getColumnModel().getColumn(col).setPreferredWidth(150); // Name
					getColumnModel().getColumn(col).setMinWidth(100);
					getColumnModel().getColumn(col).setMaxWidth(200);
				} else if (name == Storage.DESCRIPTION) {
					getColumnModel().getColumn(col).setPreferredWidth(280); // Description
					getColumnModel().getColumn(col).setMinWidth(150);
				}
			}
		}
	}

	@Override
	public ListSelectionListener getListSelectionListener() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
