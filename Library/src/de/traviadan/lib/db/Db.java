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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Db {
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

	public void createTable(String tableName, List<String> columns, List<Class<?>> types) {
		if (tableName.isBlank() || columns.size() == 0) return;
		
		// SQL statement for creating a new table
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		sb.append(tableName);
		sb.append(" (");

		Iterator<String> itColumns = columns.listIterator();
		Iterator<Class<?>> itTypes = types.listIterator();
		while(itColumns.hasNext()) {
			String cName = itColumns.next();
			sb.append(cName).append(' ');
			Class<?> cTypeClass = itTypes.next();
			if (cTypeClass.getSimpleName().equals("int")) {
				sb.append("INTEGER");
				if (cName.equals("id")) {
					sb.append(" PRIMARY KEY");
				}
			} else if (cTypeClass.getSimpleName().equals("String")) {
				sb.append("TEXT");
			} else {
				sb.append("NULL");
			}
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
	
	public int selectLastId(String tableName) {
		String sql = String.format("SELECT MAX(id) FROM %s", tableName);
        int maxId = 0;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
            	maxId = rs.getInt("MAX(id)");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return maxId;
	}
	
	public List<Map<String, Object>> selectAll(String tableName, Map<String, Class<?>> columns) {
		StringBuilder sb = new StringBuilder("Select ");
		Iterator<String> itColumns = columns.keySet().iterator();
		while(itColumns.hasNext()) {
			sb.append(itColumns.next());
			if (itColumns.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append(" FROM ").append(tableName);
		
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
						iVal = selectLastId(tableName) + 1;
						pstmt.setInt(idx, iVal);
					} else if (entry.getValue().getSimpleName().equals("String")) {
						pstmt.setString(idx, (String)val);
					} else {
						pstmt.setNull(idx, java.sql.Types.NULL);
					}
				}
			}
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
		}
	}
	
	public void update(String tableName, Map<String, Class<?>> columns, Map<String, String> constraints, Map<String, Object> values) {
		StringBuilder sb = new StringBuilder(
				String.format("UPDATE %s SET", tableName));
		String where = " WHERE id = ?";
		
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
	
	public void delete(String tableName, int id) {
		String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);

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
