package com.epam.library.dao;

import java.util.List;
import java.util.Map;

import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;

public interface BookDAO {
	
	List<Book> getBookByCategory(String language, String category) throws DAOException;
	
	Book getBook(String language, int bookId) throws DAOException;
	
	List<Book> getSearchedBooks(String searchText, String language) throws DAOException;
	
	boolean insertBook(Book book, String category) throws DAOException;
	
	boolean editBook(Book book, String category, String language) throws DAOException;
	
	List<Book> getFilteredBooks(Map<String, String> filterParameters, String language) throws DAOException;

}
