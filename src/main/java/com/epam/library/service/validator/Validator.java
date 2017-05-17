package com.epam.library.service.validator;

import java.util.List;

import com.epam.library.domain.Book;
import com.epam.library.domain.User;
import com.epam.library.service.exception.ServiceException;

public class Validator {
	
	private static final int ZERO = 0;
	
	public static void validateLoginDetails(User user) 
			throws ServiceException {
		if(user.getUserName().isEmpty()) {
			throw new ServiceException("Enter username.");
		}
		if(user.getPassword().isEmpty()) {
			throw new ServiceException("Enter password.");
		}
	}
	
	public static void validateUser(User user) 
			throws ServiceException {
		if(user.getId() == ZERO) {
			throw new ServiceException(
					"Oops! Looks like the entered details are wrong.");
		}
	}
	
	public static void validateBooksList(List<Book> books) 
			throws ServiceException {
		if(books.isEmpty()) {
			throw new ServiceException("Oops! Looks like there are "
					+ "no books available.");
		}
	}
	
	public static void validateBook(Book book) throws ServiceException {
		if(book == null) {
			throw new ServiceException("Oops! Looks like the "
					+ "data for this book is not available!");
		}
	}

}
