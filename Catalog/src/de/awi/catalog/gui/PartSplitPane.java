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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import de.awi.catalog.DeviceTable;
import de.awi.catalog.PartStorageUnitsTable;
import de.awi.catalog.PartTable;
import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.Device;
import de.awi.catalog.models.DeviceModel;
import de.awi.catalog.models.Material;
import de.awi.catalog.models.Part;
import de.awi.catalog.models.PartModel;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.helper.Check;

public class PartSplitPane extends EditTableSplitPane implements StockpilingListener {

	public static final int TXT_NAME = 0;
	public static final int TXT_DESCRIPTION = 1;
	public static final int TXT_MANUFACTURER = 2;
	public static final int TXT_SERIALNR = 3;
	public static final int TXT_WEIGHT = 4;
	public static final int TXT_PRICE = 5;

	private static final long serialVersionUID = 1L;

	private JComboBox<Material.Type> cmbType = new JComboBox<Material.Type>();
	PartStorageUnitSplitPane psuPane;
	JSplitPane splitEdit;
	
	public PartSplitPane(Db dataBase) {
		super(dataBase, JSplitPane.VERTICAL_SPLIT);
		init();
	}
	
	private void init() {
		model = new PartModel();
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				clearFields();
			}
		});
		table = new PartTable();
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
					Part p = (Part)model.getObjectAtRow(table.getSelectedRow());
					updateFields(p);
					psuPane.model.selectBy(db, Part.ID, p.getId());
					psuPane.model.fireTableDataChanged();
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
		createTextInput(panelEdit, gbl, "Hersteller:", txtFields.get(TXT_MANUFACTURER), 0, 2);
		createTextInput(panelEdit, gbl, "Serien-Nr.:", txtFields.get(TXT_SERIALNR), 0, 3);
		
		for (Material.Type t: Material.Type.values()) {
			cmbType.addItem(t);
		}
		cmbType.addActionListener( new ActionListener() {
			  @Override public void actionPerformed( ActionEvent e )
			  {
				  JComboBox<?> selectedChoice = (JComboBox<?>) e.getSource();
				  if (selectedChoice.getSelectedItem() == null) return;
			  }
		});
		createComboBoxInput(panelEdit, gbl, "Typ:", cmbType, 0, 4);
		
		createTextInput(panelEdit, gbl, "Gewicht:", txtFields.get(TXT_WEIGHT), 0, 5);
		createTextInput(panelEdit, gbl, "Preis:", txtFields.get(TXT_PRICE), 0, 6);
		
		btnUpdate.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				Part p = null;
				if (id == 0) {
					p = new Part();
				} else {
					if (table.getSelectedRow() >= 0) {
						p = (Part)model.getObjectAtRow(table.getSelectedRow());
					}
				}
				if (p != null) {
					p.setName(txtFields.get(TXT_NAME).getText());
					p.setDescription(txtFields.get(TXT_DESCRIPTION).getText());
					p.setManufacturer(txtFields.get(TXT_MANUFACTURER).getText());
					p.setSerialnr(txtFields.get(TXT_SERIALNR).getText());
					p.setWeight(Check.forDouble(txtFields.get(TXT_WEIGHT)));
					p.setPrice(Check.forDouble(txtFields.get(TXT_PRICE)));
					if (cmbType.getSelectedItem() == null) {
						p.setType(Material.Type.NA);
					} else {
						p.setType((Material.Type)cmbType.getSelectedItem());
					}
					
					if (id == 0) {
						int lastId = model.insert(db, p);
						p.setId(lastId);
					} else {
						model.update(db, p);
					}
					populateModel(false, "");
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
					populateModel(false, "");
					model.fireTableDataChanged();
				}
	        }  
	    });
		addCommandButtons(panelEdit, gbl, 10);
		
		splitEdit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitEdit.add(panelEdit);
		
		psuPane = new PartStorageUnitSplitPane(db);
		
		splitEdit.add(psuPane);
		
		add(splitEdit);
		//setDividerLocation(400);
		//splitEdit.setDividerLocation(700);
		
		PartTable.class.cast(getTable()).addPopupMenu();
	}
	
	public void setDividers() {
		setDividerLocation(0.6);
		splitEdit.setDividerLocation(0.7);
		psuPane.setDividers();
	}
	
	@Override
	public void initTable(JComponent cmp) {
		model.populate(db, false, false, "");
		super.initTable(cmp);
	}
	
	@Override
	public void clearFields() {
		cmbType.setSelectedIndex(0);
		psuPane.model.selectBy(db, Part.ID, 0);
		psuPane.model.fireTableDataChanged();
		super.clearFields();
	}
	
	private void updateFields(Part p) {
		id = p.getId();
		txtFields.get(TXT_NAME).setText(p.getName());
		txtFields.get(TXT_DESCRIPTION).setText(p.getDescription());
		txtFields.get(TXT_MANUFACTURER).setText(p.getManufacturer());
		txtFields.get(TXT_SERIALNR).setText(p.getSerialnr());
		txtFields.get(TXT_WEIGHT).setText(Double.toString(p.getWeight()));
		txtFields.get(TXT_PRICE).setText(Double.toString(p.getPrice()));
		cmbType.setSelectedIndex(p.getType().ordinal());
		updateButtons();
	}

	@Override
	public void updateStorage(StockpilingEvent event) {
		
	}

}
