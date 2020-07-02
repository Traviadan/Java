package de.awi.catalog.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.awi.catalog.StorageUnitTable;
import de.awi.catalog.models.StorageUnit;
import de.awi.catalog.models.StorageUnitModel;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.helper.Check;

public class StorageUnitSplitPane extends EditTableSplitPane {

	private static final long serialVersionUID = 1L;

	private JTextField txtName = new JTextField();
	private JTextField txtDescription = new JTextField();
	private JTextField txtWidth = new JTextField();
	private JTextField txtLength = new JTextField();
	private JTextField txtHeight = new JTextField();
	private JTextField txtWeight = new JTextField();
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
		JPanel panelTable = new JPanel();
		panelTable.setLayout(new BorderLayout(5, 5));
		initTable(panelTable);
		add(panelTable);
		
		JPanel panelEdit = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		panelEdit.setLayout(gbl);

		createTextInput(panelEdit, gbl, "Name:", txtName, 0, 0);
		createTextInput(panelEdit, gbl, "Beschreibung:", txtDescription, 0, 1);
		createTextInput(panelEdit, gbl, "Breite:", txtWidth, 0, 2);
		createTextInput(panelEdit, gbl, "Länge:", txtLength, 0, 3);
		createTextInput(panelEdit, gbl, "Höhe:", txtHeight, 0, 4);
		createTextInput(panelEdit, gbl, "Gewicht:", txtWeight, 0, 5);
		
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
					obj.setName(txtName.getText());
					obj.setDescription(txtDescription.getText());
					obj.setWidth(Check.forInteger(txtWidth));
					obj.setLength(Check.forInteger(txtLength));
					obj.setHeight(Check.forInteger(txtHeight));
					obj.setWeight(Check.forInteger(txtWeight));

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
					model.populate(db);
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
					model.populate(db);
					model.fireTableDataChanged();
					clearFields();
				}
	        }  
	    });
		addCommandButtons(panelEdit, gbl);
		
		add(panelEdit);
	}
	
	public void clearFields() {
		table.getSelectionModel().clearSelection();
		id = 0;
		txtName.setText("");
		txtDescription.setText("");
		txtWidth.setText("");
		txtLength.setText("");
		txtHeight.setText("");
		txtWeight.setText("");
		cmbType.setSelectedIndex(0);
		updateButtons();
	}
	
	private void updateFields(StorageUnit obj) {
		id = obj.getId();
		txtName.setText(obj.getName());
		txtDescription.setText(obj.getDescription());
		txtWidth.setText(""+obj.getWidth());
		txtLength.setText(""+obj.getLength());
		txtHeight.setText(""+obj.getHeight());
		txtWeight.setText(""+obj.getWeight());
		cmbType.setSelectedIndex(obj.getType().ordinal());
		updateButtons();
	}

}
