package com.epam.library.dao.impl;

import java.sql.CallableStatement;
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
import com.epam.library.dao.FieldName;
import com.epam.library.dao.SqlQueryProvider;
import com.epam.library.dao.builder.Builder;
import com.epam.library.dao.builder.BuilderFactory;
import com.epam.library.dao.connection.pool.MySQLConnectionPool;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;
import com.epam.library.domain.Ebook;
import com.epam.library.domain.PaperBook;

public class BookDAOImpl implements BookDAO {

	private static final BookDAOImpl INSTANCE = new BookDAOImpl();

	private static final String SQL_GET_BOOK_FROM_ID = "select b_id, b_price_usd, "
			+ "b_publish_year, pb_pages, ebt_file_format,"
			+ " bt_title, bt_author, bt_description, pbt_cover," 
			+ " bt_language_code from book left join paper_book"
			+ " on b_id=pb_id left join e_book_translation on " 
			+ "b_id = ebt_id join book_translation on b_id=bt_book "
			+ "left join paper_book_translation on pb_id=pbt_paper_book "
			+ "where pbt_language_code = ? or ebt_language_code " 
			+ "= ? having bt_language_code = ? and b_id = ?";
	private static final String SQL_GET_SEARCHED_BOOKS = 
			"select b_id, b_price_usd, b_publish_year, pb_pages, ebt_file_format,"
			+ " bt_title, bt_author, bt_description, pbt_cover, bt_language_code"
			+ " from book left join paper_book on b_id=pb_id left join"
			+ " e_book_translation on b_id = ebt_id join book_translation on "
			+ "b_id=bt_book left join paper_book_translation on pb_id=pbt_paper_book "
			+ "where (pbt_language_code = ? or ebt_language_code = ?) and (bt_title "
			+ "like ? or bt_description like ? or bt_author like ? or "
			+ "b_publish_year like ?) having bt_language_code = ?";
	private static final String SQL_INSERT_PAPER_BOOK = 
			"{ call insert_paper_book(?,?,?,?,?,?,?) }";
	private static final String SQL_INSERT_EBOOK = 
			"{ call insert_ebook(?,?,?,?,?,?) }";
	private static final String SQL_UPDATE_PAPER_BOOK = 
			"{ call update_paper_book(?,?,?,?,?,?,?,?,?) }";
	private static final String SQL_UPDATE_EBOOK = 
			"{ call update_ebook(?,?,?,?,?,?,?,?) }";
	private static final String SQL_UPDATE_TRANSLATE_PAPER_BOOK = 
			"{ call update_translate_paper_book(?,?,?,?,?,?,?,?,?) }";
	private static final String SQL_UPDATE_TRANSLATE_EBOOK = 
			"{ call update_translate_ebook(?,?,?,?,?,?,?,?) }";
	private static final String SQL_GET_ID_FOR_BOOK_TRANSLATION = 
			"select b_id from book join book_translation on b_id "
			+ "= bt_book where b_id = ? and bt_language_code = ?";
	
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;
	private static final int SIX = 6;
	private static final int SEVEN = 7;
	private static final int EIGHT = 8;
	private static final int NINE = 9;
	private static final String PERCENT = "%";
	private static final String DEFAULT_LANGUAGE = "en";
	private static final String PAPER = "paper";
	
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
		List<Integer> bookIds = new ArrayList<Integer>();
		String query = sqlQueryProvider.getQueryForCategory(category);
		checkConnection(connection);
		try (PreparedStatement statement = connection.prepareStatement(query);
				ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				bookIds.add(set.getInt(FieldName.BOOK_ID.toString()));
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting all books.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		List<Book> books = new ArrayList<Book>();
		for(Integer id : bookIds) {
			books.add(getBook(language, id));
		}
		return books;
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
		if(book == null) {
			book = getBook(DEFAULT_LANGUAGE, bookId);
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

	@Override
	public boolean insertBook(Book book, String category) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		boolean bookInserted = false;
		try {
			connection.setAutoCommit(false);
			bookInserted = insertBookAccordingToCategory(book, category, connection);
			connection.commit();
		} catch (SQLException se) {
			MySQLConnectionPool.rollBackAndSetAutoCommit(connection);
			throw new DAOException(
					"Issue with DB parameters while "
					+ "inserting book.", se);
		} finally {
			MySQLConnectionPool.setAutoCommit(connection);
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return bookInserted;
	}
	
	private boolean insertBookAccordingToCategory(
			Book book, String category, Connection connection) throws DAOException {
		boolean bookInserted = false;
		if(PAPER.equals(category)) {
			PaperBook paperBook = (PaperBook) book;
			bookInserted = insertPaperBook(paperBook, connection);
		} else {
			Ebook ebook = (Ebook) book;
			bookInserted = insertEbook(ebook, connection);
		}
		return bookInserted;
	}
	
	private boolean insertPaperBook(PaperBook paperBook, Connection connection) 
			throws DAOException {
		boolean paperBookInserted = true;
		try(CallableStatement statement = 
				getCallableStatementForPaperBookInsertion(paperBook, connection);) {
			statement.executeUpdate();
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "inserting paper book.", se);
		}
		return paperBookInserted;
	}
	
	private CallableStatement getCallableStatementForPaperBookInsertion(
			PaperBook paperBook, Connection connection) throws SQLException {
		CallableStatement statement = connection.prepareCall(SQL_INSERT_PAPER_BOOK);
		statement.setBigDecimal(ONE, paperBook.getPrice());
		statement.setInt(TWO, paperBook.getPublishYear());
		statement.setString(THREE, paperBook.getTitle());
		statement.setString(FOUR, paperBook.getAuthor());
		statement.setString(FIVE, paperBook.getDescription());
		statement.setInt(SIX, paperBook.getNoOfPages());
		statement.setString(SEVEN, paperBook.getCoverType());
		return statement;
	}
	
	private boolean insertEbook(Ebook ebook, Connection connection) 
			throws DAOException {
		boolean ebookInserted = true;
		try(CallableStatement statement = 
				getCallableStatementForEbookInsertion(ebook, connection);) {
			statement.execute();
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "inserting paper book.", 
					se);
		}
		return ebookInserted;
	}
	
