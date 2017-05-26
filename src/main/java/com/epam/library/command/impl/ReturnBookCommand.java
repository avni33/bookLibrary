package com.epam.library.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.command.Command;
import com.epam.library.command.Constant;
import com.epam.library.service.BookUserService;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.exception.ServiceException;

public class ReturnBookCommand implements Command {
	
	private static final ReturnBookCommand INSTANCE = new ReturnBookCommand();
	private static final String BOOK_ID = "bookId";

	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookUserService bookUserService = serviceFactory.getBookUserService();
	private GetUserFromIdCommand getUserFromIdCommand = GetUserFromIdCommand.getInstance();
	
	private ReturnBookCommand() {}
	
	public static ReturnBookCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter(Constant.USER_ID));
		int bookId = Integer.parseInt(request.getParameter(BOOK_ID));
		try {
			bookUserService.returnBook(userId, bookId);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
		}
		getUserFromIdCommand.execute(request, response);
	}

}
