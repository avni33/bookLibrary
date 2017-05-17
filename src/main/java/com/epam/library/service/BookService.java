package com.epam.library.service;

import java.util.List;

import com.epam.library.domain.Book;
import com.epam.library.service.exception.ServiceException;

public interface BookService {
	
	List<Book> getBooksByCategory(String category, String language) throws ServiceException;
	
	Book getBookFromId(String language, int bookId) throws ServiceException;
	
	List<Book> getSearchedBooks(String searchText, String language) throws ServiceException;

}
