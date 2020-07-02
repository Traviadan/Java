package de.awi.catalog.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
import de.awi.catalog.models.Device;
import de.awi.catalog.models.DeviceModel;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.gui.WindowFrame;

public class DeviceSplitPane extends JSplitPane {

	private static final long serialVersionUID = 1L;

	private Db db;
	private int id;
	private DeviceModel model;
	private DeviceTable table;
	private JTextField txtName;
	private JTextField txtDescription;
	private JTextField txtManufacturer;
	private JTextField txtUnitNr;
	private JTextField txtSerialNr;
	private JComboBox<Device.Type> cmbType;
	private JComboBox<Device.Protection> cmbProtection;
	private JButton btnUpdate;
	private JButton btnReset;
	private JButton btnDelete;
	
	public DeviceSplitPane(Db dataBase) {
		super(VERTICAL_SPLIT);
		db = dataBase;
		id = 0;
		init();
	}
	
	private void init() {
		setDividerLocation(300);
		model = new DeviceModel();
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
		JPanel panelTable = new JPanel();
		panelTable.setLayout(new BorderLayout(5, 5));
		initTableDevice(panelTable);
		add(panelTable);
		
		JPanel panelEdit = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		panelEdit.setLayout(gbl);
		GridBagConstraints gbc;

		JLabel lblName = new JLabel("Name:"); // Bezeichner für Textfeld Name
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(lblName, gbc);
		panelEdit.add(lblName);
		
		txtName = new JTextField(); // Textfeld für Name 
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(txtName, gbc);
		panelEdit.add(txtName);
		
		JLabel lblDescription = new JLabel("Beschreibung:"); // Bezeichner für Textfeld Beschreibung
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0; gbc.gridy = 1;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(lblDescription, gbc);
		panelEdit.add(lblDescription);
		
		txtDescription = new JTextField(); // Textfeld für Beschreibung
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(txtDescription, gbc);
		panelEdit.add(txtDescription);
		
		JLabel lblManufacturer = new JLabel("Hersteller:"); // Bezeichner für Textfeld Hersteller
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0; gbc.gridy = 2;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(lblManufacturer, gbc);
		panelEdit.add(lblManufacturer);
		
		txtManufacturer = new JTextField(); // Textfeld für Hersteller
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(txtManufacturer, gbc);
		panelEdit.add(txtManufacturer);
		add(panelEdit);

		JLabel lblUnitNr = new JLabel("Inventar-Nr.:"); // Bezeichner für Textfeld Inventar-Nr.
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0; gbc.gridy = 3;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(lblUnitNr, gbc);
		panelEdit.add(lblUnitNr);
		
		txtUnitNr = new JTextField(); // Textfeld für Inventar-Nr.
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1; gbc.gridy = 3;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(txtUnitNr, gbc);
		panelEdit.add(txtUnitNr);

		JLabel lblSerialNr = new JLabel("Serien-Nr.:"); // Bezeichner für Textfeld Serien-Nr.
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0; gbc.gridy = 4;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(lblSerialNr, gbc);
		panelEdit.add(lblSerialNr);
		
		txtSerialNr = new JTextField(); // Textfeld für Serien-Nr.
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1; gbc.gridy = 4;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(txtSerialNr, gbc);
		panelEdit.add(txtSerialNr);
		
		JLabel lblType = new JLabel("Gerätetyp:"); // Bezeichner für Gerätetyp Auswahl
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0; gbc.gridy = 5;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(lblType, gbc);
		panelEdit.add(lblType);

		cmbType = new JComboBox<Device.Type>(); // Auswahl für den Gerätetyp
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
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 1; gbc.gridy = 5;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(cmbType, gbc);
		panelEdit.add(cmbType);
		
		JLabel lblProtection = new JLabel("Schutzklasse:"); // Bezeichner für Schutzklasse Auswahl
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 0; gbc.gridy = 6;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(lblProtection, gbc);
		panelEdit.add(lblProtection);

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
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 1; gbc.gridy = 6;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(cmbProtection, gbc);
		panelEdit.add(cmbProtection);

		JPanel pnlButtons = new JPanel(); // Button Panel für die Kommandos
		pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0; gbc.gridy = 7;
		gbc.gridwidth = 2; gbc.gridheight = 1;
		gbc.weightx = 0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(pnlButtons, gbc);
		
		btnUpdate = new JButton("Insert");
		btnUpdate.setPreferredSize(new Dimension(150, 30));
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
					model.populate(db);
					model.fireTableDataChanged();
				}
	        }  
	    });
		pnlButtons.add(btnUpdate);

		btnReset = new JButton("Reset");
		btnReset.setPreferredSize(new Dimension(150, 30));
		btnReset.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				clearFields();
	        }  
	    });
		pnlButtons.add(btnReset);

		btnDelete = new JButton("Delete");
		btnDelete.setPreferredSize(new Dimension(150, 30));
		btnDelete.setEnabled(false);
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
					model.populate(db);
					model.fireTableDataChanged();
					clearFields();
				}
	        }  
	    });
		pnlButtons.add(btnDelete);
		
		panelEdit.add(pnlButtons);
		
	}
	
	private void initTableDevice(JComponent cmp) {
		/*
		String[][] rowData = {
				{ "Japan", "245" }, { "USA", "240" }, { "Italien", "220" },
			    { "Spanien", "217" }, {"Türkei", "215"} ,{ "England", "214" },
			    { "Frankreich", "190" }, {"Griechenland", "185" },
			    { "Deutschland", "180" }, {"Portugal", "170" }
		};

		String[] columnNames = {
				"Land", "Durchschnittliche Fernsehdauer pro Tag in Minuten"
		};

		JTable table = new JTable(rowData, columnNames);
		*/
		JScrollPane scrollPane = new JScrollPane(table);
		//scrollPane.setPreferredSize(new Dimension(500, 500));
		cmp.add(scrollPane);
		
		model.populate(db);
		table.setModel(model);
		table.setupColumns();
	}
	
	public void updateButtons() {
		if (id == 0) {
			btnUpdate.setText("Insert");
			btnDelete.setEnabled(false);
		} else {
			btnUpdate.setText("Update");
			btnDelete.setEnabled(true);
		}
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

}
