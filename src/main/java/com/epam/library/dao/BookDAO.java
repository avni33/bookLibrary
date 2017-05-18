package com.epam.library.dao;

import java.util.List;

import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Book;

public interface BookDAO {
	
	/*List<Book> getAllBooks(String language) throws DAOException;
	
	List<Book> getPaperBooks(String language) throws DAOException;
	
	List<Book> getEbooks(String language) throws DAOException;*/
	
	List<Book> getBookByCategory(String language, String category) throws DAOException;
	
	Book getBook(String language, int bookId) throws DAOException;
	
	List<Book> getSearchedBooks(String searchText, String language) throws DAOException;

}
