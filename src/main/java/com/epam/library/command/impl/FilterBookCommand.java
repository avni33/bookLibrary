package com.epam.library.command.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.command.Command;
import com.epam.library.command.Constant;
import com.epam.library.command.TargetFileProvider;
import com.epam.library.command.util.LanguageProvider;
import com.epam.library.domain.Book;
import com.epam.library.domain.User;
import com.epam.library.service.BookService;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.exception.ServiceException;

public class FilterBookCommand implements Command {
	
	private final static FilterBookCommand INSTANCE = new FilterBookCommand();
	
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookService bookService = serviceFactory.getBookService();
	private TargetFileProvider targetFileProvider = TargetFileProvider.getInstance();
	
	private FilterBookCommand() {}
	
	public static FilterBookCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		HttpSession session = request.getSession(false);
		String language = LanguageProvider.getLanguage(request);
		Map<String, Object> filterParameters = getMapFromRequestParameters(request);
		User user = (User) session.getAttribute(Constant.USER);
		try {
			List<Book> books = bookService.getFilteredBooks(filterParameters, language);
			session.setAttribute(Constant.BOOKS, books);
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
		requestDispatcher.forward(request, response);
	}
	
	private static Map<String, Object> getMapFromRequestParameters(HttpServletRequest request) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(Constant.TITLE, request.getParameter(Constant.TITLE));
		requestParameters.put(Constant.AUTHOR, request.getParameter(Constant.AUTHOR));
		requestParameters.put(Constant.DESCRIPTION, request.getParameter(Constant.DESCRIPTION));
		requestParameters.put(Constant.MIN_PRICE, request.getParameter(Constant.MIN_PRICE));
		requestParameters.put(Constant.MAX_PRICE, request.getParameter(Constant.MAX_PRICE));
		requestParameters.put(Constant.PUBLISH_YEAR, request.getParameter(Constant.PUBLISH_YEAR));
		return requestParameters;
	}


}
