package com.epam.library.service.checker;

import java.util.List;
import java.util.Map;

import com.epam.library.domain.Book;
import com.epam.library.domain.BorrowedBook;
import com.epam.library.domain.User;
import com.epam.library.service.exception.ServiceException;

public class ResultChecker {
	
	public static void checkBooksList(List<Book> books) 
			throws ServiceException {
		if(books.isEmpty()) {
			throw new ServiceException("Oops! Looks like there are "
					+ "no books available.");
		}
	}
	
	public static void checkBook(Book book) throws ServiceException {
		if(book == null) {
			throw new ServiceException("Oops! Looks like the "
					+ "data for this book is not available!");
		}
	}
	
	public static void checkUsers(List<User> users) throws ServiceException {
		if(users.isEmpty()) {
			throw new ServiceException("No Users registered!");
		}
	}
	
	public static void checkBorrowedBooks(Map<BorrowedBook, Book> borrowedBooks) throws ServiceException {
		if(borrowedBooks.isEmpty()) {
			throw new ServiceException("no books");
		}
	}

}
