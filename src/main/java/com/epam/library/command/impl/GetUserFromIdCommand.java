package com.epam.library.command.impl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.command.Command;
import com.epam.library.command.Constant;
import com.epam.library.command.util.LanguageProvider;
import com.epam.library.domain.Book;
import com.epam.library.domain.BorrowedBook;
import com.epam.library.domain.User;
import com.epam.library.service.UserService;
import com.epam.library.service.BookUserService;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.exception.ServiceException;

public class GetUserFromIdCommand implements Command {
	
	private final static GetUserFromIdCommand INSTANCE = new GetUserFromIdCommand();
	
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private static final String SELECTED_USER = "selectedUser";
	private static final String BORROWED_BOOKS = "borrowedBooks";
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private UserService userService = serviceFactory.getUserService();
	private BookUserService bookUserService = serviceFactory.getBookUserService();
	
	private GetUserFromIdCommand() {}
	
	public static GetUserFromIdCommand getInstance() {
		return INSTANCE; 
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		String language = LanguageProvider.getLanguage(request);
		int userId = Integer.parseInt(request.getParameter(Constant.USER_ID));
		try {
			User user = userService.getSelectedUser(userId, language);
			request.setAttribute(SELECTED_USER, user);
			Map<BorrowedBook, Book> borrowedBooks = bookUserService.getBorrowedBooks(userId, language);
			request.setAttribute(BORROWED_BOOKS, borrowedBooks);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
		}
		targetResource = Constant.USER_DETAILS_JSP;
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
		requestDispatcher.forward(request, response);
	}

}
