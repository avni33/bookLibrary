package com.epam.library.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.library.command.Constant;
import com.epam.library.dao.BookDAO;
import com.epam.library.dao.FieldName;
import com.epam.library.dao.SqlQueryProvider;
import com.epam.library.dao.builder.Builder;
import com.epam.library.dao.builder.BuilderFactory;
import com.epam.library.dao.connection.pool.MySQLConnectionPool;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;
import com.epam.library.domain.BorrowedBook;
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
			"{ call insert_paper_book(?,?,?,?,?,?,?,?) }";
	private static final String SQL_INSERT_EBOOK = 
			"{ call insert_ebook(?,?,?,?,?,?,?) }";
	private static final String SQL_UPDATE_PAPER_BOOK = 
			"{ call update_paper_book(?,?,?,?,?,?,?,?,?,?) }";
	private static final String SQL_UPDATE_EBOOK = 
			"{ call update_ebook(?,?,?,?,?,?,?,?,?) }";
	private static final String SQL_UPDATE_TRANSLATE_PAPER_BOOK = 
			"{ call update_translate_paper_book(?,?,?,?,?,?,?,?,?,?) }";
	private static final String SQL_UPDATE_TRANSLATE_EBOOK = 
			"{ call update_translate_ebook(?,?,?,?,?,?,?,?,?) }";
	private static final String SQL_GET_ID_FOR_BOOK_TRANSLATION = 
			"select b_id from book join book_translation on b_id "
			+ "= bt_book where b_id = ? and bt_language_code = ?";
	private static final String SQL_GET_FILTERED_BOOKS = 
			"select b_id, bt_title, bt_author, bt_description, b_price_usd,"
			+ " b_publish_year, pb_pages, ebt_file_format, pbt_cover,"
			+ " bt_language_code from book left join paper_book on "
			+ "b_id=pb_id left join e_book_translation on b_id = ebt_id"
			+ " join book_translation on b_id=bt_book left join paper_book_translation"
			+ " on pb_id=pbt_paper_book where (pbt_language_code = ? or "
			+ "ebt_language_code = ?) and  bt_title like ? and bt_author like"
			+ " ? and bt_description like ? and b_price_usd >= ? and b_price_usd <= ? and "
			+ "b_publish_year like ? having bt_language_code = ?";
	private static final String SQL_RATE_BOOK = 
			"{ call insert_update_rating(?,?,?,?) }";
	private static final String SQL_GET_RATING = 
			"select avg(r_rating) as rate from rating where r_book = ?";
	private static final String SQL_GET_USER_RATING = 
			"select r_rating from rating where r_user = ? and r_book = ?";
	private static final String SQL_GET_BORROWED_BOOK_ID = 
			"select bb_id from book_borrow where bb_user = ? and bb_book = ?";
	private static final String SQL_INSERT_BORROWED_BOOK = 
			"INSERT INTO `book_borrow` (`bb_user`, `bb_book`, `bb_borrow_date`,"
			+ " `bb_return_date`, `bb_returned`) VALUES (?,?,?,?,?)";
	private static final String SQL_GET_BORROWED_BOOKS = 
			"select b_id, bb_borrow_date, bb_return_date from book join"
			+ " book_borrow on b_id = bb_book where bb_user = ? and bb_returned = 0";
	private static final String SQL_RETURN_BORROWED_BOOK = 
			"update book_borrow set bb_returned = 1 where bb_user = ? and bb_book = ?";
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;
	private static final int SIX = 6;
	private static final int SEVEN = 7;
	private static final int EIGHT = 8;
	private static final int NINE = 9;
	private static final int TEN = 10;
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
		boolean paperBookInserted = false;
		try(CallableStatement statement = 
				getCallableStatementForPaperBookInsertion(paperBook, connection);) {
			statement.executeUpdate();
			int rows = statement.getInt(EIGHT);
			paperBookInserted = checkIfInserted(rows);
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
		statement.registerOutParameter(EIGHT, Types.INTEGER);
		return statement;
	}
	
	private boolean insertEbook(Ebook ebook, Connection connection) 
			throws DAOException {
		boolean ebookInserted = false;
		try(CallableStatement statement = 
				getCallableStatementForEbookInsertion(ebook, connection);) {
			statement.execute();
			int rows = statement.getInt(SEVEN);
			ebookInserted = checkIfInserted(rows);
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
		statement.registerOutParameter(SEVEN, Types.INTEGER);
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
		boolean paperBookUpdated = false;
		try(CallableStatement statement = 
				getCallableStatementForPaperBookUpdation(paperBook,
						connection, language);) {
			statement.executeUpdate();
			int rows = statement.getInt(TEN);
			paperBookUpdated = checkIfInserted(rows);
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
		statement.registerOutParameter(TEN, Types.INTEGER);
		return statement;
	}
	
	private boolean updateEbook(Ebook ebook, String language, Connection connection) 
			throws DAOException {
		boolean ebookUpdated = false;
		try(CallableStatement statement = getCallableStatementForEbookUpdation(
				ebook, connection, language);) {
			statement.execute();
			int rows = statement.getInt(NINE);
			ebookUpdated = checkIfInserted(rows);
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
		statement.registerOutParameter(NINE, Types.INTEGER);
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
		boolean paperBookUpdatedTranslated = false;
		try(CallableStatement statement = 
				getCallableStatementForPaperBookUpdateTranslation(paperBook, 
						connection, language);) {
			statement.executeUpdate();
			int rows = statement.getInt(TEN);
			paperBookUpdatedTranslated = checkIfInserted(rows);
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
		boolean ebookUpdatedTranslated = false;
		try(CallableStatement statement = 
				getCallableStatementForEbookUpdateTranslation(ebook, 
						connection, language);) {
			statement.execute();
			int rows = statement.getInt(NINE);
			ebookUpdatedTranslated = checkIfInserted(rows);
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

	@Override
	public List<Book> getFilteredBooks(Map<String, Object> filterParameters, String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		List<Book> books = new ArrayList<Book>();
		checkConnection(connection);
		try (PreparedStatement statement = 
				createPreparedStatementForFilteredBooks(connection, language, filterParameters);
				ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				books.add(getBookFromResultSet(set));
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting filtered books.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return books;
	}

	private PreparedStatement createPreparedStatementForFilteredBooks
	(Connection connection, String language,
			Map<String, Object> filterParameters) throws SQLException {
		PreparedStatement statement = 
				connection.prepareStatement(SQL_GET_FILTERED_BOOKS);
		statement.setString(ONE, language);
		statement.setString(TWO, language);
		statement.setString(THREE, PERCENT + (String) filterParameters.get(Constant.TITLE) + PERCENT);
		statement.setString(FOUR, PERCENT + (String) filterParameters.get(Constant.AUTHOR) + PERCENT);
		statement.setString(FIVE, PERCENT + (String) filterParameters.get(Constant.DESCRIPTION) + PERCENT);
		statement.setInt(SIX, Integer.parseInt((String) filterParameters.get(Constant.MIN_PRICE)));
		statement.setInt(SEVEN, Integer.parseInt((String) filterParameters.get(Constant.MAX_PRICE)));
		statement.setString(EIGHT, PERCENT + (String) filterParameters.get(Constant.PUBLISH_YEAR) + PERCENT);
		statement.setString(NINE, language);
		return statement;
	}

	@Override
	public boolean rateBook(int userId, int bookId, int rating) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		boolean ratingDone = false;
		checkConnection(connection);
		try (CallableStatement statement = 
				getCallableStatementForRating(userId, bookId, rating, connection);) {
			statement.execute();
			int rows = statement.getInt(FOUR);
			ratingDone = checkIfInserted(rows);
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "rating books.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return ratingDone;
	}
	
	private CallableStatement getCallableStatementForRating(
			int userId, int bookId, int rating, Connection connection) throws SQLException {
		CallableStatement statement = connection.prepareCall(SQL_RATE_BOOK);
		statement.setInt(ONE, userId);
		statement.setInt(TWO, bookId);
		statement.setInt(THREE, rating);
		statement.registerOutParameter(FOUR, Types.INTEGER);
		return statement;
	}

	@Override
	public float getRating(int bookId) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		float rating = 0;
		checkConnection(connection);
		try (PreparedStatement statement = 
				createPreparedStatementForGettingRating(connection, bookId);
				ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				rating = set.getFloat(FieldName.RATE.toString());
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting rating.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return rating;
	}
	
	private PreparedStatement createPreparedStatementForGettingRating
	(Connection connection, int bookId)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(SQL_GET_RATING);
		statement.setInt(ONE, bookId);
		return statement;
	}

	@Override
	public int getUserRating(int userId, int bookId) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		int userRating = 0;
		checkConnection(connection);
		try (PreparedStatement statement = 
				createPreparedStatementForGettingUserRating(connection, bookId, userId);
				ResultSet set = statement.executeQuery();) {
			while (set.next()) {
				userRating = set.getInt(FieldName.RATING.toString());
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting user rating.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return userRating;
	}
	
	private PreparedStatement createPreparedStatementForGettingUserRating
	(Connection connection, int bookId, int userId)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(SQL_GET_USER_RATING);
		statement.setInt(ONE, userId);
		statement.setInt(TWO, bookId);
		return statement;
	}

	@Override
	public boolean checkIfUserHasBorrowedBook(int userId, int bookId) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		boolean userBorrowedBook = false;
		checkConnection(connection);
		try (PreparedStatement statement = 
				createPreparedStatementForBorrowedBookId(connection, bookId, userId);
				ResultSet set = statement.executeQuery();) {
			if(set.next()) {
				userBorrowedBook = true;
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting borrowed book id.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return userBorrowedBook;
	}
	
	private PreparedStatement createPreparedStatementForBorrowedBookId
	(Connection connection, int bookId, int userId)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(SQL_GET_BORROWED_BOOK_ID);
		statement.setInt(ONE, userId);
		statement.setInt(TWO, bookId);
		return statement;
	}

	@Override
	public boolean insertBorrowedBook(BorrowedBook borrowedBook) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		boolean inserted = false;
		checkConnection(connection);
		try (PreparedStatement statement = 
				createPreparedStatementForInsertingBorrowedBook(connection, borrowedBook);) {
			int rows = statement.executeUpdate();
			inserted = checkIfInserted(rows);
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "inserting borrowed book.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return inserted;
	}
	
	private PreparedStatement createPreparedStatementForInsertingBorrowedBook
	(Connection connection, BorrowedBook borrowedBook)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(SQL_INSERT_BORROWED_BOOK);
		statement.setInt(ONE, borrowedBook.getUserId());
		statement.setInt(TWO, borrowedBook.getBookId());
		statement.setDate(THREE, Date.valueOf(borrowedBook.getBorrowedDate()));
		statement.setDate(FOUR, Date.valueOf(borrowedBook.getReturnDate()));
		statement.setBoolean(FIVE, borrowedBook.isReturnedBook());
		return statement;
	}

	@Override
	public Map<BorrowedBook, Book> gerBorrowedBooks(int userId, String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		checkConnection(connection);
		List<BorrowedBook> borrowedBooks = new ArrayList<BorrowedBook>();
		try (PreparedStatement statement = 
				createPreparedStatementForBorrowedBook(connection, userId);
				ResultSet set = statement.executeQuery();) {
			while(set.next()) {
				borrowedBooks.add(getBorrowedBookFromResultSet(set));
			}
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "getting borrowed book.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		Map<BorrowedBook, Book> borrowedBooksMap = new HashMap<BorrowedBook, Book>();
		for(BorrowedBook borrowedBook : borrowedBooks) {
			borrowedBooksMap.put(borrowedBook, getBook(language, borrowedBook.getBookId()));
		}
		return borrowedBooksMap;
	}
	
	private PreparedStatement createPreparedStatementForBorrowedBook
	(Connection connection, int userId)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(SQL_GET_BORROWED_BOOKS);
		statement.setInt(ONE, userId);
		return statement;
	}
	
	private BorrowedBook getBorrowedBookFromResultSet(ResultSet set) throws SQLException {
		BorrowedBook borrowedBook = new BorrowedBook();
		borrowedBook.setBookId(set.getInt(FieldName.BOOK_ID.toString()));
		borrowedBook.setBorrowedDate(set.getDate(FieldName.BORROW_DATE.toString()).toLocalDate());
		borrowedBook.setReturnDate(set.getDate(FieldName.RETURN_DATE.toString()).toLocalDate());
		return borrowedBook;
	}

	@Override
	public boolean returnBook(int userId, int bookId) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		boolean returned = false;
		checkConnection(connection);
		try (PreparedStatement statement = 
				createPreparedStatementForReturningBorrowedBook(connection, userId, bookId);) {
			int rows = statement.executeUpdate();
			returned = checkIfInserted(rows);
		} catch (SQLException se) {
			throw new DAOException("Issue with DB parameters while " 
		+ "returning borrowed book.", se);
		} finally {
			MySQLConnectionPool.returnConnectionToPool(connection);
		}
		return returned;
	}
	
	private PreparedStatement createPreparedStatementForReturningBorrowedBook
	(Connection connection, int userId, int bookId)
			throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement(SQL_RETURN_BORROWED_BOOK);
		statement.setInt(ONE, userId);
		statement.setInt(TWO, bookId);
		return statement;
	}
	
	private boolean checkIfInserted(int rows) {
		boolean insertDone = false;
		if(rows > 0) {
			insertDone = true;
		}
		return insertDone;
	}

}
