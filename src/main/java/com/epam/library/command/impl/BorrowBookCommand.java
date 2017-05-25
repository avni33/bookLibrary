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

public class BorrowBookCommand implements Command {
	
	private static final BorrowBookCommand INSTANCE = new BorrowBookCommand();
	private static final String USER_ID = "userId";
	private static final String BOOK_BORROWED = "bookBorrowed";

	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookUserService bookUserService = serviceFactory.getBookUserService();
	private GetBookFromIdCommand getBookFromIdCommand = GetBookFromIdCommand.getInstance();
	private BorrowBookCommand() {}
	
	public static BorrowBookCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter(USER_ID));
		int bookId = Integer.parseInt(request.getParameter(Constant.BOOK_ID));
		try {
			boolean bookBorrowed = bookUserService.borrowBook(bookId, userId);
			request.setAttribute(BOOK_BORROWED, bookBorrowed);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
		}
		getBookFromIdCommand.execute(request, response);
	}

}
