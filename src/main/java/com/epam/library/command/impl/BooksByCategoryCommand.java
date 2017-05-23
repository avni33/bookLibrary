package com.epam.library.command.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			getTargetResource(commandName, request, user, response);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
	}
	
	private void getTargetResource(String commandName, HttpServletRequest request,
			User user, HttpServletResponse response) throws IOException, ServletException {
		String targetResource = null;
		if(CommandEnum.LOGIN.toString().equals(commandName) || CommandEnum.ADD_BOOK.toString().equals(commandName)) {
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.REDIRECT);
			response.sendRedirect(targetResource);
		} else {
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
	}

}
