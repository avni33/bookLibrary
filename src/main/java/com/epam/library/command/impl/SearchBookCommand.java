package com.epam.library.command.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

public class SearchBookCommand implements Command {
	
	private static final SearchBookCommand INSTANCE = new SearchBookCommand();
	
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private static final String BOOKS = "books";
	private static final String SEARCH_TEXT = "searchText";
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookService bookService = serviceFactory.getBookService();
	private TargetFileProvider targetFileProvider = TargetFileProvider.getInstance();
	
	private SearchBookCommand() {}
	
	public static SearchBookCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public String execute(HttpServletRequest request) {
		String targetResource = null;
		HttpSession session = request.getSession(false);
		String language = LanguageProvider.getLanguage(request);
		String searchText = (String) request.getParameter(SEARCH_TEXT);
		User user = (User) session.getAttribute(Constant.USER);
		try {
			List<Book> books = bookService.getSearchedBooks(searchText, language);
			session.setAttribute(BOOKS, books);
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
		}
		request.setAttribute(Constant.REQUEST_SEND_TYPE, Constant.FORWARD);
		return targetResource;
	}

}
