package de.traviadan.lib.db;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
			} else if (cTypeClass.getSimpleName().equals("String")) {
				sb.append("TEXT");
			} else {
				sb.append("NULL");
			}
			if (itColumns.hasNext()) sb.append(", ");
		}
		sb.append(");");
		
        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sb.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void connect_old() {
		Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + Paths.get( System.getProperty("user.dir"), name );
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
	}
}
