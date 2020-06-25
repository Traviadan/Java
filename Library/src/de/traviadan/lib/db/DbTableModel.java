package de.traviadan.lib.db;

import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DbTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	protected String tableName;
	protected Vector<String> columnNames = new Vector<>();
	protected Vector<Class<?>> columnTypes = new Vector<>();
	
	public DbTableModel() {
		
	}
	public DbTableModel(Class<?> c) {
		initTableName(c);
		initColumnNames(c);
	}
	
	public Vector<String> getColumnNames() {
		return columnNames;
	}
	public Vector<Class<?>> getColumnTypes() {
		return columnTypes;
	}
	protected void initTableName(Class<?> c) {
		if (c.isAnnotationPresent(DbTableName.class)) {
			tableName = c.getAnnotation(DbTableName.class).name();
		}
	}
	protected void initColumnNames(Class<?> c) {
		for (Method m: c.getMethods()) {
			if (m.isAnnotationPresent(DbFieldGetter.class)) {
				String name = m.getAnnotation(DbFieldGetter.class).name();
				columnNames.add(name);
				columnTypes.add(m.getReturnType());
			}
		}
	}

	protected void createDbTable() {
		
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
