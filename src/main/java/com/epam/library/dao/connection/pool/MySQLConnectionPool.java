package com.epam.library.dao.connection.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.dao.exception.DAOException;

public class MySQLConnectionPool {

	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "mysql://kc0n2ce3tn676cz0:os4taudxkucgi3yq@irkm0xtlo2pcmvvz.chr7pe7iynqr.eu-west-1.rds.amazonaws.com:3306/j1k1az6c2zhkczmg";
	private static final String USERNAME = "kc0n2ce3tn676cz0";
	private static final String PASSWORD = "os4taudxkucgi3yq";
	private static final int CONNECTION_COUNT = 3;

	private static final Logger LOG = LogManager.getLogger(MySQLConnectionPool.class.getName());

	private static BlockingQueue<Connection> availableConnections = new ArrayBlockingQueue<Connection>(
			CONNECTION_COUNT);
	private static BlockingQueue<Connection> connectionsInUse = new ArrayBlockingQueue<Connection>(CONNECTION_COUNT);

	private MySQLConnectionPool() {
		try {
			loadDriver();
			for (int i = 0; i < CONNECTION_COUNT; i++) {
				Connection connection = openConnection();
				if (connection != null) {
					availableConnections.put(connection);
				}
			}
		} catch (InterruptedException e) {
			LOG.log(Level.ERROR, "Problem during adding connections to available connections", e);
		} catch (DAOException e) {
			LOG.log(Level.ERROR, e);
		}
	}
	
	public static MySQLConnectionPool getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public static Connection getConnection() throws DAOException {
		Connection connection = null;
		try {
			connection = availableConnections.take();
			connectionsInUse.put(connection);
		} catch (InterruptedException e) {
			throw new DAOException("Problem during getting connection from available connections"
					+ "or while adding it to connections in use.", e);
		}
		return connection;
	}

	public static void returnConnectionToPool(Connection connection) throws DAOException {
		try {
			availableConnections.put(connection);
		} catch (InterruptedException e) {
			throw new DAOException("Problem during adding connection back to available connections", e);
		}
		connectionsInUse.remove(connection);
	}

	public static void closeAllConnections() throws DAOException {
		try {
			for (Connection connection : availableConnections) {
				connection.close();
			}
			for (Connection connection : connectionsInUse) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new DAOException("Problem while closing all connections", e);
		}
	}

	private void loadDriver() throws DAOException {
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException e) {
			throw new DAOException("Problem during loading MySQL Driver.", e);
		}
	}

	private Connection openConnection() throws DAOException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new DAOException("Problem during getting connection from driver.", e);
		}
		return connection;
	}
	
	private static class SingletonHelper{
        private static final MySQLConnectionPool INSTANCE = new MySQLConnectionPool();
    }
	
}