	private CallableStatement getCallableStatementForEbookInsertion(
			Ebook ebook, Connection connection) throws SQLException {
		CallableStatement statement = connection.prepareCall(SQL_INSERT_EBOOK);
		statement.setBigDecimal(ONE, ebook.getPrice());
		statement.setInt(TWO, ebook.getPublishYear());
		statement.setString(THREE, ebook.getTitle());
		statement.setString(FOUR, ebook.getAuthor());
		statement.setString(FIVE, ebook.getDescription());
		statement.setString(SIX, ebook.getFileFormat());
		return statement;
	}
	
	@Override
	public boolean editBook(Book book, String category, String language) 
			throws DAOException {
		boolean translationExist = checkIfTranslationExist(book, language);
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		boolean bookInserted = false;
		try {
			connection.setAutoCommit(false);
			if(translationExist) {
				updateBookAccordingToCategory(book, category, language, connection);
			} else {
				updateTranslateBookAccordingToCategory(book, 
						category, language, connection);
			}
			connection.commit();
		} catch (SQLException se) {
			MySQLConnectionPool.rollBackAndSetAutoCommit(connection);
			throw new DAOException(
					"Issue with DB parameters while "
					+ "editing book.", se);
		} finally {
			MySQLConnectionPool.setAutoCommit(connection);
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return bookInserted;
	}
	
	private boolean checkIfTranslationExist(Book book, String language) 
			throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		boolean translationExist = false;
		try (PreparedStatement statement = 
				createPreparedStatementForTranslationCheck(connection,
						language, book);
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
			Book book) throws SQLException {
		PreparedStatement statement = 
				connection.prepareStatement(SQL_GET_ID_FOR_BOOK_TRANSLATION);
		statement.setInt(ONE, book.getId());
		statement.setString(TWO, language);
		return statement;
	}
	
	private boolean updateBookAccordingToCategory(
			Book book, String category, String language, Connection connection) 
					throws DAOException {
		boolean bookUpdated = false;
		if(PAPER.equals(category)) {
			PaperBook paperBook = (PaperBook) book;
			bookUpdated = updatePaperBook(paperBook, language, connection);
		} else {
			Ebook ebook = (Ebook) book;
			bookUpdated = updateEbook(ebook, language, connection);
		}
		return bookUpdated;
	}
	
	private boolean updatePaperBook(PaperBook paperBook, String language,
			Connection connection) throws DAOException {
		boolean paperBookUpdated = true;
		try(CallableStatement statement = 
				getCallableStatementForPaperBookUpdation(paperBook,
						connection, language);) {
			statement.executeUpdate();
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "updating paper book.", se);
		}
		return paperBookUpdated;
	}
	
