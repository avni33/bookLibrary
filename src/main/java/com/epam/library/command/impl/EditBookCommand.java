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
import com.epam.library.command.util.ParameterProvider;
import com.epam.library.service.BookService;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.exception.ServiceException;

public class EditBookCommand implements Command {
	
	private static final EditBookCommand INSTANCE = new EditBookCommand();
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
		
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookService bookService = serviceFactory.getBookService();
	private GetBookFromIdCommand getBookFromIdCommand = GetBookFromIdCommand.getInstance();
	
	private EditBookCommand() {}
	
	public static EditBookCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		Map<String, Object> requestParameters = ParameterProvider.getMapFromRequestParameters(request);
		requestParameters.put(Constant.BOOK_ID, request.getParameter(Constant.BOOK_ID));
		String language = LanguageProvider.getLanguage(request);
		try {
			bookService.editBook(requestParameters, language);
			getBookFromIdCommand.execute(request, response);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = Constant.EDIT_BOOK_JSP;
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
	}

}
