package com.epam.library.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.dao.connection.pool.MySQLConnectionPool;
import com.epam.library.dao.exception.DAOException;

public class ContextListener implements ServletContextListener {
	
	private static final Logger LOG = 
			LogManager.getLogger(ContextListener.class.getName());

    public void contextDestroyed(ServletContextEvent contextEvent)  { 
         try {
			MySQLConnectionPool.closeAllConnections();
		} catch (DAOException e) {
			LOG.log(Level.ERROR, e);
		}
    }
    
    public void contextInitialized(ServletContextEvent contextEvent)  { 
         MySQLConnectionPool.getInstance();
    }
	
}
