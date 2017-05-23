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

public class BookServiceImpl implements BookService {

	private static final BookServiceImpl INSTANCE = new BookServiceImpl();
	
	private static final String PAPER = "paper";
	
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
	
	private Book getBookFromParameters(Map<String, Object> requestParameters, String category) {
		if(PAPER.equals(category)) {
			PaperBook paperBook = new PaperBook();
			paperBook.setNoOfPages(Integer.parseInt((String) requestParameters.get(Constant.PAGES)));
			paperBook.setCoverType((String) requestParameters.get(Constant.COVER_TYPE));
			paperBook = (PaperBook) setCommonProperties(requestParameters, paperBook);
			return paperBook;
		} else {
			Ebook ebook = new Ebook();
			ebook.setFileFormat((String) requestParameters.get(Constant.FILE_FORMAT));
			ebook = (Ebook) setCommonProperties(requestParameters, ebook);
			return ebook;
		}
	}
	
	private Book setCommonProperties(Map<String, Object> requestParameters, Book book) {
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

}
