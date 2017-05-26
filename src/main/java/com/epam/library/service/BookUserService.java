package com.epam.library.service;

import java.util.Map;

import com.epam.library.domain.Book;
import com.epam.library.domain.BorrowedBook;
import com.epam.library.service.exception.ServiceException;

public interface BookUserService {
	
	boolean borrowBook(int bookId, int userId) throws ServiceException;
	
	boolean rateBook(int userId, int bookId, int rating) throws ServiceException;
	
	float getRating(int bookId) throws ServiceException;
	
	int getUserRating(int userId, int bookId) throws ServiceException;
	
	Map<BorrowedBook, Book> getBorrowedBooks(int userId, String language) throws ServiceException;
	
	boolean returnBook(int userId, int bookId) throws ServiceException;

}
