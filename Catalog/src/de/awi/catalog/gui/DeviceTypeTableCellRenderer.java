package de.awi.catalog.gui;

import javax.swing.table.DefaultTableCellRenderer;

import de.awi.catalog.models.Device;

public class DeviceTypeTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setValue(Object value) {
		if (value instanceof Integer) {
			setText(Device.Type.values()[(int)value].toString());
		} else {
			super.setValue(value);
		}
	}
}
