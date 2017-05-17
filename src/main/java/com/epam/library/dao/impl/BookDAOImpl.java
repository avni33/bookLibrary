package com.epam.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epam.library.dao.BookDAO;
import com.epam.library.dao.FieldName;
import com.epam.library.dao.connection.pool.MySQLConnectionPool;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;
import com.epam.library.domain.Ebook;
import com.epam.library.domain.PaperBook;

public class BookDAOImpl implements BookDAO {
	
	private static final BookDAOImpl INSTANCE = new BookDAOImpl();
	
	private static final String SQL_GET_PAPER_BOOKS 
	= "select b_id, b_price_usd, b_publish_year, bt_title, bt_author, "
			+ "bt_description, pb_pages, pbt_cover from book join book_translation "
			+ "on b_id=bt_book join paper_book on b_id=pb_id join "
			+ "paper_book_translation on pb_id=pbt_paper_book where "
			+ "bt_language_code = ? and pbt_language_code = ?";
	private static final String SQL_GET_EBOOKS 
	= "select b_id, b_price_usd, b_publish_year, bt_title, bt_author,"
			+ " bt_description, ebt_file_format from book join "
			+ "book_translation on b_id=bt_book join e_book_translation"
			+ " on b_id = ebt_id where bt_language_code = ?"
			+ " and ebt_language_code = ?";
	private static final String SQL_GET_ALL_BOOKS 
	= "select b_id, b_price_usd, b_publish_year, pb_pages, ebt_file_format,"
			+ " bt_title, bt_author, bt_description, pbt_cover,"
			+ " bt_language_code from book left join paper_book"
			+ " on b_id=pb_id left join e_book_translation on "
			+ "b_id = ebt_id join book_translation on b_id=bt_book "
			+ "left join paper_book_translation on pb_id=pbt_paper_book "
			+ "where pbt_language_code = ? or ebt_language_code "
			+ "= ? having bt_language_code = ?";
	private static final String SQL_GET_BOOK_FROM_ID
	= "select b_id, b_price_usd, b_publish_year, pb_pages, ebt_file_format,"
			+ " bt_title, bt_author, bt_description, pbt_cover,"
			+ " bt_language_code from book left join paper_book"
			+ " on b_id=pb_id left join e_book_translation on "
			+ "b_id = ebt_id join book_translation on b_id=bt_book "
			+ "left join paper_book_translation on pb_id=pbt_paper_book "
			+ "where pbt_language_code = ? or ebt_language_code "
			+ "= ? having bt_language_code = ? and b_id = ?";
	private static final String SQL_GET_SEARCHED_BOOKS
	= "select b_id, b_price_usd, b_publish_year, pb_pages, ebt_file_format,"
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
	
	private BookDAOImpl() {}
	
	public static BookDAOImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public List<Book> getAllBooks(String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		List<Book> books = new ArrayList<Book>();
		if (connection != null) {
			try(PreparedStatement statement = createPreparedStatementForAllBooks(connection, language);
					ResultSet set = statement.executeQuery();) {
				while (set.next()) {
					books.add(getBookFromResultSet(set));
				}
			} catch (SQLException se) {
				throw new DAOException(
						"Issue with DB parameters while "
						+ "getting all books.", se);
			} finally {
				MySQLConnectionPool.returnConnectionToPool(connection);
			}
		}
		return books;
	}
	
