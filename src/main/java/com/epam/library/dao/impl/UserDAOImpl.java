package com.epam.library.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.epam.library.dao.FieldName;
import com.epam.library.dao.UserDAO;
import com.epam.library.dao.connection.pool.MySQLConnectionPool;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Role;
import com.epam.library.domain.User;

public class UserDAOImpl implements UserDAO {
	
private static final UserDAOImpl INSTANCE = new UserDAOImpl();
	
	private static final String SQL_GET_USER 
	= "select u_id, u_username, r_role, r_id, ut_name from user join role on u_role = "
			+ "r_id join user_translation on u_id = ut_user where "
			+ "u_username = ? and u_password = ? and ut_language_code = ?";
	private static final String SQL_GET_USER_FROM_ID 
	= "select u_id, u_username, r_role, r_id, ut_name from user join role"
			+ " on u_role = r_id join user_translation on"
			+ " u_id = ut_user where u_id = ? and ut_language_code = ?";
	private static final String SQL_GET_ID_FROM_USER_NAME 
	= "select u_id from user where u_username = ?";
	private static final String SQL_INSERT_USER 
	= "{ call insert_user(?,?,?,?,?) }";
	private static final String SQL_UPDATE_USER 
	= "{ call update_user(?,?,?,?) }";
	private static final String SQL_UPDATE_TRANSLATE_USER 
	= "{ call update_translate_user(?,?,?,?) }";
	private static final String SQL_GET_ID_FOR_USER_TRANSLATION = 
			"select ut_user from user_translation where ut_user = ? "
			+ "and ut_language_code = ?";
	private static final String SQL_GET_ALL_USER_IDS = 
	"select u_id from user where u_role = 1";
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;
	private static final int ZERO = 0;
	private static final String DEFAULT_LANGUAGE = "en";
	
	private UserDAOImpl() {}
	
