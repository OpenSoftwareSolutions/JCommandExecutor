package org.oss.console.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractJdbcExecutor {
	private final String user, password, host, port, database;
	
	protected AbstractJdbcExecutor(String user, String password, String host,
			String port, String database) {
		super();
		this.user = user;
		this.password = password;
		this.host = host;
		this.port = port;
		this.database = database;
	}
	
	/**
	 * Execute a statement without getting the results
	 * @param sql
	 * @return true if success 
	 * @throws SQLException
	 */
	public boolean executeSql(String sql) throws SQLException {
		try (
			Connection con = getConnection(user, password, host, port, database);
			Statement stmt = con.createStatement();){
			return stmt.execute(sql);
		} 
	}
	/**
	 * Execute a select count(*) from statement
	 * @param sql
	 * @return count or -1 if there are no results
	 * @throws SQLException
	 */
	public int executeSqlGetCount(String sql) throws SQLException {
		try (
			Connection con = getConnection(user, password, host, port, database);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			){
			if (rs.next()) {
				return rs.getInt(1);
			}
			return -1;
		} 
	}
	
	abstract Connection getConnection(String user, String password, String host, String port, String database) throws SQLException;
}
