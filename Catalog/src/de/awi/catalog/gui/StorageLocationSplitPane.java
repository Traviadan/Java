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
import de.awi.catalog.StorageUnitTable;
import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.StorageLocation;
import de.awi.catalog.models.StorageLocationModel;
import de.awi.catalog.models.StorageUnit;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class StorageLocationSplitPane extends EditTableSplitPane implements StockpilingListener {

	public static final int TXT_NAME = 0;
	public static final int TXT_DESCRIPTION = 1;

	private static final long serialVersionUID = 1L;

	public StorageLocationSplitPane(Db dataBase) {
		super(dataBase, JSplitPane.VERTICAL_SPLIT);
		init();
	}
	
	private void init() {
		setDividerLocation(300);
		model = new StorageLocationModel();
		table = new StorageLocationTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
					updateFields((StorageLocation)model.getObjectAtRow(table.getSelectedRow()));
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
				StorageLocation obj = null;
				if (id == 0) {
					obj = new StorageLocation();
				} else {
					if (table.getSelectedRow() >= 0) {
						obj = (StorageLocation)model.getObjectAtRow(table.getSelectedRow());
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
					populateModel(false, "");
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
					populateModel(false, "");
					model.fireTableDataChanged();
					clearFields();
				}
	        }  
	    });
		addCommandButtons(panelEdit, gbl, 2);
		
		add(panelEdit);
		
		StorageLocationTable.class.cast(getTable()).addPopupMenu();
	}
	
	@Override
	public void initTable(JComponent cmp) {
		model.populate(db, true, false, "");
		super.initTable(cmp);
	}

	@Override
	public void clearFields() {
		super.clearFields();
	}
	
	private void updateFields(StorageLocation obj) {
		id = obj.getId();
		txtFields.get(TXT_NAME).setText(obj.getName());
		txtFields.get(TXT_DESCRIPTION).setText(obj.getDescription());
		updateButtons();
	}

	@Override
	public void updateStorage(StockpilingEvent event) {
		if (event.getStockpilingObject() instanceof StorageUnit) {
			StorageUnit su = StorageUnit.class.cast(event.getStockpilingObject());
			if (event.isOutsourcing()) {
				su.setStoragelocationid(0);
			} else if (getTable().getSelectedRow() >= 0) {
				StorageLocation storageLocation = StorageLocation.class.cast(model.getObjectAtRow(getTable().getSelectedRow()));
				su.setStoragelocationid(storageLocation.getId());
			} else {
				return;
			}
			StorageUnitTable table = StorageUnitTable.class.cast(event.getSource());
			DbTableModel model = DbTableModel.class.cast(table.getModel());
			model.update(db, su);
			model.populate(db, true, false, "");
			System.out.println(model.joinedDataSize());
			model.fireTableDataChanged();
		}
	}

}
