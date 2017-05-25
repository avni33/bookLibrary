package com.epam.library.service;

import com.epam.library.service.impl.BookServiceImpl;
import com.epam.library.service.impl.BookUserServiceImpl;
import com.epam.library.service.impl.UserServiceImpl;

public class ServiceFactory {
	
	private static final ServiceFactory INSTANCE = new ServiceFactory();
	
	private static final UserService USER_SERVICE_INSTANCE = UserServiceImpl.getInstance();
	private static final BookService BOOK_SERVICE_INSTANCE = BookServiceImpl.getInstance();
	private static final BookUserService BOOK_USER_SERVICE_INSTANCE = BookUserServiceImpl.getInstance();
	
	private ServiceFactory() {}
	
	public static ServiceFactory getInstance() {
		return INSTANCE;
	}
	
	public UserService getUserService() {
		return USER_SERVICE_INSTANCE;
	}
	
	public BookService getBookService() {
		return BOOK_SERVICE_INSTANCE;
	}
	
	public BookUserService getBookUserService() {
		return BOOK_USER_SERVICE_INSTANCE;
	}

}
