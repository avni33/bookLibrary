package com.epam.library.dao;

import java.util.List;
import java.util.Map;

import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;
import com.epam.library.domain.BorrowedBook;

public interface BookDAO {
	
	List<Book> getBookByCategory(String language, String category) throws DAOException;
	
	Book getBook(String language, int bookId) throws DAOException;
	
	List<Book> getSearchedBooks(String searchText, String language) throws DAOException;
	
	boolean insertBook(Book book, String category) throws DAOException;
	
	boolean editBook(Book book, String category, String language) throws DAOException;
	
	List<Book> getFilteredBooks(Map<String, Object> filterParameters, String language) throws DAOException;
	
	boolean rateBook(int userId, int bookId, int rating) throws DAOException;
	
	float getRating(int bookId) throws DAOException;
	
	int getUserRating(int userId, int bookId) throws DAOException;
	
	boolean checkIfUserHasBorrowedBook(int userId, int bookId) throws DAOException;
	
	boolean insertBorrowedBook(BorrowedBook borrowedBook) throws DAOException;
	
	Map<BorrowedBook, Book> gerBorrowedBooks(int userId, String language) throws DAOException;
	
	boolean returnBook(int userId, int bookId) throws DAOException;

}
