package com.epam.library.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.epam.library.dao.BookDAO;
import com.epam.library.dao.DAOFactory;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;
import com.epam.library.service.BookService;
import com.epam.library.service.checker.ResultChecker;
import com.epam.library.service.exception.ServiceException;

public class BookServiceImpl implements BookService {

	private static final BookServiceImpl INSTANCE = new BookServiceImpl();
	
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

}
