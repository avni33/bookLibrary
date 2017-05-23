package com.epam.library.command.impl;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.command.Command;
import com.epam.library.command.Constant;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.UserService;
import com.epam.library.service.exception.ServiceException;

public class RegisterCommand implements Command {
	
	private static final RegisterCommand INSTANCE = new RegisterCommand();
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	private static final String NAME = "name";
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private UserService userService = serviceFactory.getUserService();	
	
	private RegisterCommand() {}
	
	public static RegisterCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		String userName = request.getParameter(Constant.USER_NAME);
		String password = request.getParameter(Constant.PASSWORD);
		String name = request.getParameter(NAME);
		try {
			userService.registerUser(userName, password, name);
			targetResource = Constant.HOME_JSP_REDIRECT;
			response.sendRedirect(targetResource);
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = Constant.REGISTER_JSP;
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
	}

}
