package com.epam.library.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.epam.library.dao.BookDAO;
import com.epam.library.dao.DAOFactory;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;
import com.epam.library.service.BookService;
import com.epam.library.service.exception.ServiceException;
import com.epam.library.service.validator.Validator;

public class BookServiceImpl implements BookService {

	private static final BookServiceImpl INSTANCE = new BookServiceImpl();
	private static final String ALL = "all";
	private static final String PAPER = "paper";
	private static final String EBOOK = "ebook";
	
	private DAOFactory daoFactory = DAOFactory.getInstance();
	private BookDAO bookDAO = daoFactory.getBookDao();

	private BookServiceImpl() {
	}

	public static BookServiceImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public List<Book> getBooksByCategory(String category, String language) throws ServiceException {
		List<Book> books = switchCategory(category, language);
		Validator.validateBooksList(books);
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
		Validator.validateBook(book);
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
		Validator.validateBooksList(books);
		return books;
	}

	private List<Book> switchCategory(String category, String language) 
			throws ServiceException {
		List<Book> books = new ArrayList<Book>();
		try {
			switch (category) {
			case ALL:
				books = bookDAO.getAllBooks(language);
				break;
			case PAPER:
				books = bookDAO.getPaperBooks(language);
				break;
			case EBOOK:
				books = bookDAO.getEbooks(language);
				break;
			}
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return books;
	}

}
