package de.traviadan.lib.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class DbTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	protected Class<?> thisClass;
	protected String tableName;
	protected Map<String, Map<String, Object>> properties = new LinkedHashMap<>();
	protected Map<String, Class<?>> columns = new LinkedHashMap<>();
	protected Map<String, Boolean> visibilities = new LinkedHashMap<>();
	protected Map<String, String> titles = new LinkedHashMap<>();
	protected Map<String, Class<?>> joins = new LinkedHashMap<>();
	protected Map<String, String> constraints = new LinkedHashMap<>();
	protected Map<String, Method> getter = new LinkedHashMap<>();
	protected Map<String, Method> setter = new LinkedHashMap<>();
	protected Map<String, Integer> order = new LinkedHashMap<>();
	protected List<Object> data = new ArrayList<>();
	protected Map<Class<?>, List<Map<String, Object>>> joinedData = new HashMap<>();
	
	protected String primaryField;
	
	public DbTableModel() {
	
	}

	public DbTableModel(Class<?> c) {
		thisClass = c;
		initTableName();
		initColumns();
	}

	public int joinedDataSize() {
		return joinedData.size();
	}
	
	public void selectBy(Db db, String field, Object where) {
		populate(db, true, true, String.format("WHERE %s = %s", field, where.toString()));
	}
	
	@Override
	public int findColumn(String name) {
		int found = -1;
		int idx = 0;
		for (String colName: properties.keySet()) {
			if (colName.equals(name)) {
				found = idx;
				break;
			}
			idx++;
		}
		return found;
	}
	
	@Override
	public String getColumnName(int column) {
		String columnName = (String)columns.keySet().toArray()[column];
		Map<String, Object> prop = properties.get(columnName);
		return (String)prop.get(Db.TITLE);
	}
	
	public Vector<String> getColumnNames() {
		Vector<String> cols = new Vector<>();
		for (Map.Entry<String, Map<String, Object>> colEntry: properties.entrySet()) {
			cols.add(colEntry.getKey());
		}
		return cols;
	}
	
	public Vector<Boolean> getColumnVisibilities() {
		Vector<Boolean> vis = new Vector<>();
		for (Map.Entry<String, Map<String, Object>> colEntry: properties.entrySet()) {
			vis.add((Boolean)colEntry.getValue().get(Db.VISIBILITY));
		}
		return vis;
	}

	public Vector<Class<?>> getColumnTypes() {
		Vector<Class<?>> types = new Vector<>();
		for (Map.Entry<String, Map<String, Object>> colEntry: properties.entrySet()) {
			types.add((Class<?>)colEntry.getValue().get(Db.TYPE));
		}
		return types;
	}

	public Vector<String> getColumnContraints() {
		Vector<String> constraints = new Vector<>();
		for (Map.Entry<String, Map<String, Object>> colEntry: properties.entrySet()) {
			constraints.add((String)colEntry.getValue().get(Db.CONSTRAINT));
		}
		return constraints;
	}

	public void populate(Db db, boolean join, boolean recursive, String where) {
		if (join && joins.size() > 0) {
			List<Map<String, Object>> rsData = db.leftJoin(tableName, columns, joins, recursive, where);
			for (Class<?> joinClass : joins.values()) {
				String joinTableName = Db.getTableName(joinClass);
				List<Map<String, Object>> l = new ArrayList<>(); 
				for (Map<String, Object> row : rsData) {
					Map<String, Object> jd = new HashMap<>();
					for (Map.Entry<String, Object> e : row.entrySet()) {
						if (e.getKey().startsWith(joinTableName+"_")) {
							jd.put(e.getKey(), e.getValue());
						}
					}
					l.add(jd);
				}
				joinedData.put(joinClass, l);
			}
			populate(rsData);
		} else {
			populate(db);
		}
	}
	
	public void populate(Db db) {
		List<Map<String, Object>> rsData = db.selectAll(tableName, columns);
		populate(rsData);
	}
	
	public void populate(List<Map<String, Object>> rsData) {
		data.clear();
		boolean joined = false;
		String colName = "";
		if (joinedData.size() > 0) {
			joined = true;
		}
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
					if (setter.containsKey(entry.getKey())) {
						if (joined) {
							colName = String.format("%s_%s", tableName, entry.getKey());
						} else {
							colName = entry.getKey();
						}
						if (entry.getValue().getSimpleName().equals("int")) {
							int value = (int)eData.get(colName);
							Method m = setter.get(entry.getKey());
							try {
								m.invoke(obj, value);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (entry.getValue().getSimpleName().equals("float")) {
							float value = (float)eData.get(colName);
							Method m = setter.get(entry.getKey());
							try {
								m.invoke(obj, value);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (entry.getValue().getSimpleName().equals("double")) {
							double value = (double)eData.get(colName);
							Method m = setter.get(entry.getKey());
							try {
								m.invoke(obj, value);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (entry.getValue().getSimpleName().equals("String")) {
							String value = (String)eData.get(colName);
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
			}
			data.add(obj);
		}
	}
	
	protected void initColumns() {
		for (Map.Entry<String, Map<String, Object>> colEntry: properties.entrySet()) {
			order.put(colEntry.getKey(), (Integer)colEntry.getValue().get(Db.ORDER));
		}
		Map<String, Map<String, Object>> rest = new LinkedHashMap<>();
		Map<String, Map<String, Object>> ordered = new LinkedHashMap<>();
		order.entrySet().stream()
    	.sorted(Map.Entry.comparingByValue())
    	.forEach(entry -> {
    		int o = entry.getValue();
    		String colName = entry.getKey();
    		if (o == 0) {
    			rest.put(colName, properties.get(colName));
    		} else {
    			ordered.put(colName, properties.get(colName));
    		}
    	} );
		properties = ordered;
		properties.putAll(rest);

		for (Map.Entry<String, Map<String, Object>> colEntry: properties.entrySet()) {
			if (((String)colEntry.getValue().get(Db.CONSTRAINT)).equals("PRIMARY KEY")) {
				primaryField = colEntry.getKey();
			}
			String name = colEntry.getKey();
			getter.put(name, (Method)colEntry.getValue().get(Db.GETTER));
			columns.put(name, (Class<?>)colEntry.getValue().get(Db.TYPE));
			titles.put(name, (String)colEntry.getValue().get(Db.TITLE));
			visibilities.put(name, (Boolean)colEntry.getValue().get(Db.VISIBILITY));
			constraints.put(name, (String)colEntry.getValue().get(Db.CONSTRAINT));
			if (colEntry.getValue().containsKey(Db.SETTER)) {
				setter.put(name, (Method)colEntry.getValue().get(Db.SETTER));
			}
		}
	}
	
	protected void initTableName() {
		if (thisClass.isAnnotationPresent(DbTableName.class)) {
			tableName = thisClass.getAnnotation(DbTableName.class).name();
		}
		if (thisClass.isAnnotationPresent(DbTableJoin.class)) {
			Class<?>[] joinClass = thisClass.getAnnotation(DbTableJoin.class).table();
			String[] nameUsing = thisClass.getAnnotation(DbTableJoin.class).using();
			for (int i = 0; i < joinClass.length; i++) {
				joins.put(nameUsing[i], joinClass[i]);
			}
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
		for (Map.Entry<String, Method> getterEntry: getter.entrySet()) {
			try {
				values.put(getterEntry.getKey(), getterEntry.getValue().invoke(dataObj));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				System.out.println("Fehler bei " + getterEntry.getKey());
				System.out.println("Fehler beim Auslesen der Werte: " + e.getMessage() + " " + e.getClass());
			}
		}
		return values;
	}
	public int insert(Db db, Object dataObj) {
		db.insert(tableName, columns, constraints, getValues(dataObj));
		return db.selectLastId(tableName, primaryField);
	}
	
	public void update(Db db, Object dataObj) {
		db.update(tableName, columns, constraints, getValues(dataObj), primaryField);
	}

	public void delete(Db db, Object dataObj) {
		if (constraints.containsValue("PRIMARY KEY")) {
			for (Map.Entry<String, String> entry: constraints.entrySet()) {
				if (entry.getValue().equals("PRIMARY KEY")) {
					Method m = getter.get(entry.getKey());
					try {
						db.delete(tableName, entry.getKey(), (int)m.invoke(dataObj));
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
		db.createTable(tableName, getColumnNames(), getColumnTypes(), getColumnContraints());
	}
	
	public Object getObjectAtRow(int rowIndex) {
		return data.get(rowIndex);
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
