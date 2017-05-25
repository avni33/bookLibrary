package com.epam.library.command.impl;

import java.io.IOException;

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
import com.epam.library.domain.Ebook;
import com.epam.library.domain.PaperBook;
import com.epam.library.domain.User;
import com.epam.library.service.BookService;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.exception.ServiceException;

public class GetBookFromIdCommand implements Command {
	
	private final static GetBookFromIdCommand INSTANCE = new GetBookFromIdCommand();
	
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private static final String BOOK = "book";
	private static final String TARGET_PAGE = "targetPage";
	private static final String EDIT = "edit";
	private static final String RATING = "avgrating";
	private static final String USER_RATING = "rating";
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookService bookService = serviceFactory.getBookService();
	private TargetFileProvider targetFileProvider = TargetFileProvider.getInstance();
	
	private GetBookFromIdCommand() {}
	
	public static GetBookFromIdCommand getInstance() {
		return INSTANCE; 
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		HttpSession session = request.getSession(false);
		String language = LanguageProvider.getLanguage(request);
		User user = (User) session.getAttribute(Constant.USER);
		String targetPage = (String) request.getParameter(TARGET_PAGE);
		int bookId = Integer.parseInt(request.getParameter(Constant.BOOK_ID));
		try {
			Book book = bookService.getBookFromId(language, bookId);
			putBookInRequest(book, request);
			float rating = bookService.getRating(bookId);
			request.setAttribute(RATING, rating);
			int userRating = bookService.getUserRating(user.getId(), bookId);
			request.setAttribute(USER_RATING, userRating);
			if(EDIT.equals(targetPage)) {
				targetResource = Constant.EDIT_BOOK_JSP;
			} else {
				targetResource = Constant.BOOK_DETAIL_JSP;
			}
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
		requestDispatcher.forward(request, response);
	}
	
	public void putBookInRequest(Book book, HttpServletRequest request) {
		if(book instanceof PaperBook) {
			PaperBook castedBook = (PaperBook) book;
			request.setAttribute(BOOK, castedBook);
		} else if(book instanceof Ebook) {
			Ebook castedBook = (Ebook) book;
			request.setAttribute(BOOK, castedBook);
		}
	}

}
