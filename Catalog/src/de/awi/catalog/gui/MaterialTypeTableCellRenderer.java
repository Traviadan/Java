package de.awi.catalog.gui;

import javax.swing.table.DefaultTableCellRenderer;

import de.awi.catalog.models.Material;

public class MaterialTypeTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setValue(Object value) {
		if (value instanceof Integer) {
			setText(Material.Type.values()[(int)value].toString());
		} else {
			super.setValue(value);
		}
	}
}
