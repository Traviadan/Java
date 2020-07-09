package de.awi.catalog.gui;

import java.util.Vector;

import javax.swing.event.ListSelectionListener;

import de.awi.catalog.models.Checklist;
import de.awi.catalog.models.ChecklistModel;

public class ChecklistTable extends AbstractTable {
	private static final long serialVersionUID = 1L;

	public ChecklistTable() {
		super();
		setRowSelectionAllowed(true);
	}
	
	public void setupColumns() {
		ChecklistModel model = (ChecklistModel)getModel();
		Vector<Boolean> vis = model.getColumnVisibilities();
		
		for (String name: model.getColumnNames()) {
			int col = model.findColumn(name);
			if (!vis.get(col)) {
				getColumnModel().getColumn(col).setPreferredWidth(0);
				getColumnModel().getColumn(col).setMinWidth(0);
				getColumnModel().getColumn(col).setMaxWidth(0);
			} else {
				if (name == Checklist.TESTNR) {
					getColumnModel().getColumn(col).setPreferredWidth(80); // Test-Nr.
					getColumnModel().getColumn(col).setMinWidth(50);
					getColumnModel().getColumn(col).setMaxWidth(100);
				} else if (name == Checklist.DESCRIPTION) {
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