	public static UserDAOImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	public User getUserDetails(User user, String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
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
		if(user.getId() == ZERO) {
			user = getUserDetails(user, DEFAULT_LANGUAGE);
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
		user.setUserName(set.getString(FieldName.USERNAME.toString()));
		user.setName(set.getString(FieldName.NAME.toString()));
		user.setId(set.getInt(FieldName.USER_ID.toString()));
		return user;
	}
	
	private void checkConnection(Connection connection) throws DAOException {
		if (connection == null) {
			throw new DAOException("Connection is null");
		}
	}

	@Override
	public User getUserWithChangedLanguage(User user, String language) throws DAOException {
		String name = user.getName();
		user = getUserFromId(user.getId(), language);
		if(!DEFAULT_LANGUAGE.equals(language) && name.equals(user.getName())) {
			user = getUserWithChangedLanguage(user, DEFAULT_LANGUAGE);
		}
		return user;
	}
	
	private User getUserFromId(int userId, String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		User user = new User();
		user.setId(userId);
		checkConnection(connection);
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
		return user;
	}
	
	private PreparedStatement createPreparedStatementForUserFromId(Connection connection
			, User user, String language) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_USER_FROM_ID);
		statement.setInt(ONE, user.getId());
		statement.setString(TWO, language);
		return statement;
	}

	@Override
	public boolean checkIfUserNameExists(String userName) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		boolean userNameExist = false;
		try(PreparedStatement statement = 
				createPreparedStatementForIdFromUserName(connection, userName);
				ResultSet set = statement.executeQuery();) {
			if (set.next()) {
				userNameExist = true;
			}
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "checking user name.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return userNameExist;
	}
	
	private PreparedStatement createPreparedStatementForIdFromUserName(Connection connection
			, String userName) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_ID_FROM_USER_NAME);
		statement.setString(ONE, userName);
		return statement;
	}

	@Override
	public boolean insertUser(User user) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		boolean userInserted = false;
		try {
			connection.setAutoCommit(false);
			userInserted = insertUserInDB(user, connection);
			connection.commit();
		} catch (SQLException se) {
			MySQLConnectionPool.rollBackAndSetAutoCommit(connection);
			throw new DAOException(
					"Issue with DB parameters while "
					+ "inserting user.", se);
		} finally {
			MySQLConnectionPool.setAutoCommit(connection);
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return userInserted;
	}
	
	private boolean insertUserInDB(User user, Connection connection) throws DAOException {
		boolean userInserted = false;
		try(CallableStatement statement = 
				createCallableStatementForInsertingUser(connection, user);) {
			statement.executeUpdate();
			int rows = statement.getInt(FIVE);
			userInserted = checkIfInserted(rows);
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "inserting user.", se);
		}
		return userInserted;
	}
	
	private CallableStatement createCallableStatementForInsertingUser(Connection connection
			, User user) throws SQLException {
		CallableStatement statement = connection.prepareCall(SQL_INSERT_USER);
		statement.setString(ONE, user.getUserName());
		statement.setString(TWO, user.getPassword());
		statement.setInt(THREE, user.getRole().getId());
		statement.setString(FOUR, user.getName());
		statement.registerOutParameter(FIVE, Types.INTEGER);
		return statement;
	}

	@Override
	public boolean editUser(User user, String language) throws DAOException {
		boolean translationExist = checkIfTranslationExist(user, language);
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		boolean userEdited = false;
		try {
			connection.setAutoCommit(false);
			if(translationExist) {
				userEdited = updateUserInDB(user, connection, language);
			} else {
				userEdited = updateTranslateUserInDB(user, connection, language);
			}
			connection.commit();
		} catch (SQLException se) {
			MySQLConnectionPool.rollBackAndSetAutoCommit(connection);
			throw new DAOException(
					"Issue with DB parameters while "
					+ "inserting user.", se);
		} finally {
			MySQLConnectionPool.setAutoCommit(connection);
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return userEdited;
	}
	
	private boolean checkIfTranslationExist(User user, String language) 
			throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		boolean translationExist = false;
		try (PreparedStatement statement = 
				createPreparedStatementForTranslationCheck(connection,
						language, user);
				ResultSet set = statement.executeQuery()) {
			if(set.next()) {
				translationExist = true;
			}
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "checking if translation exist.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return translationExist;
	}
	
	private PreparedStatement createPreparedStatementForTranslationCheck
	(Connection connection, String language,
			User user) throws SQLException {
		PreparedStatement statement = 
				connection.prepareStatement(SQL_GET_ID_FOR_USER_TRANSLATION);
		statement.setInt(ONE, user.getId());
		statement.setString(TWO, language);
		return statement;
	}
	
	private boolean updateUserInDB(User user, Connection connection, String language) 
			throws DAOException {
		boolean userUpdated = false;
		try(CallableStatement statement = 
				createCallableStatementForUpdatingUser(connection, user, language);) {
			statement.executeUpdate();
			int rows = statement.getInt(FOUR);
			userUpdated = checkIfInserted(rows);
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "updating user.", se);
		}
		return userUpdated;
	}
	
	private CallableStatement createCallableStatementForUpdatingUser(Connection connection
			, User user, String language) throws SQLException {
		CallableStatement statement = connection.prepareCall(SQL_UPDATE_USER);
		statement = setParameters(statement, user, language);
		return statement;
	}
	
	private CallableStatement setParameters(CallableStatement 
			statement, User user, String language) throws SQLException {
		statement.setInt(ONE, user.getId());
		statement.setString(TWO, language);
		statement.setString(THREE, user.getName());
		statement.registerOutParameter(FOUR, Types.INTEGER);
		return statement;
	}
	
	private boolean updateTranslateUserInDB(User user, Connection connection, 
			String language) throws DAOException {
		boolean userUpdated = false;
		try(CallableStatement statement = 
				createCallableStatementForUpdateTranslatingUser(connection, 
						user, language);) {
			statement.executeUpdate();
			int rows = statement.getInt(FOUR);
			userUpdated = checkIfInserted(rows);
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "updating translating user.", se);
		}
		return userUpdated;
	}
	
	private CallableStatement createCallableStatementForUpdateTranslatingUser(Connection connection
			, User user, String language) throws SQLException {
		CallableStatement statement = connection.prepareCall(SQL_UPDATE_TRANSLATE_USER);
		statement = setParameters(statement, user, language);
		return statement;
	}

	@Override
	public List<User> getAllUsers(String language) throws DAOException {
		List<Integer> userIds = getUserIds();
		List<User> users = new ArrayList<User>();
		for(Integer id : userIds) {
			User user = getUserFromId(id, language);
			if(user.getName() == null) {
				user = getUserFromId(id, DEFAULT_LANGUAGE);
			}
			users.add(user);
		}
		return users;
	}

	@Override
	public List<Integer> getUserIds() throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		List<Integer> userIds = new ArrayList<Integer>();
		try(PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_USER_IDS);
				ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				userIds.add(set.getInt(FieldName.USER_ID.toString()));
			}
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "getting user ids.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return userIds;
	}

	@Override
	public User getUserById(int userId, String language) throws DAOException {
		User user = getUserFromId(userId, language);
		if(user.getName() == null) {
			user = getUserFromId(userId, DEFAULT_LANGUAGE);
		}
		return user;
	}
	
	private boolean checkIfInserted(int rows) {
		boolean insertDone = false;
		if(rows > 0) {
			insertDone = true;
		}
		return insertDone;
	}
	
}
