package de.traviadan.lib.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DbTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	protected Class<?> thisClass;
	protected String tableName;
	protected Map<String, Class<?>> columns = new LinkedHashMap<>();
	protected Map<String, String> constraints = new LinkedHashMap<>();
	protected Map<String, Method> getter = new LinkedHashMap<>();
	protected Map<String, Method> setter = new LinkedHashMap<>();
	protected List<Object> data = new ArrayList<>();
	
	public DbTableModel() {
		
	}
	public DbTableModel(Class<?> c) {
		thisClass = c;
		initTableName();
		initColumns();
	}
	
	public Vector<String> getColumnNames() {
		return new Vector<String>(columns.keySet());
	}
	
	public Vector<Class<?>> getColumnTypes() {
		return new Vector<Class<?>>(columns.values());
	}
	
	public void populate(Db db) {
		List<Map<String, Object>> rsData = db.selectAll(tableName, columns);
		data.clear();
		Constructor<?> c = null;
		try {
			c = thisClass.getConstructor();
		} catch (NoSuchMethodException | SecurityException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		for (Map<String, Object> eData: rsData) {
			Object obj = null;
			try {
				obj = c.newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (obj != null ) {
				for (Map.Entry<String, Class<?>> entry: columns.entrySet()) {
					if (entry.getValue().getSimpleName().equals("int")) {
						int value = (int)eData.get(entry.getKey());
						Method m = setter.get(entry.getKey());
						try {
							m.invoke(obj, value);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (entry.getValue().getSimpleName().equals("String")) {
						String value = (String)eData.get(entry.getKey());
						Method m = setter.get(entry.getKey());
						try {
							m.invoke(obj, value);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			data.add(obj);
		}
	}
	
	private void initColumns() {
		for (Method m: thisClass.getMethods()) {
			if (m.isAnnotationPresent(DbFieldGetter.class)) {
				String name = m.getAnnotation(DbFieldGetter.class).name();
				String constraint = m.getAnnotation(DbFieldGetter.class).constraint();
				columns.put(name, m.getReturnType());
				constraints.put(name, constraint);
				getter.put(name, m);
			} else if (m.isAnnotationPresent(DbFieldSetter.class)) {
				String name = m.getAnnotation(DbFieldSetter.class).name();
				setter.put(name, m);
			}
		}
	}
	protected void initTableName() {
		if (thisClass.isAnnotationPresent(DbTableName.class)) {
			tableName = thisClass.getAnnotation(DbTableName.class).name();
		}
	}

	private Object getValue(Object dataObj, int col) {
		Object value = null;
		Object[] gArray = getter.values().toArray();
		Method m = (Method)gArray[col];
		try {
			value = m.invoke(dataObj);
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
		return value;
	}
	
	private Map<String, Object> getValues(Object dataObj) {
		Map<String, Object> values = new HashMap<>();
		
		getter.forEach((name, m) -> {
			try {
				values.put(name, m.invoke(dataObj));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		return values;
	}
	public int insert(Db db, Object dataObj) {
		db.insert(tableName, columns, constraints, getValues(dataObj));
		return db.selectLastId(tableName);
	}
	
	public void update(Db db, Object dataObj) {
		db.update(tableName, columns, constraints, getValues(dataObj));
	}

	public void delete(Db db, Object dataObj) {
		if (constraints.containsValue("PRIMARY KEY")) {
			for (Map.Entry<String, String> entry: constraints.entrySet()) {
				if (entry.getValue().equals("PRIMARY KEY")) {
					Method m = getter.get(entry.getKey());
					try {
						db.delete(tableName, (int)m.invoke(dataObj));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
	public void createDbTable(Db db) {
		db.createTable(tableName, getColumnNames(), getColumnTypes());
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return getValue(data.get(rowIndex), columnIndex);
	}

}
