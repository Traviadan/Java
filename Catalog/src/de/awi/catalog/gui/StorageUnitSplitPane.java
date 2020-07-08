package de.awi.catalog.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.awi.catalog.DeviceTable;
import de.awi.catalog.PartStorageUnitsTable;
import de.awi.catalog.PartTable;
import de.awi.catalog.StorageUnitTable;
import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.Device;
import de.awi.catalog.models.Part;
import de.awi.catalog.models.PartStorageUnits;
import de.awi.catalog.models.PartStorageUnitsModel;
import de.awi.catalog.models.StorageUnit;
import de.awi.catalog.models.StorageUnitModel;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;
import de.traviadan.lib.helper.Check;

public class StorageUnitSplitPane extends EditTableSplitPane implements StockpilingListener {
	public static final int TXT_NAME = 0;
	public static final int TXT_DESCRIPTION = 1;
	public static final int TXT_WIDTH = 2;
	public static final int TXT_LENGTH = 3;
	public static final int TXT_HEIGHT = 4;
	public static final int TXT_WEIGHT = 5;
	
	private static final long serialVersionUID = 1L;

	private JComboBox<StorageUnit.Type> cmbType = new JComboBox<>();
	
	public StorageUnitSplitPane(Db dataBase) {
		super(dataBase, JSplitPane.VERTICAL_SPLIT);
		init();
	}
	
	private void init() {
		setDividerLocation(300);
		model = new StorageUnitModel();
		table = new StorageUnitTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
					//System.out.println(model.getObjectAtRow(table.getSelectedRow()));
					updateFields((StorageUnit)model.getObjectAtRow(table.getSelectedRow()));
				}
				
			}
		});
		panelTable.setLayout(new BorderLayout(5, 5));
		initTable(panelTable);
		add(panelTable);
		
		GridBagLayout gbl = new GridBagLayout();
		panelEdit.setLayout(gbl);
		
		for (int i=0; i < 6; i++) {
			txtFields.add(new JTextField());
		}
		createTextInput(panelEdit, gbl, "Name:", txtFields.get(TXT_NAME), 0, 0);
		createTextInput(panelEdit, gbl, "Beschreibung:", txtFields.get(TXT_DESCRIPTION), 0, 1);
		createTextInput(panelEdit, gbl, "Länge:", txtFields.get(TXT_LENGTH), 0, 2);
		createTextInput(panelEdit, gbl, "Breite:", txtFields.get(TXT_WIDTH), 0, 3);
		createTextInput(panelEdit, gbl, "Höhe:", txtFields.get(TXT_HEIGHT), 0, 4);
		createTextInput(panelEdit, gbl, "Gewicht:", txtFields.get(TXT_WEIGHT), 0, 5);
		
		for (StorageUnit.Type t: StorageUnit.Type.values()) {
			cmbType.addItem(t);
		}
		cmbType.addActionListener( new ActionListener() {
			  @Override public void actionPerformed( ActionEvent e )
			  {
				  JComboBox<?> selectedChoice = (JComboBox<?>) e.getSource();
				  if (selectedChoice.getSelectedItem() == null) return;
			  }
		});
		createComboBoxInput(panelEdit, gbl, "Typ:", cmbType, 0, 6);

		btnUpdate.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				StorageUnit obj = null;
				if (id == 0) {
					obj = new StorageUnit();
				} else {
					if (table.getSelectedRow() >= 0) {
						obj = (StorageUnit)model.getObjectAtRow(table.getSelectedRow());
					}
				}
				if (obj != null) {
					obj.setName(txtFields.get(TXT_NAME).getText());
					obj.setDescription(txtFields.get(TXT_DESCRIPTION).getText());
					obj.setWidth(Check.forInteger(txtFields.get(TXT_WIDTH)));
					obj.setLength(Check.forInteger(txtFields.get(TXT_LENGTH)));
					obj.setHeight(Check.forInteger(txtFields.get(TXT_HEIGHT)));
					obj.setWeight(Check.forInteger(txtFields.get(TXT_WEIGHT)));

					if (cmbType.getSelectedItem() == null) {
						obj.setType(StorageUnit.Type.AluBox);
					} else {
						obj.setType((StorageUnit.Type)cmbType.getSelectedItem());
					}
					
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
		addCommandButtons(panelEdit, gbl, 7);
		
		add(panelEdit);
		
		StorageUnitTable.class.cast(getTable()).addPopupMenu();
	}
	
	@Override
	public void initTable(JComponent cmp) {
		model.populate(db, true, false, null);
		super.initTable(cmp);
	}

	@Override
	public void clearFields() {
		cmbType.setSelectedIndex(0);
		super.clearFields();
	}
	
	private void updateFields(StorageUnit obj) {
		id = obj.getId();
		txtFields.get(TXT_NAME).setText(obj.getName());
		txtFields.get(TXT_DESCRIPTION).setText(obj.getDescription());
		txtFields.get(TXT_WIDTH).setText(""+obj.getWidth());
		txtFields.get(TXT_LENGTH).setText(""+obj.getLength());
		txtFields.get(TXT_HEIGHT).setText(""+obj.getHeight());
		txtFields.get(TXT_WEIGHT).setText(""+obj.getWeight());
		cmbType.setSelectedIndex(obj.getType().ordinal());
		updateButtons();
	}

	@Override
	public void updateStorage(StockpilingEvent event) {
		if (event.getStockpilingObject() instanceof Device) {
			Device d = Device.class.cast(event.getStockpilingObject());
			if (event.isOutsourcing()) {
				d.setStorageunitid(0);
			} else if (getTable().getSelectedRow() >= 0) {
				StorageUnit storageUnit = StorageUnit.class.cast(model.getObjectAtRow(getTable().getSelectedRow()));
				d.setStorageunitid(storageUnit.getId());
			} else {
				return;
			}
			DeviceTable table = DeviceTable.class.cast(event.getSource());
			DbTableModel model = DbTableModel.class.cast(table.getModel());
			model.update(db, d);
			model.populate(db, true, false, null);
			model.fireTableDataChanged();
		} else if (event.getStockpilingObject() instanceof Part) {
			PartTable pt = PartTable.class.cast(event.getSource());
			Part p = Part.class.cast(event.getStockpilingObject());
			if (!event.isOutsourcing() && getTable().getSelectedRow() >= 0) {
				StorageUnit storageUnit = StorageUnit.class.cast(model.getObjectAtRow(getTable().getSelectedRow()));
				PartStorageUnitsModel psum = new PartStorageUnitsModel();
				psum.populate(db, true, false, new String[] { Part.ID, ""+p.getId(), StorageUnit.ID, ""+storageUnit.getId() });
				//psum.populate(db, true, false, String.format("WHERE %s = %s AND %s = %s", Part.ID, p.getId(), StorageUnit.ID, storageUnit.getId()));
				if (psum.getRowCount() > 0) {
					PartStorageUnits psu = (PartStorageUnits)psum.getObjectAtRow(0);
					psu.setAmount(event.getAmount());
					psum.update(db, psu);
				} else {
					PartStorageUnits psu = new PartStorageUnits();
					psu.setId(p.getId());
					psu.setStorageUnitId(storageUnit.getId());
					psu.setAmount(event.getAmount());
					psum.insert(db, psu);
				}
				int i = pt.getSelectedRow();
				pt.clearSelection();
				pt.setRowSelectionInterval(i, i);
			} else {
				return;
			}
		}
	}

}
