package de.awi.catalog.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import de.awi.catalog.DeviceTable;
import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.Device;
import de.awi.catalog.models.DeviceModel;
import de.traviadan.lib.db.Db;

public class DeviceSplitPane extends EditTableSplitPane implements StockpilingListener {

	public static final int TXT_NAME = 0;
	public static final int TXT_DESCRIPTION = 1;
	public static final int TXT_MANUFACTURER = 2;
	public static final int TXT_UNITNR = 3;
	public static final int TXT_SERIALNR = 4;

	private static final long serialVersionUID = 1L;

	private JComboBox<Device.Type> cmbType = new JComboBox<Device.Type>();
	private JComboBox<Device.Protection> cmbProtection = new JComboBox<Device.Protection>();
	
	public DeviceSplitPane(Db dataBase) {
		super(dataBase, JSplitPane.VERTICAL_SPLIT);
		init();
	}
	
	private void init() {
		setDividerLocation(300);
		model = new DeviceModel();
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				clearFields();
			}
		});
		table = new DeviceTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
					//System.out.println(model.getObjectAtRow(table.getSelectedRow()));
					updateFields((Device)model.getObjectAtRow(table.getSelectedRow()));
				}
				
			}
		});
		panelTable.setLayout(new BorderLayout(5, 5));
		initTable(panelTable);
		add(panelTable);
		
		
		GridBagLayout gbl = new GridBagLayout();
		panelEdit.setLayout(gbl);

		for (int i=0; i < 5; i++) {
			txtFields.add(new JTextField());
		}
		createTextInput(panelEdit, gbl, "Ident-Nr.:", txtFields.get(TXT_UNITNR), 0, 0);
		createTextInput(panelEdit, gbl, "Name:", txtFields.get(TXT_NAME), 0, 1);
		createTextInput(panelEdit, gbl, "Beschreibung:", txtFields.get(TXT_DESCRIPTION), 0, 2);
		createTextInput(panelEdit, gbl, "Hersteller:", txtFields.get(TXT_MANUFACTURER), 0, 3);
		createTextInput(panelEdit, gbl, "Serien-Nr.:", txtFields.get(TXT_SERIALNR), 0, 4);
		
		for (Device.Type t: Device.Type.values()) {
			cmbType.addItem(t);
		}
		cmbType.addActionListener( new ActionListener() {
			  @Override public void actionPerformed( ActionEvent e )
			  {
				  JComboBox<?> selectedChoice = (JComboBox<?>) e.getSource();
				  if (selectedChoice.getSelectedItem() == null) return;
				  //System.out.println(selectedChoice.getSelectedItem().toString());
			  }
		});
		createComboBoxInput(panelEdit, gbl, "Gerätetyp:", cmbType, 0, 5);
		
		cmbProtection = new JComboBox<Device.Protection>(); // Auswahl für die Schutzklasse
		for (Device.Protection p: Device.Protection.values()) {
			cmbProtection.addItem(p);
		}
		cmbProtection.addActionListener( new ActionListener() {
			  @Override public void actionPerformed( ActionEvent e )
			  {
				  JComboBox<?> selectedChoice = (JComboBox<?>) e.getSource();
				  if (selectedChoice.getSelectedItem() == null) return;
				  //System.out.println(selectedChoice.getSelectedItem().toString());
			  }
		});
		createComboBoxInput(panelEdit, gbl, "Schutzklasse:", cmbProtection, 0, 6);
		
		btnUpdate.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				Device d = null;
				if (id == 0) {
					d = new Device();
				} else {
					if (table.getSelectedRow() >= 0) {
						d = (Device)model.getObjectAtRow(table.getSelectedRow());
					}
				}
				if (d != null) {
					d.setName(txtFields.get(TXT_NAME).getText());
					d.setDescription(txtFields.get(TXT_DESCRIPTION).getText());
					d.setManufacturer(txtFields.get(TXT_MANUFACTURER).getText());
					d.setUnitnr(txtFields.get(TXT_UNITNR).getText());
					d.setSerialnr(txtFields.get(TXT_SERIALNR).getText());
					if (cmbType.getSelectedItem() == null) {
						d.setDeviceType(Device.Type.NA);
					} else {
						d.setDeviceType((Device.Type)cmbType.getSelectedItem());
					}
					if (cmbProtection.getSelectedItem() == null) {
						d.setProtection(Device.Protection.NA);
					} else {
						d.setProtection((Device.Protection)cmbProtection.getSelectedItem());
					}
					
					if (id == 0) {
						int lastId = model.insert(db, d);
						d.setId(lastId);
					} else {
						model.update(db, d);
					}
					model.populate(db, true);
					model.fireTableDataChanged();
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
					model.populate(db, true);
					model.fireTableDataChanged();
				}
	        }  
	    });
		addCommandButtons(panelEdit, gbl);
		
		add(panelEdit);
		
		DeviceTable.class.cast(getTable()).addPopupMenu();
	}
	
	@Override
	public void clearFields() {
		cmbType.setSelectedIndex(0);
		cmbProtection.setSelectedIndex(0);
		super.clearFields();
	}
	
	private void updateFields(Device d) {
		id = d.getId();
		txtFields.get(TXT_NAME).setText(d.getName());
		txtFields.get(TXT_DESCRIPTION).setText(d.getDescription());
		txtFields.get(TXT_MANUFACTURER).setText(d.getManufacturer());
		txtFields.get(TXT_UNITNR).setText(d.getUnitnr());
		txtFields.get(TXT_SERIALNR).setText(d.getSerialnr());
		cmbType.setSelectedIndex(d.getDeviceType().ordinal());
		cmbProtection.setSelectedIndex(d.getProtection().ordinal());
		updateButtons();
	}

	@Override
	public void updateStorage(StockpilingEvent event) {
		
	}

}
