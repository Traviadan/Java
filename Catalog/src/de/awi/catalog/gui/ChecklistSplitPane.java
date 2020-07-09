package de.awi.catalog.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.awi.catalog.events.StockpilingEvent;
import de.awi.catalog.interfaces.StockpilingListener;
import de.awi.catalog.models.Checklist;
import de.awi.catalog.models.ChecklistModel;
import de.awi.catalog.models.Device;
import de.traviadan.lib.db.Db;

public class ChecklistSplitPane extends EditTableSplitPane implements StockpilingListener {

	public static final int TXT_TESTNR = 0;
	public static final int TXT_DESCRIPTION = 1;

	private static final long serialVersionUID = 1L;
	private Device device;
	
	private ChecklistView checklistView;

	public ChecklistSplitPane(Db dataBase) {
		super(dataBase, JSplitPane.VERTICAL_SPLIT);
		init();
	}
	
	public void setDevice(Device d) {
		this.device = d;
	}
	
	private void init() {
		setDividerLocation(300);
		model = new ChecklistModel();
		table = new ChecklistTable();
		addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
					updateFields((Checklist)model.getObjectAtRow(table.getSelectedRow()));
				}
			}
		});
		panelTable.setLayout(new BorderLayout(5, 5));
		initTable(panelTable);
		add(panelTable);
		
		GridBagLayout gbl = new GridBagLayout();
		panelEdit.setLayout(gbl);

		/*
		for (int i=0; i < 2; i++) {
			txtFields.add(new JTextField());
		}
		createTextInput(panelEdit, gbl, "Name:", txtFields.get(TXT_TESTNR), 0, 0);
		createTextInput(panelEdit, gbl, "Beschreibung:", txtFields.get(TXT_DESCRIPTION), 0, 1);
		*/
		
		btnUpdate.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				if (device != null) {
					checklistView = new ChecklistView(getParent(), device);
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
							checklistView.setVisible(true);
						}
					});
				}
	        }  
	    });
		btnUpdate.setEnabled(false);
		
		btnReset.addActionListener(new ActionListener(){
			@Override
	    	public void actionPerformed(ActionEvent e){
				clearFields();
	        }  
	    });

		btnDelete.setVisible(false);
		addCommandButtons(panelEdit, gbl, 0);
		
		add(panelEdit);
	}
	
	private void updateFields(Checklist obj) {
		id = obj.getId();
		updateButtons();
	}

	public ListSelectionListener getListSelectionListener() {
		return new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (e.getSource() instanceof DefaultListSelectionModel) {
						if (((DefaultListSelectionModel)e.getSource()).getSelectedItemsCount() == 0) {
							btnUpdate.setEnabled(false);
						} else {
							btnUpdate.setEnabled(true);
						}
					}
				}
			}
		};
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
	
	@Override
	protected void updateButtons() {
		if (id == 0) {
			btnUpdate.setText("Insert");
		} else {
			btnUpdate.setText("View");
		}
	}


	@Override
	public void updateStorage(StockpilingEvent event) {
		// Empty
	}

}
