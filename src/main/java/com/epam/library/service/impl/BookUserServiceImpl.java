package com.epam.library.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.library.dao.BookDAO;
import com.epam.library.dao.DAOFactory;
import com.epam.library.dao.UserDAO;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;
import com.epam.library.domain.BorrowedBook;
import com.epam.library.service.BookUserService;
import com.epam.library.service.checker.ResultChecker;
import com.epam.library.service.exception.ServiceException;

public class BookUserServiceImpl implements BookUserService {
	
	private static final BookUserServiceImpl INSTANCE = new BookUserServiceImpl();
	private static final long FIFTEEN_DAYS = 15;

	private DAOFactory daoFactory = DAOFactory.getInstance();
	private BookDAO bookDAO = daoFactory.getBookDao();
	private UserDAO userDAO = daoFactory.getUserDAO();
	
	private BookUserServiceImpl() {}
	
	public static BookUserServiceImpl getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean rateBook(int userId, int bookId, int rating) throws ServiceException {
		boolean ratingDone = false;
		try {
			ratingDone = bookDAO.rateBook(userId, bookId, rating);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return ratingDone;
	}

	@Override
	public float getRating(int bookId) throws ServiceException {
		float rating = 0;
		try {
			rating = bookDAO.getRating(bookId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return rating;
	}

	@Override
	public int getUserRating(int userId, int bookId) throws ServiceException {
		int rating = 0;
		try {
			rating = bookDAO.getUserRating(userId, bookId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return rating;
	}

	@Override
	public Map<BorrowedBook, Book> getBorrowedBooks(int userId, String language) throws ServiceException {
		Map<BorrowedBook, Book> borrowedBooksMap = new HashMap<BorrowedBook, Book>();
		try {
			borrowedBooksMap = bookDAO.gerBorrowedBooks(userId, language);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		ResultChecker.checkBorrowedBooks(borrowedBooksMap);
		return borrowedBooksMap;
	}

	@Override
	public boolean returnBook(int userId, int bookId) throws ServiceException {
		boolean returned = false;
		try {
			returned = bookDAO.returnBook(userId, bookId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return returned;
	}
	
	@Override
	public boolean borrowBook(int bookId, int userId) throws ServiceException {
		if(!validUserId(userId)) {
			return false;
		}
		try {
			if(bookDAO.checkIfUserHasBorrowedBook(userId, bookId)) {
				return false;
			} else {
				insertBorrowedBook(bookId, userId);
				return true;
			}
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
	
	private boolean validUserId(int userId) throws ServiceException {
		try {
			List<Integer> userIds = userDAO.getUserIds();
			return userIds.contains(userId);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
	
	private void insertBorrowedBook(int bookId, int userId) throws ServiceException {
		BorrowedBook borrowedBook = new BorrowedBook();
		borrowedBook.setUserId(userId);
		borrowedBook.setBookId(bookId);
		borrowedBook.setBorrowedDate(LocalDate.now());
		borrowedBook.setReturnDate(LocalDate.now().plusDays(FIFTEEN_DAYS));
		borrowedBook.setReturnedBook(false);
		try {
			bookDAO.insertBorrowedBook(borrowedBook);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}

}
