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

	private static final long serialVersionUID = 1L;

	private JTextField txtName = new JTextField();
	private JTextField txtDescription = new JTextField();
	private JTextField txtManufacturer = new JTextField();
	private JTextField txtUnitNr = new JTextField();
	private JTextField txtSerialNr = new JTextField();
	private JComboBox<Device.Type> cmbType = new JComboBox<Device.Type>();
	private JComboBox<Device.Protection> cmbProtection = new JComboBox<Device.Protection>();
	private JPanel panelTable = new JPanel();
	private JPanel panelEdit = new JPanel();
	

	
	public DeviceSplitPane(Db dataBase) {
		super(dataBase, JSplitPane.VERTICAL_SPLIT);
		init();
	}
	
	
	
	public JPanel getPanelTable() {
		return panelTable;
	}
	
	public JPanel getPanelEdit() {
		return panelEdit;
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

		createTextInput(panelEdit, gbl, "Name:", txtName, 0, 0);
		createTextInput(panelEdit, gbl, "Beschreibung:", txtDescription, 0, 1);
		createTextInput(panelEdit, gbl, "Hersteller:", txtManufacturer, 0, 2);
		createTextInput(panelEdit, gbl, "Inventar-Nr.:", txtUnitNr, 0, 3);
		createTextInput(panelEdit, gbl, "Serien-Nr.:", txtSerialNr, 0, 4);
		
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
					d.setName(txtName.getText());
					d.setDescription(txtDescription.getText());
					d.setManufacturer(txtManufacturer.getText());
					d.setUnitnr(txtUnitNr.getText());
					d.setSerialnr(txtSerialNr.getText());
					if (cmbType.getSelectedItem() == null) {
						d.setType(Device.Type.NA);
					} else {
						d.setType((Device.Type)cmbType.getSelectedItem());
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
	protected void initTable(JComponent cmp) {
		scrollPane = new JScrollPane(table);
		cmp.add(scrollPane);
		model.populate(db, true);
		table.setModel(model);
		table.setupColumns();
	}
	
	public void clearFields() {
		table.getSelectionModel().clearSelection();
		id = 0;
		txtName.setText("");
		txtDescription.setText("");
		txtManufacturer.setText("");
		txtUnitNr.setText("");
		txtSerialNr.setText("");
		cmbType.setSelectedIndex(0);
		cmbProtection.setSelectedIndex(0);
		updateButtons();
	}
	
	private void updateFields(Device d) {
		id = d.getId();
		txtName.setText(d.getName());
		txtDescription.setText(d.getDescription());
		txtManufacturer.setText(d.getManufacturer());
		txtUnitNr.setText(d.getUnitnr());
		txtSerialNr.setText(d.getSerialnr());
		cmbType.setSelectedIndex(d.getType().ordinal());
		cmbProtection.setSelectedIndex(d.getProtection().ordinal());
		updateButtons();
	}



	@Override
	public void updateStorage(StockpilingEvent event) {
		
	}

}
