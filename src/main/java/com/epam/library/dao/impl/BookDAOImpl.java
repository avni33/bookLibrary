package com.epam.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.library.dao.BookDAO;
import com.epam.library.dao.SqlQueryProvider;
import com.epam.library.dao.builder.Builder;
import com.epam.library.dao.builder.BuilderFactory;
import com.epam.library.dao.connection.pool.MySQLConnectionPool;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;

public class BookDAOImpl implements BookDAO {

	private static final BookDAOImpl INSTANCE = new BookDAOImpl();

	private static final String SQL_GET_BOOK_FROM_ID = "select b_id, b_price_usd, b_publish_year, pb_pages, ebt_file_format,"
			+ " bt_title, bt_author, bt_description, pbt_cover," + " bt_language_code from book left join paper_book"
			+ " on b_id=pb_id left join e_book_translation on " + "b_id = ebt_id join book_translation on b_id=bt_book "
			+ "left join paper_book_translation on pb_id=pbt_paper_book "
			+ "where pbt_language_code = ? or ebt_language_code " + "= ? having bt_language_code = ? and b_id = ?";
	private static final String SQL_GET_SEARCHED_BOOKS = "select b_id, b_price_usd, b_publish_year, pb_pages, ebt_file_format,"
			+ " bt_title, bt_author, bt_description, pbt_cover, bt_language_code"
			+ " from book left join paper_book on b_id=pb_id left join"
			+ " e_book_translation on b_id = ebt_id join book_translation on "
			+ "b_id=bt_book left join paper_book_translation on pb_id=pbt_paper_book "
			+ "where (pbt_language_code = ? or ebt_language_code = ?) and (bt_title "
			+ "like ? or bt_description like ? or bt_author like ? or "
			+ "b_publish_year like ?) having bt_language_code = ?";
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;
	private static final int SIX = 6;
	private static final int SEVEN = 7;
	private static final String PERCENT = "%";
	
	private SqlQueryProvider sqlQueryProvider = SqlQueryProvider.getInstance();
	private BuilderFactory builderFactory = BuilderFactory.getInstance();

	private BookDAOImpl() {
	}

	public static BookDAOImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public List<Book> getBookByCategory(String language, String category) 
			throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		List<Book> books = new ArrayList<Book>();
		checkConnection(connection);
		try (PreparedStatement statement = createPreparedStatementForBooksCategory
				(connection, language, category);
				ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				books.add(getBookFromResultSet(set));
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting all books.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return books;
	}

	private PreparedStatement createPreparedStatementForBooksCategory
	(Connection connection, String language,
			String category) throws SQLException {
		String query = sqlQueryProvider.getQueryForCategory(category, language);
		PreparedStatement statement = connection.prepareStatement(query);
		return statement;
	}

	private Book getBookFromResultSet(ResultSet set) throws SQLException {
		ResultSetMetaData md = set.getMetaData();
		int columns = md.getColumnCount();
		Map<String, Object> row = new HashMap<String, Object>();
		for (int i = ONE; i <= columns; i++) {
			row.put(md.getColumnName(i), set.getObject(i));
		}
		Builder builder = builderFactory.getBuilder(row);
		return builder.buildBook(row);
	}
	
	private void checkConnection(Connection connection) throws DAOException {
		if (connection == null) {
			throw new DAOException("Connection is null");
		}
	}

	@Override
	public Book getBook(String language, int bookId) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		Book book = null;
		checkConnection(connection);
		try (PreparedStatement statement = 
				createPreparedStatementForBookFromId
				(connection, language, bookId);
				ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				book = getBookFromResultSet(set);
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting selected books.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return book;
	}

	private PreparedStatement createPreparedStatementForBookFromId
	(Connection connection, String language, int bookId)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(SQL_GET_BOOK_FROM_ID);
		statement.setString(ONE, language);
		statement.setString(TWO, language);
		statement.setString(THREE, language);
		statement.setInt(FOUR, bookId);
		return statement;
	}

	@Override
	public List<Book> getSearchedBooks(String searchText, String language) 
			throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		List<Book> books = new ArrayList<Book>();
		checkConnection(connection);
		try (PreparedStatement statement = 
				createPreparedStatementForSearchedBooks(connection, language,
				searchText); ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				books.add(getBookFromResultSet(set));
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting searched books.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return books;
	}

	private PreparedStatement createPreparedStatementForSearchedBooks
	(Connection connection, String language,
			String searchText) throws SQLException {
		PreparedStatement statement = 
				connection.prepareStatement(SQL_GET_SEARCHED_BOOKS);
		statement.setString(ONE, language);
		statement.setString(TWO, language);
		statement.setString(THREE, PERCENT + searchText + PERCENT);
		statement.setString(FOUR, PERCENT + searchText + PERCENT);
		statement.setString(FIVE, PERCENT + searchText + PERCENT);
		statement.setString(SIX, PERCENT + searchText + PERCENT);
		statement.setString(SEVEN, language);
		return statement;
	}

}
