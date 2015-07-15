package org.oss.console.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleJdbcExecutor extends AbstractJdbcExecutor {

	private static final String DRIVER_CLASS = "oracle.jdbc.driver.OracleDriver";
	
	public OracleJdbcExecutor(String user, String password, String host,
			String port, String database) {
		super(user, password, host, port, database);
	}

	@Override
	Connection getConnection(String user, String password, String host,
			String port, String database) throws SQLException {
		try {
			Class.forName(DRIVER_CLASS);
			return DriverManager.getConnection(
				String.format("jdbc:oracle:thin:@%s:%s:%s",host,port,database),user,password);
		} catch (ClassNotFoundException e) {
			throw new SQLException("Driver: " + DRIVER_CLASS + " not found.",e);
		}
	}

}
