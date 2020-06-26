package de.traviadan.lib.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DbTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	protected Class<?> thisClass;
	protected String tableName;
	protected Vector<String> columnNames = new Vector<>();
	protected Vector<Class<?>> columnTypes = new Vector<>();
	protected Vector<String> columnConstraints = new Vector<>();
	protected List<Object> data = new ArrayList<>();
	private Vector<Method> getterMethods = new Vector<>();
	
	public DbTableModel() {
		
	}
	public DbTableModel(Class<?> c) {
		thisClass = c;
		initTableName();
		initColumnNames();
	}
	
	public Vector<String> getColumnNames() {
		return columnNames;
	}
	public Vector<Class<?>> getColumnTypes() {
		return columnTypes;
	}
	protected void initTableName() {
		if (thisClass.isAnnotationPresent(DbTableName.class)) {
			tableName = thisClass.getAnnotation(DbTableName.class).name();
		}
	}
	protected void initColumnNames() {
		for (Method m: thisClass.getMethods()) {
			if (m.isAnnotationPresent(DbFieldGetter.class)) {
				String name = m.getAnnotation(DbFieldGetter.class).name();
				String constraint = m.getAnnotation(DbFieldGetter.class).constraint();
				columnNames.add(name);
				columnTypes.add(m.getReturnType());
				columnConstraints.add(constraint);
				getterMethods.add(m);
			}
		}
	}

	private Vector<Object> getValues(Object dataObj) {
		Vector<Object> values = new Vector<>();
		for (Method m: getterMethods) {
			try {
				Object value = m.invoke(dataObj);
				values.add(value);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return values;
	}
	public int insert(Db db, Object dataObj) {
		db.insert(tableName, columnNames, columnTypes, columnConstraints, getValues(dataObj));
		return db.selectLastId(tableName);
	}
	
	public void update(Db db, Object dataObj) {
		db.update(tableName, columnNames, columnTypes, columnConstraints, getValues(dataObj));
	}

	public void delete(Db db, Object dataObj) {
		if (columnConstraints.indexOf("PRIMARY KEY") != -1) {
			Method m = getterMethods.get(columnConstraints.indexOf("PRIMARY KEY"));
			try {
				int id = (int)m.invoke(dataObj);
				db.delete(tableName, id);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void createDbTable(Db db) {
		db.createTable(tableName, getColumnNames(), getColumnTypes());
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
