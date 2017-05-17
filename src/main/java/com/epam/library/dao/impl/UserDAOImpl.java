package com.epam.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.epam.library.dao.FieldName;
import com.epam.library.dao.UserDAO;
import com.epam.library.dao.connection.pool.MySQLConnectionPool;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Role;
import com.epam.library.domain.User;

public class UserDAOImpl implements UserDAO {
	
private static final UserDAOImpl INSTANCE = new UserDAOImpl();
	
	private static final String SQL_GET_USER 
	= "select u_id, r_role, r_id, ut_name from user join role on u_role = "
			+ "r_id join user_translation on u_id = ut_user where "
			+ "u_username = ? and u_password = ? and ut_language_code = ?";
	private static final String SQL_GET_USER_FROM_ID 
	= "select u_id, r_role, r_id, ut_name from user join role"
			+ " on u_role = r_id join user_translation on"
			+ " u_id = ut_user where u_id = ? and ut_language_code = ?";
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	
	private UserDAOImpl() {}
	
	public static UserDAOImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	public User getUserDetails(User user, String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		if (connection != null) {
			try(PreparedStatement statement = createPreparedStatement(connection, user, language);
					ResultSet set = statement.executeQuery();) {
				while (set.next()) {
					user = getUserFromResultSet(set, user);
				}
			} catch (SQLException se) {
				throw new DAOException(
						"Issue with DB parameters while "
						+ "getting user.", se);
			} finally {
				MySQLConnectionPool.returnConnectionToPool(connection);
			}
		}
		return user;
	}
	
	private PreparedStatement createPreparedStatement(Connection connection
			, User user, String language) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_USER);
		statement.setString(ONE, user.getUserName());
		statement.setString(TWO, user.getPassword());
		statement.setString(THREE, language);
		return statement;
	}
	
	private User getUserFromResultSet(ResultSet set, User user) throws SQLException {
		Role role = new Role();
		role.setId(set.getInt(FieldName.ROLE_ID.toString()));
		role.setRole(set.getString(FieldName.ROLE.toString()));
		user.setRole(role);
		user.setName(set.getString(FieldName.NAME.toString()));
		user.setId(set.getInt(FieldName.USER_ID.toString()));
		return user;
	}

	@Override
	public User getUserWithChangedLanguage(User user, String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		if (connection != null) {
			try(PreparedStatement statement = 
					createPreparedStatementForUserFromId(connection, user, language);
					ResultSet set = statement.executeQuery();) {
				while (set.next()) {
					user = getUserFromResultSet(set, user);
				}
			} catch (SQLException se) {
				throw new DAOException(
						"Issue with DB parameters while "
						+ "getting user.", se);
			} finally {
				MySQLConnectionPool.returnConnectionToPool(connection);
			}
		}
		return user;
	}
	
	private PreparedStatement createPreparedStatementForUserFromId(Connection connection
			, User user, String language) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_USER_FROM_ID);
		statement.setInt(ONE, user.getId());
		statement.setString(TWO, language);
		return statement;
	}

}
