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
import com.epam.library.command.util.LanguageProvider;
import com.epam.library.domain.User;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.UserService;
import com.epam.library.service.exception.ServiceException;

public class EditUserCommand implements Command {
	
	private final static EditUserCommand INSTANCE = new EditUserCommand();

	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private final static String NAME = "name";
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private UserService userService = serviceFactory.getUserService();	
	BooksByCategoryCommand booksByCategoryCommand = BooksByCategoryCommand.getInstance();
	
	private EditUserCommand() {}
	
	public static EditUserCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(Constant.USER);
		user.setName(request.getParameter(NAME));
		String language = LanguageProvider.getLanguage(request);
		try {
			userService.editUser(user, language);
			booksByCategoryCommand.execute(request, response);
			session.setAttribute(Constant.USER, user);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = Constant.EDIT_USER_JSP;
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
		
	}

}
