package com.epam.library.command.impl;

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
	private static final String BOOK_ID = "id";
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private BookService bookService = serviceFactory.getBookService();
	private TargetFileProvider targetFileProvider = TargetFileProvider.getInstance();
	
	private GetBookFromIdCommand() {}
	
	public static GetBookFromIdCommand getInstance() {
		return INSTANCE; 
	}

	@Override
	public String execute(HttpServletRequest request) {
		String targetResource = null;
		HttpSession session = request.getSession(false);
		String language = LanguageProvider.getLanguage(request);
		User user = (User) session.getAttribute(Constant.USER);
		int bookId = Integer.parseInt(request.getParameter(BOOK_ID));
		try {
			Book book = bookService.getBookFromId(language, bookId);
			putBookInRequest(book, request);
			targetResource = Constant.BOOK_DETAIL_JSP;
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
		}
		request.setAttribute(Constant.REQUEST_SEND_TYPE, Constant.FORWARD);
		return targetResource;
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
