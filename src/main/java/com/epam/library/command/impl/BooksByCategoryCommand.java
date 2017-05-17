package com.epam.library.command.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.command.Command;
import com.epam.library.command.CommandEnum;
import com.epam.library.command.Constant;
import com.epam.library.command.TargetFileProvider;
import com.epam.library.command.util.LanguageProvider;
import com.epam.library.domain.Book;
import com.epam.library.domain.User;
import com.epam.library.service.BookService;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.exception.ServiceException;

public class BooksByCategoryCommand implements Command {
	
	private static final BooksByCategoryCommand INSTANCE = new BooksByCategoryCommand();
	
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private static final String CATEGORY = "category";
	private static final String BOOKS = "books";
	private static final String COMMAND = "command";
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookService bookService = serviceFactory.getBookService();
	private TargetFileProvider targetFileProvider = TargetFileProvider.getInstance();
	
	private BooksByCategoryCommand() {}
	
	public static BooksByCategoryCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public String execute(HttpServletRequest request) {
		String targetResource = null;
		String category = request.getParameter(CATEGORY);
		HttpSession session = request.getSession(false);
		String language = LanguageProvider.getLanguage(request);
		User user = (User) session.getAttribute(Constant.USER);
		try {
			List<Book> books = bookService.getBooksByCategory(category, language);
			session.setAttribute(BOOKS, books);
			session.setAttribute(CATEGORY, category);
			String commandName = request.getParameter(COMMAND);
			targetResource = getTargetResource(commandName, request, user);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			request.setAttribute(Constant.REQUEST_SEND_TYPE, Constant.FORWARD);
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
		}
		return targetResource;
	}
	
	private String getTargetResource(String commandName, HttpServletRequest request, User user) {
		String targetResource = null;
		if(CommandEnum.LOGIN.toString().equals(commandName)) {
			request.setAttribute(Constant.REQUEST_SEND_TYPE, Constant.REDIRECT);
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.REDIRECT);
		} else {
			request.setAttribute(Constant.REQUEST_SEND_TYPE, Constant.FORWARD);
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
		}
		return targetResource;
	}

}
