package com.epam.library.service;

import com.epam.library.service.exception.ServiceException;

public interface BookUserService {
	
	boolean borrowBook(int bookId, int userId) throws ServiceException;

}