	private PreparedStatement createPreparedStatementForAllBooks(Connection connection
			, String language) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_ALL_BOOKS);
		statement.setString(ONE, language);
		statement.setString(TWO, language);
		statement.setString(THREE, language);
		return statement;
	}
	
	private Book getBookFromResultSet(ResultSet set) throws SQLException {
		Book book = getBookInstance(set);
		setBookValues(set, book);
		return book;
	}
	
	private Book getBookInstance(ResultSet set) throws SQLException {
		String fileFormat = set.getString(FieldName.FILE_FORMAT.toString());
		int pages = set.getInt(FieldName.PAGES.toString());
		String coverType = set.getString(FieldName.COVER_TYPE.toString());
		if(fileFormat != null) {
			Ebook ebook = new Ebook();
			ebook.setFileFormat(fileFormat);
			return ebook;
		} else {
			PaperBook paperBook =  new PaperBook();
			paperBook.setCoverType(coverType);
			paperBook.setNoOfPages(pages);
			return paperBook;
		}
	}
	
	@Override
	public List<Book> getPaperBooks(String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		List<Book> paperBooks = new ArrayList<Book>();
		if (connection != null) {
			try(PreparedStatement statement = createPreparedStatementForPaperBooks(connection, language);
					ResultSet set = statement.executeQuery();) {
				while (set.next()) {
					paperBooks.add(getPaperBookFromResultSet(set));
				}
			} catch (SQLException se) {
				throw new DAOException(
						"Issue with DB parameters while "
						+ "getting paper books.", se);
			} finally {
				MySQLConnectionPool.returnConnectionToPool(connection);
			}
		}
		return paperBooks;
	}
	
	private PreparedStatement createPreparedStatementForPaperBooks(Connection connection
			, String language) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_PAPER_BOOKS);
		statement.setString(ONE, language);
		statement.setString(TWO, language);
		return statement;
	}
	
	private Book getPaperBookFromResultSet(ResultSet set) throws SQLException {
		Book book = new PaperBook();
		setBookValues(set, book);
		return book;
	}

	@Override
	public List<Book> getEbooks(String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		List<Book> ebooks = new ArrayList<Book>();
		if (connection != null) {
			try(PreparedStatement statement = createPreparedStatementForEbooks(connection, language);
					ResultSet set = statement.executeQuery();) {
				while (set.next()) {
					ebooks.add(getEbookFromResultSet(set));
				}
			} catch (SQLException se) {
				throw new DAOException(
						"Issue with DB parameters while "
						+ "getting electronic books.", se);
			} finally {
				MySQLConnectionPool.returnConnectionToPool(connection);
			}
		}
		return ebooks;
	}
	
	private PreparedStatement createPreparedStatementForEbooks(Connection connection
			, String language) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_EBOOKS);
		statement.setString(ONE, language);
		statement.setString(TWO, language);
		return statement;
	}
	
	private Book getEbookFromResultSet(ResultSet set) throws SQLException {
		Book book = new Ebook();
		setBookValues(set, book);
		return book;
	}
	
	private Book setBookValues(ResultSet set, Book book) throws SQLException {
		book.setId(set.getInt(FieldName.BOOK_ID.toString()));
		book.setTitle(set.getString(FieldName.TITLE.toString()));
		book.setAuthor(set.getString(FieldName.AUTHOR.toString()));
		book.setDescription(set.getString(FieldName.DESCRIPTION.toString()));
		book.setPrice(set.getBigDecimal(FieldName.PRICE.toString()));
		book.setPublishYear(set.getInt(FieldName.PUBLISH_YEAR.toString()));
		return book;
	}

	@Override
	public Book getBook(String language, int bookId) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		Book book = null;
		if (connection != null) {
			try(PreparedStatement statement = 
					createPreparedStatementForBookFromId(
							connection, language, bookId);
					ResultSet set = statement.executeQuery();) {
				while (set.next()) {
					book = getBookFromIdFromResultSet(set);
				}
			} catch (SQLException se) {
				throw new DAOException(
						"Issue with DB parameters while "
						+ "getting selected books.", se);
			} finally {
				MySQLConnectionPool.returnConnectionToPool(connection);
			}
		}
		return book;
	}
	
	private PreparedStatement createPreparedStatementForBookFromId(Connection connection
			, String language, int bookId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_BOOK_FROM_ID);
		statement.setString(ONE, language);
		statement.setString(TWO, language);
		statement.setString(THREE, language);
		statement.setInt(FOUR, bookId);
		return statement;
	}
	
	private Book getBookFromIdFromResultSet(ResultSet set) throws SQLException {
		Book book = getBookInstance(set);
		setBookValues(set, book);
		return book;
	}

	@Override
	public List<Book> getSearchedBooks(String searchText, String language) throws DAOException {
		Connection connection = MySQLConnectionPool.getConnection();
		List<Book> books = new ArrayList<Book>();
		if (connection != null) {
			try(PreparedStatement statement = createPreparedStatementForSearchedBooks(
					connection, language, searchText);
					ResultSet set = statement.executeQuery();) {
				while (set.next()) {
					books.add(getBookFromResultSet(set));
				}
			} catch (SQLException se) {
				throw new DAOException(
						"Issue with DB parameters while "
						+ "getting searched books.", se);
			} finally {
				MySQLConnectionPool.returnConnectionToPool(connection);
			}
		}
		return books;
	}
	
	private PreparedStatement createPreparedStatementForSearchedBooks(Connection connection
			, String language, String searchText) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(SQL_GET_SEARCHED_BOOKS);
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
