package com.epam.library.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epam.library.command.Constant;
import com.epam.library.dao.BookDAO;
import com.epam.library.dao.DAOFactory;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;
import com.epam.library.domain.Ebook;
import com.epam.library.domain.PaperBook;
import com.epam.library.service.BookService;
import com.epam.library.service.checker.ResultChecker;
import com.epam.library.service.exception.ServiceException;
import com.epam.library.service.validator.Validator;

public class BookServiceImpl implements BookService {

	private static final BookServiceImpl INSTANCE = new BookServiceImpl();
	
	private static final String PAPER = "paper";
	private static final String EMPTY_STRING = "";
	private static final String DEFAULT_MIN_PRICE = "0";
	private static final String DEFAULT_MAX_PRICE = "100";
	
	private DAOFactory daoFactory = DAOFactory.getInstance();
	private BookDAO bookDAO = daoFactory.getBookDao();

	private BookServiceImpl() {
	}

	public static BookServiceImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public List<Book> getBooksByCategory(String category, String language) throws ServiceException {
		List<Book> books = new ArrayList<Book>();
		try {
			books = bookDAO.getBookByCategory(language, category);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		ResultChecker.checkBooksList(books);
		return books;
	}

	@Override
	public Book getBookFromId(String language, int bookId) throws ServiceException {
		Book book = null;
		try {
			book = bookDAO.getBook(language, bookId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		ResultChecker.checkBook(book);
		return book;
	}

	@Override
	public List<Book> getSearchedBooks(String searchText, String language) throws ServiceException {
		List<Book> books = new ArrayList<Book>();
		try {
			books = bookDAO.getSearchedBooks(searchText, language);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		ResultChecker.checkBooksList(books);
		return books;
	}

	@Override
	public boolean addBook(Map<String, Object> requestParameters) throws ServiceException {
		boolean bookInserted = false;
		String category = (String) requestParameters.get(Constant.BOOK_CATEGORY);
		Book book = getBookFromParameters(requestParameters, category);
		try {
			bookInserted = bookDAO.insertBook(book, category);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return bookInserted;
	}
	
	private Book getBookFromParameters(Map<String, Object> requestParameters, String category) throws ServiceException {
		if(PAPER.equals(category)) {
			Validator.validatePaperBookData(requestParameters);
			PaperBook paperBook = new PaperBook();
			paperBook.setNoOfPages(Integer.parseInt((String) requestParameters.get(Constant.PAGES)));
			paperBook.setCoverType((String) requestParameters.get(Constant.COVER_TYPE));
			paperBook = (PaperBook) setCommonProperties(requestParameters, paperBook);
			return paperBook;
		} else {
			Validator.validateEbookData(requestParameters);
			Ebook ebook = new Ebook();
			ebook.setFileFormat((String) requestParameters.get(Constant.FILE_FORMAT));
			ebook = (Ebook) setCommonProperties(requestParameters, ebook);
			return ebook;
		}
	}
	
	private Book setCommonProperties(Map<String, Object> requestParameters, Book book) throws ServiceException {
		Validator.validateBookData(requestParameters);
		book.setAuthor((String) requestParameters.get(Constant.AUTHOR));
		book.setTitle((String) requestParameters.get(Constant.TITLE));
		book.setDescription((String) requestParameters.get(Constant.DESCRIPTION));
		book.setPrice(new BigDecimal((String) requestParameters.get(Constant.PRICE)));
		book.setPublishYear(Integer.parseInt((String) requestParameters.get(Constant.PUBLISH_YEAR)));
		return book;
	}

	@Override
	public boolean editBook(Map<String, Object> requestParameters, String language) throws ServiceException {
		boolean bookEdited = false;
		String category = (String) requestParameters.get(Constant.BOOK_CATEGORY);
		Book book = getBookFromParameters(requestParameters, category);
		book.setId(Integer.parseInt((String) requestParameters.get(Constant.BOOK_ID)));
		try {
			bookEdited = bookDAO.editBook(book, category, language);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return bookEdited;
	}

	@Override
	public List<Book> getFilteredBooks(Map<String, Object> filterParameters, String language)
			throws ServiceException {
		List<Book> books = new ArrayList<Book>();
		filterParameters = replaceNullValuesWithEmptyString(filterParameters);
		try {
			books = bookDAO.getFilteredBooks(filterParameters, language);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		ResultChecker.checkBooksList(books);
		return books;
	}
	
	private Map<String, Object> replaceNullValuesWithEmptyString(Map<String, Object> filterParameters) {
		for(Map.Entry<String, Object> entry : filterParameters.entrySet()) {
			if(entry.getValue() == null) {
				entry.setValue(EMPTY_STRING);
			}
			if(entry.getKey().equals(Constant.MIN_PRICE) && entry.getValue().equals(EMPTY_STRING)) {
				entry.setValue(DEFAULT_MIN_PRICE);
			} else if(entry.getKey().equals(Constant.MAX_PRICE) && entry.getValue().equals(EMPTY_STRING))  {
				entry.setValue(DEFAULT_MAX_PRICE);
			}
		}
		return filterParameters;
	}



}
