package com.epam.library.service.impl;

import java.time.LocalDate;
import java.util.List;

import com.epam.library.dao.BookDAO;
import com.epam.library.dao.DAOFactory;
import com.epam.library.dao.UserDAO;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.BorrowedBook;
import com.epam.library.service.BookUserService;
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
