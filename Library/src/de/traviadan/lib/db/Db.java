package de.traviadan.lib.db;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

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
	
	public void insert(String tableName, List<String> columns, List<Class<?>> types, List<String> constraints, List<Object> values) {
		StringBuilder sb = new StringBuilder(
				String.format("INSERT INTO %s (", tableName));
		StringBuilder sbValues = new StringBuilder(" VALUES(");
		Iterator<String> itColumns = columns.listIterator();
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
		
		Iterator<Class<?>> itTypes = types.listIterator();
		Iterator<Object> itValues = values.listIterator();
		Iterator<String> itConstraints = constraints.listIterator();
		try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sb.toString())) {
			int idx = 0;
			while (itTypes.hasNext()) {
				idx++;
				Class<?> cTypeClass = itTypes.next();
				Object val = itValues.next();
				String constraint = itConstraints.next();
				if (cTypeClass.getSimpleName().equals("int")) {
					int iVal = (int)val;
					if(constraint != null && constraint.equals("PRIMARY KEY") && iVal == 0) {
						iVal = selectLastId(tableName) + 1;
					}
					pstmt.setInt(idx, iVal);
				} else if (cTypeClass.getSimpleName().equals("String")) {
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
	
	public void update(String tableName, List<String> columns, List<Class<?>> types, List<String> constraints, List<Object> values) {
		StringBuilder sb = new StringBuilder(
				String.format("UPDATE %s SET", tableName));
		String where = " WHERE id = ?";
		
		Iterator<String> itColumns = columns.listIterator();
		Iterator<String> itConstraints = constraints.listIterator();
		while (itColumns.hasNext()) {
    		String column = itColumns.next();
			String constraint = itConstraints.next();
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
		
		itConstraints = constraints.listIterator();
		Iterator<Class<?>> itTypes = types.listIterator();
		Iterator<Object> itValues = values.listIterator();
		
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sb.toString())) {

        	int idx = 0;
        	int id = 0;
        	while(itTypes.hasNext()) {
        		Class<?> cTypeClass = itTypes.next();
				Object val = itValues.next();
				String constraint = itConstraints.next();
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
