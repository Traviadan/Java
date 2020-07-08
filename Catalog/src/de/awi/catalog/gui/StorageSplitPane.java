package de.awi.catalog.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.awi.catalog.StorageLocationTable;
import de.awi.catalog.StorageTable;
import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.Storage;
import de.awi.catalog.models.StorageLocation;
import de.awi.catalog.models.StorageModel;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class StorageSplitPane extends EditTableSplitPane implements StockpilingListener {

	public static final int TXT_NAME = 0;
	public static final int TXT_DESCRIPTION = 1;

	private static final long serialVersionUID = 1L;

	public StorageSplitPane(Db dataBase) {
		super(dataBase, JSplitPane.VERTICAL_SPLIT);
		init();
	}
	
	private void init() {
		setDividerLocation(300);
		model = new StorageModel();
		table = new StorageTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
					updateFields((Storage)model.getObjectAtRow(table.getSelectedRow()));
				}
				
			}
		});
		panelTable.setLayout(new BorderLayout(5, 5));
		initTable(panelTable);
		add(panelTable);
		
		GridBagLayout gbl = new GridBagLayout();
		panelEdit.setLayout(gbl);

		for (int i=0; i < 2; i++) {
			txtFields.add(new JTextField());
		}
		createTextInput(panelEdit, gbl, "Name:", txtFields.get(TXT_NAME), 0, 0);
		createTextInput(panelEdit, gbl, "Beschreibung:", txtFields.get(TXT_DESCRIPTION), 0, 1);
		
		btnUpdate.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				Storage obj = null;
				if (id == 0) {
					obj = new Storage();
				} else {
					if (table.getSelectedRow() >= 0) {
						obj = (Storage)model.getObjectAtRow(table.getSelectedRow());
					}
				}
				if (obj != null) {
					obj.setName(txtFields.get(TXT_NAME).getText());
					obj.setDescription(txtFields.get(TXT_DESCRIPTION).getText());
					
					if (id == 0) {
						int lastId = model.insert(db, obj);
						obj.setId(lastId);
					} else {
						model.update(db, obj);
					}
					populateModel(false, null);
					model.fireTableDataChanged();
					clearFields();
				}
	        }  
	    });
		btnReset.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				clearFields();
	        }  
	    });
		btnDelete.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				if (id == 0 || table.getSelectedRow() < 0) return;
				int choice = JOptionPane.showOptionDialog(
						getParent(),
						"Den Datenbankeintrag unwiderruflich löschen?",
						"Löschen?",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null, null, null);
				if(choice == JOptionPane.YES_OPTION) {
					model.delete(db, model.getObjectAtRow(table.getSelectedRow()));
					populateModel(false, null);
					model.fireTableDataChanged();
					clearFields();
				}
	        }  
	    });
		addCommandButtons(panelEdit, gbl, 2);
		
		add(panelEdit);
	}
	
	@Override
	public void initTable(JComponent cmp) {
		model.populate(db);
		super.initTable(cmp);
	}

	@Override
	public void clearFields() {
		super.clearFields();
	}
	
	private void updateFields(Storage obj) {
		id = obj.getId();
		txtFields.get(TXT_NAME).setText(obj.getName());
		txtFields.get(TXT_DESCRIPTION).setText(obj.getDescription());
		updateButtons();
	}

	@Override
	public void updateStorage(StockpilingEvent event) {
		if (event.getStockpilingObject() instanceof StorageLocation) {
			StorageLocation sl = StorageLocation.class.cast(event.getStockpilingObject());
			if (event.isOutsourcing()) {
				sl.setStorageid(0);
			} else if (getTable().getSelectedRow() >= 0) {
				Storage storage = Storage.class.cast(model.getObjectAtRow(getTable().getSelectedRow()));
				sl.setStorageid(storage.getId());
			} else {
				return;
			}
			StorageLocationTable table = StorageLocationTable.class.cast(event.getSource());
			DbTableModel model = DbTableModel.class.cast(table.getModel());
			model.update(db, sl);
			model.populate(db, true, false, null);
			model.fireTableDataChanged();
		}
	}

}