	private CallableStatement getCallableStatementForPaperBookUpdation(
			PaperBook paperBook, Connection connection, String language) 
					throws SQLException {
		CallableStatement statement = connection.prepareCall(
				SQL_UPDATE_PAPER_BOOK);
		statement = setParametersForPaperBook(statement, paperBook, language);
		return statement;
	}
	
	private CallableStatement setParametersForPaperBook(CallableStatement statement,
			PaperBook paperBook, String language) throws SQLException {
		statement.setInt(ONE, paperBook.getId());
		statement.setBigDecimal(TWO, paperBook.getPrice());
		statement.setInt(THREE, paperBook.getPublishYear());
		statement.setString(FOUR, paperBook.getTitle());
		statement.setString(FIVE, paperBook.getAuthor());
		statement.setString(SIX, paperBook.getDescription());
		statement.setInt(SEVEN, paperBook.getNoOfPages());
		statement.setString(EIGHT, paperBook.getCoverType());
		statement.setString(NINE, language);
		return statement;
	}
	
	private boolean updateEbook(Ebook ebook, String language, Connection connection) 
			throws DAOException {
		boolean ebookUpdated = true;
		try(CallableStatement statement = getCallableStatementForEbookUpdation(
				ebook, connection, language);) {
			statement.execute();
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "updating paper book.", 
					se);
		}
		return ebookUpdated;
	}
	
	private CallableStatement getCallableStatementForEbookUpdation(Ebook ebook, 
			Connection connection, String language) throws SQLException {
		CallableStatement statement = connection.prepareCall(SQL_UPDATE_EBOOK);
		statement = setParametersForEbook(statement, ebook, language);
		return statement;
	}
	
	private CallableStatement setParametersForEbook(CallableStatement statement, 
			Ebook ebook, String language) throws SQLException {
		statement.setInt(ONE, ebook.getId());
		statement.setBigDecimal(TWO, ebook.getPrice());
		statement.setInt(THREE, ebook.getPublishYear());
		statement.setString(FOUR, ebook.getTitle());
		statement.setString(FIVE, ebook.getAuthor());
		statement.setString(SIX, ebook.getDescription());
		statement.setString(SEVEN, ebook.getFileFormat());
		statement.setString(EIGHT, language);
		return statement;
	}
	
	private boolean updateTranslateBookAccordingToCategory(
			Book book, String category, String language, Connection connection) 
					throws DAOException {
		boolean bookUpdated = false;
		if(PAPER.equals(category)) {
			PaperBook paperBook = (PaperBook) book;
			bookUpdated = updateTranslatePaperBook(paperBook, language, connection);
		} else {
			Ebook ebook = (Ebook) book;
			bookUpdated = updateTranslateEbook(ebook, language, connection);
		}
		return bookUpdated;
	}
	
	private boolean updateTranslatePaperBook(PaperBook paperBook, String language, 
			Connection connection) throws DAOException {
		boolean paperBookUpdatedTranslated = true;
		try(CallableStatement statement = 
				getCallableStatementForPaperBookUpdateTranslation(paperBook, 
						connection, language);) {
			statement.executeUpdate();
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "updating translating paper book.", se);
		}
		return paperBookUpdatedTranslated;
	}
	
	private CallableStatement getCallableStatementForPaperBookUpdateTranslation(
			PaperBook paperBook, Connection connection, String language) 
					throws SQLException {
		CallableStatement statement = connection.prepareCall(
				SQL_UPDATE_TRANSLATE_PAPER_BOOK);
		statement = setParametersForPaperBook(statement, paperBook, language);
		return statement;
	}
	
	private boolean updateTranslateEbook(Ebook ebook, String language, 
			Connection connection) throws DAOException {
		boolean ebookUpdatedTranslated = true;
		try(CallableStatement statement = 
				getCallableStatementForEbookUpdateTranslation(ebook, 
						connection, language);) {
			statement.execute();
		} catch (SQLException se) {
			throw new DAOException(
					"Issue with DB parameters while "
					+ "updating translating paper book.", 
					se);
		}
		return ebookUpdatedTranslated;
	}
	
	private CallableStatement getCallableStatementForEbookUpdateTranslation(
			Ebook ebook, Connection connection, String language) throws SQLException {
		CallableStatement statement = connection.prepareCall(
				SQL_UPDATE_TRANSLATE_EBOOK);
		statement = setParametersForEbook(statement, ebook, language);
		return statement;
	}

}
