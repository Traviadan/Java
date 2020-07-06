package de.traviadan.lib.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Db {
	public static final String NAME = "name";
	public static final String CONSTRAINT = "constraint";
	public static final String TYPE = "type";
	public static final String TITLE = "title";
	public static final String VISIBILITY = "visibility";
	public static final String GETTER = "getter";
	public static final String SETTER = "setter";
	
	private String name;
	
	public Db() {
		this("noname.db");
	}
	public Db(String dbName) {
		if (!dbName.isBlank()) {
			name = dbName;
		} else {
			name = "sqlite.db";
		}
	}
	
	private Connection connect() throws SQLException {
		Connection conn = null;
		String url = "jdbc:sqlite:" + Paths.get( System.getProperty("user.dir"), name );
        // create a connection to the database
        conn = DriverManager.getConnection(url);
		return conn;
	}
	
	private void executeStatement(String sql) {
		try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
		}
	}

	public void createTable(String tableName, List<String> columns, List<Class<?>> types, List<String> constraints) {
		if (tableName.isBlank() || columns.size() == 0) return;
		
		// SQL statement for creating a new table
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		sb.append(tableName);
		sb.append(" (");

		Iterator<String> itColumns = columns.listIterator();
		Iterator<Class<?>> itTypes = types.listIterator();
		Iterator<String> itContraints = constraints.listIterator();
		while(itColumns.hasNext()) {
			String cName = itColumns.next();
			sb.append(cName).append(' ');
			Class<?> cTypeClass = itTypes.next();
			String cConstraint = itContraints.next();
			if (cTypeClass.getSimpleName().equals("int")) {
				sb.append("INTEGER");
			} else if (cTypeClass.getSimpleName().equals("String")) {
				sb.append("TEXT");
			} else {
				sb.append("NULL");
			}
			sb.append(" " + cConstraint);
			if (itColumns.hasNext()) sb.append(", ");
		}
		sb.append(");");
		
        executeStatement(sb.toString());
	}
	
	public void dropTable(String tableName) {
		if (tableName.isBlank()) return;
		
		String sql = String.format("DROP TABLE IF EXISTS %s", tableName);
		executeStatement(sql);
	}
	
	public int selectLastId(String tableName, String primaryField) {
		String sql = String.format("SELECT MAX(%s) FROM %s", primaryField, tableName);
        int maxId = 0;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
            	maxId = rs.getInt(String.format("MAX(%s)", primaryField));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return maxId;
	}

	private void appendColumns(Map<String, Class<?>> columns, String alias, StringBuilder sb) {
		Iterator<String> itColumns = columns.keySet().iterator();
		while(itColumns.hasNext()) {
			String colName = itColumns.next();
			if (!alias.isBlank()) {
				sb.append(String.format("%s.%s AS %s_%s", alias, colName, alias, colName));
			} else {
				sb.append(colName);
			}
			if (itColumns.hasNext()) {
				sb.append(", ");
			}
		}
	}
	private void appendColumns(Map<String, Class<?>> columns, StringBuilder sb) {
		appendColumns(columns, "", sb);
	}

	public static Map<String, Map<String, Object>> getColumnProperties(Class<?> c) {
		Map<String, Map<String, Object>> columns = new LinkedHashMap<>();
		for (Method m: c.getMethods()) {
			Map<String, Object> props = new HashMap<>();
			if (m.isAnnotationPresent(DbFieldGetter.class)) {
				String name = m.getAnnotation(DbFieldGetter.class).name();
				if (columns.containsKey(name)) props = columns.get(name);
				props.put(NAME, m.getAnnotation(DbFieldGetter.class).name());
				props.put(CONSTRAINT, m.getAnnotation(DbFieldGetter.class).constraint());
				props.put(TITLE, m.getAnnotation(DbFieldGetter.class).title());
				props.put(VISIBILITY, m.getAnnotation(DbFieldGetter.class).visibility());
				props.put(TYPE, m.getReturnType());
				props.put(GETTER, m);
				columns.put(m.getAnnotation(DbFieldGetter.class).name(), props);
			} else if (m.isAnnotationPresent(DbFieldSetter.class)) {
				String name = m.getAnnotation(DbFieldSetter.class).name();
				if (columns.containsKey(name)) props = columns.get(name);
				props.put(NAME, m.getAnnotation(DbFieldSetter.class).name());
				props.put(SETTER, m);
				columns.put(m.getAnnotation(DbFieldSetter.class).name(), props);
			}
		}
		return columns;
	}
	
	public static String getTableName(Class<?> c) {
		if (c.isAnnotationPresent(DbTableName.class)) {
			return c.getAnnotation(DbTableName.class).name();
		} else {
			return "";
		}
	}
	
	public List<Map<String, Object>> leftJoin(String tableName, Map<String, Class<?>> columns, Map<String, Class<?>> joins) {
		StringBuilder sb = new StringBuilder("Select ");
		String joinName = "";
		String using = "";
		appendColumns(columns, tableName, sb);
		for (Map.Entry<String, Class<?>> joinEntry: joins.entrySet()) {
			joinName = Db.getTableName(joinEntry.getValue());
			using = joinEntry.getKey();
			Map<String, Class<?>> joinColumns = new LinkedHashMap<>();
			for (Map.Entry<String, Map<String, Object>> propEntry: Db.getColumnProperties(joinEntry.getValue()).entrySet()) {
				joinColumns.put(propEntry.getKey(), (Class<?>)propEntry.getValue().get(Db.TYPE));
			}
			sb.append(", ");
			appendColumns(joinColumns, joinName, sb);
		}
		sb.append(String.format(" FROM %s ", tableName));
		sb.append(String.format("LEFT JOIN %s USING(%s)", joinName, using));
		
		System.out.println(sb.toString());
		List<Map<String, Object>> rsData = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sb.toString())){
        	
        	while (rs.next()) {
        		Map<String, Object> eData = new LinkedHashMap<>();
        		for (Map.Entry<String, Class<?>> entry: columns.entrySet()) {
        			String colName = String.format("%s_%s", tableName, entry.getKey());
					eData.put(colName, rs.getObject(colName));
				}
        		for (Map.Entry<String, Class<?>> joinEntry: joins.entrySet()) {
        			joinName = Db.getTableName(joinEntry.getValue());
        			Map<String, Class<?>> joinColumns = new LinkedHashMap<>();
        			for (Map.Entry<String, Map<String, Object>> propEntry: Db.getColumnProperties(joinEntry.getValue()).entrySet()) {
        				joinColumns.put(propEntry.getKey(), (Class<?>)propEntry.getValue().get(Db.TYPE));
        			}
            		for (Map.Entry<String, Class<?>> entry: joinColumns.entrySet()) {
            			String colName = String.format("%s_%s", joinName, entry.getKey());
    					eData.put(colName, rs.getObject(colName));
    				}
        		}
        		rsData.add(eData);
			}
        	System.out.println("rsData:" + rsData.get(0));
            return rsData;
        } catch (SQLException e) {
            System.out.println("LeftJoin Exception: " + e.getMessage());
        }
        return null;
	}
	
	public List<Map<String, Object>> selectAll(String tableName, Map<String, Class<?>> columns) {
		StringBuilder sb = new StringBuilder("Select ");
		appendColumns(columns, sb);
		sb.append(String.format(" FROM %s ", tableName));
		
		List<Map<String, Object>> rsData = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sb.toString())){
        	
        	while (rs.next()) {
        		Map<String, Object> eData = new LinkedHashMap<>();
        		for (Map.Entry<String, Class<?>> entry: columns.entrySet()) {
					eData.put(entry.getKey(), rs.getObject(entry.getKey()));
				}
        		rsData.add(eData);
			}
            return rsData;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
	}
	
	public void insert(String tableName, Map<String, Class<?>> columns, Map<String, String> constraints, Map<String, Object> values) {
		StringBuilder sb = new StringBuilder(
				String.format("INSERT INTO %s (", tableName));
		StringBuilder sbValues = new StringBuilder(" VALUES(");
		Iterator<String> itColumns = columns.keySet().iterator();
		while(itColumns.hasNext()) {
			sb.append(itColumns.next());
			sbValues.append('?');
			if (itColumns.hasNext()) {
				sb.append(", ");
				sbValues.append(", ");
			}
		}
		sbValues.append(')');
		sb.append(") ").append(sbValues.toString());
		
		try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sb.toString())) {
			int idx = 0;
			for (Map.Entry<String, Class<?>> entry: columns.entrySet()) {
				idx++;
				String constraint = constraints.get(entry.getKey());
				Object val = values.get(entry.getKey());
				if (entry.getValue().getSimpleName().equals("int")) {
					int iVal = (int)val;
					if(constraint != null && constraint.equals("PRIMARY KEY") && iVal == 0) {
						iVal = selectLastId(tableName, entry.getKey()) + 1;
					}
					pstmt.setInt(idx, iVal);
				} else if (entry.getValue().getSimpleName().equals("String")) {
					pstmt.setString(idx, (String)val);
				} else {
					pstmt.setNull(idx, java.sql.Types.NULL);
				}
			}
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
		}
	}
	
	public void update(String tableName, Map<String, Class<?>> columns, Map<String, String> constraints, Map<String, Object> values, String primaryField) {
		StringBuilder sb = new StringBuilder(
				String.format("UPDATE %s SET", tableName));
		String where = String.format(" WHERE %s = ?", primaryField);
		
		Iterator<String> itColumns = columns.keySet().iterator();
		while (itColumns.hasNext()) {
    		String column = itColumns.next();
			String constraint = constraints.get(column);
    		if(constraint == null || !constraint.equals("PRIMARY KEY")) {
        		sb.append(String.format(" %s = ?", column));
    			if (itColumns.hasNext()) {
    				sb.append(",");
    			}
    		} else {
    			continue;
    		}
		}
		sb.append(where);
		
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sb.toString())) {

        	int idx = 0;
        	int id = 0;
        	for (Map.Entry<String, Class<?>> entry: columns.entrySet()) {
        		Class<?> cTypeClass = entry.getValue();
				Object val = values.get(entry.getKey());
				String constraint = constraints.get(entry.getKey());
				if (cTypeClass.getSimpleName().equals("int")) {
					if(constraint == null || !constraint.equals("PRIMARY KEY")) {
		        		idx++;
						pstmt.setInt(idx, (int)val);
					} else if (constraint != null && constraint.equals("PRIMARY KEY")) {
						id = (int)val;
					}
				} else if (cTypeClass.getSimpleName().equals("String")) {
	        		idx++;
					pstmt.setString(idx, (String)val);
				} else {
	        		idx++;
					pstmt.setNull(idx, java.sql.Types.NULL);
				}
        	}
        	idx++;
        	pstmt.setInt(idx, id);
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void delete(String tableName, String primaryField, int id) {
		String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName, primaryField);

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }	
	}
}
