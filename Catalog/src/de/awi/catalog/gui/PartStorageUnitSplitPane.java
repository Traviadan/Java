package de.awi.catalog.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.Part;
import de.awi.catalog.models.PartStorageUnits;
import de.awi.catalog.models.PartStorageUnitsModel;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.helper.Check;

public class PartStorageUnitSplitPane extends EditTableSplitPane implements StockpilingListener {
	public static final int TXT_AMOUNT = 0;
	
	private static final long serialVersionUID = 1L;

	public PartStorageUnitSplitPane(Db dataBase) {
		super(dataBase, JSplitPane.VERTICAL_SPLIT);
		init();
	}
	
	private void init() {
		model = new PartStorageUnitsModel();
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				clearFields();
			}
		});
		
		table = new PartStorageUnitsTable();
		((PartStorageUnitsTable)table).addStockpilingListener(this);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
					updateFields((PartStorageUnits)model.getObjectAtRow(table.getSelectedRow()));
				}
				
			}
		});
		panelTable.setLayout(new BorderLayout(5, 5));
		initTable(panelTable);
		model.selectBy(db, new String[] {Part.ID, ""+0});
		add(panelTable);
		
		GridBagLayout gbl = new GridBagLayout();
		panelEdit.setLayout(gbl);
		
		txtFields.add(new JTextField());
		createTextInput(panelEdit, gbl, "Menge:", txtFields.get(TXT_AMOUNT), 0, 0);
		
		add(panelEdit);
		
		PartStorageUnitsTable.class.cast(getTable()).addPopupMenu();
	}

	public void setDividers() {
		setDividerLocation(0.5);
	}
	
	@Override
	public void initTable(JComponent cmp) {
		model.populate(db, true, true, null);
		super.initTable(cmp);
	}

	@Override
	public void clearFields() {
		super.clearFields();
	}
	
	private void updateFields(PartStorageUnits obj) {
		id = obj.getId();
		txtFields.get(TXT_AMOUNT).setText("" + obj.getAmount());
	}

	@Override
	public void updateStorage(StockpilingEvent event) {
		if (event.getStockpilingObject() instanceof PartStorageUnits) {
			PartStorageUnits psu = PartStorageUnits.class.cast(event.getStockpilingObject());
			PartStorageUnitsModel psum = (PartStorageUnitsModel)model;
			if (event.isOutsourcing()) {
				psum.delete(db, psu);
				model.selectBy(db, new String[] {Part.ID, ""+0});
				model.fireTableDataChanged();
			} else if (getTable().getSelectedRow() >= 0) {
				psu.setAmount(Check.forDouble(txtFields.get(TXT_AMOUNT)));
				psum.update(db, psu);
				model.selectBy(db, new String[] {Part.ID, ""+psu.getId()});
				model.fireTableDataChanged();
			} else {
				return;
			}
		}
	}

}
