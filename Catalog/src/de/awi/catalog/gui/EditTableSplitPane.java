package de.awi.catalog.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import de.awi.catalog.AbstractTable;
import de.traviadan.lib.db.Db;
import de.traviadan.lib.db.DbTableModel;

public class EditTableSplitPane extends JSplitPane {

	private static final long serialVersionUID = 1L;
	protected Db db;
	protected int id;
	protected DbTableModel model;
	protected AbstractTable table;
	protected List<JTextField> txtFields = new ArrayList<>();
	protected JButton btnUpdate = new JButton("Insert");
	protected JButton btnReset = new JButton("Reset");
	protected JButton btnDelete = new JButton("Delete");
	protected JScrollPane scrollPane;
	protected JPanel panelTable = new JPanel();
	protected JPanel panelEdit = new JPanel();
	
	public EditTableSplitPane(Db db, int split) {
		super(split);
		this.db = db;
		id = 0;
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public AbstractTable getTable() {
		return table;
	}
	
	public JPanel getPanelTable() {
		return panelTable;
	}
	
	public JPanel getPanelEdit() {
		return panelEdit;
	}

	protected void initTable(JComponent cmp) {
		scrollPane = new JScrollPane(table);
		cmp.add(scrollPane);
		table.setModel(model);
		table.setupColumns();
	}
	
	protected void populateModel(boolean recursive, String[] where) {
		if (model.joinedDataSize() > 0)	model.populate(db, true, recursive, where);
		else model.populate(db);
	}

	protected void createTextInput(JPanel panel, GridBagLayout gbl, String labelText, JTextField textField, int gridx, int gridy) {
		this.createTextInput(panel, gbl, labelText, textField, GridBagConstraints.NONE, gridx, gridy);
	}
	
	protected void createTextInput(
			JPanel panel, GridBagLayout gbl, String labelText, JTextField textField,
			int fill, int gridx, int gridy) {
		GridBagConstraints gbc;
		JLabel label = new JLabel(labelText); // Label für das Texteingabefeld
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = gridx; gbc.gridy = gridy;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(label, gbc);
		panel.add(label);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = gridx+1; gbc.gridy = gridy;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(textField, gbc);
		panel.add(textField);
	}

	protected void createComboBoxInput(JPanel panel, GridBagLayout gbl, String labelText, JComboBox<?> comboBox, int gridx, int gridy) {
		this.createComboBoxInput(panel, gbl, labelText, comboBox, GridBagConstraints.NONE, gridx, gridy);
	}
	
	protected void createComboBoxInput(
			JPanel panel, GridBagLayout gbl, String labelText, JComboBox<?> comboBox,
			int fill, int gridx, int gridy) {
		GridBagConstraints gbc;
		JLabel label = new JLabel(labelText); // Label für die ComboBox
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = gridx; gbc.gridy = gridy;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 0.5; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.EAST;
		gbl.setConstraints(label, gbc);
		panel.add(label);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = gridx+1; gbc.gridy = gridy;
		gbc.gridwidth = 1; gbc.gridheight = 1;
		gbc.weightx = 1.0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(comboBox, gbc);
		panel.add(comboBox);
	}
	
	protected void addCommandButtons(JPanel panel, GridBagLayout gbl, int gridy) {
		GridBagConstraints gbc;
		JPanel pnlButtons = new JPanel(); // Button Panel für die Kommandos
		pnlButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0; gbc.gridy = gridy;
		gbc.gridwidth = 2; gbc.gridheight = 1;
		gbc.weightx = 0; gbc.weighty = 0;
		gbc.ipadx = 10; gbc.ipady = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		gbl.setConstraints(pnlButtons, gbc);
		
		btnUpdate.setPreferredSize(new Dimension(150, 30));
		pnlButtons.add(btnUpdate);

		btnReset.setPreferredSize(new Dimension(150, 30));
		pnlButtons.add(btnReset);

		btnDelete.setPreferredSize(new Dimension(150, 30));
		btnDelete.setEnabled(false);
		
		pnlButtons.add(btnDelete);
		
		panel.add(pnlButtons);
	}
	
	protected void updateButtons() {
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
		for (JTextField t : txtFields) {
			t.setText("");
		}
		updateButtons();
	}


}
