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
import com.epam.library.domain.User;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.UserService;
import com.epam.library.service.exception.ServiceException;

public class LoginCommand implements Command {
	
	private static final LoginCommand INSTANCE = new LoginCommand();
	
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private static final String USER_NAME = "userName";
	private static final String PASSWORD = "password";
		
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private UserService userService = serviceFactory.getUserService();	
	BooksByCategoryCommand booksByCategoryCommand = BooksByCategoryCommand.getInstance();
	
	private LoginCommand() {}
	
	public static LoginCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		String userName = request.getParameter(USER_NAME);
		String password = request.getParameter(PASSWORD);
		HttpSession session = request.getSession(false);
		String language = (String)session.getAttribute(Constant.LANGUAGE);
		try {
			User user = userService.validateGetUser(userName, password, language);
			session.setAttribute(Constant.USER, user);
			booksByCategoryCommand.execute(request, response);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = Constant.HOME_JSP;
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
	}

}
