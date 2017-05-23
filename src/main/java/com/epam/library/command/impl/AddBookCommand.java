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
import com.epam.library.command.util.ParameterProvider;
import com.epam.library.service.BookService;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.exception.ServiceException;

public class AddBookCommand implements Command {

	private static final AddBookCommand INSTANCE = new AddBookCommand();
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookService bookService = serviceFactory.getBookService();
	BooksByCategoryCommand booksByCategoryCommand = BooksByCategoryCommand.getInstance();

	private AddBookCommand() {
	}

	public static AddBookCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		Map<String, Object> requestParameters = 
				ParameterProvider.getMapFromRequestParameters(request);
		try {
			bookService.addBook(requestParameters);
			booksByCategoryCommand.execute(request, response);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = Constant.ADD_BOOK_JSP;
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
	}

}
