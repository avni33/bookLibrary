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
import com.epam.library.command.Constant;
import com.epam.library.command.util.LanguageProvider;
import com.epam.library.domain.User;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.UserService;
import com.epam.library.service.exception.ServiceException;

public class GetUsersCommand implements Command {
	
	private static final GetUsersCommand INSTANCE = new GetUsersCommand();
	private static final String USERS = "users";
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private UserService userService = serviceFactory.getUserService();	
	
	private GetUsersCommand() {}
	
	public static GetUsersCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		HttpSession session = request.getSession(false);
		String language = LanguageProvider.getLanguage(request);
		try {
			List<User> users = userService.getAllUsers(language);
			session.setAttribute(USERS, users);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());		
		}
		targetResource = Constant.USERS_LIST_JSP;
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
		requestDispatcher.forward(request, response);
	}

}
