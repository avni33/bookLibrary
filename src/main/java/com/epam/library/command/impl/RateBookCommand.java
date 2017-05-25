package com.epam.library.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.command.Command;
import com.epam.library.command.Constant;
import com.epam.library.domain.User;
import com.epam.library.service.BookService;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.exception.ServiceException;

public class RateBookCommand implements Command {
	
	private static final RateBookCommand INSTANCE = new RateBookCommand();
	private static final String RATING = "rating";

	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookService bookService = serviceFactory.getBookService();
	private GetBookFromIdCommand getBookFromIdCommand = GetBookFromIdCommand.getInstance();
	
	private RateBookCommand() {}
	
	public static RateBookCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(Constant.USER);
		int bookId = Integer.parseInt(request.getParameter(Constant.BOOK_ID));
		int rating = Integer.parseInt(request.getParameter(RATING));
		try {
			bookService.rateBook(user.getId(), bookId, rating);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
		}
		getBookFromIdCommand.execute(request, response);
	}

}
