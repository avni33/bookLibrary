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
import com.epam.library.command.CommandProvider;
import com.epam.library.command.Constant;
import com.epam.library.command.TargetFileProvider;
import com.epam.library.command.util.LanguageProvider;
import com.epam.library.domain.User;
import com.epam.library.service.ServiceFactory;
import com.epam.library.service.UserService;
import com.epam.library.service.exception.ServiceException;

public class ChangeLanguageCommand implements Command {
	
	private static final ChangeLanguageCommand INSTANCE = new ChangeLanguageCommand();
	private static final String PREVIOUS_COMMAND = "previousCmd"; 
	private static final String TARGET = "target"; 
	private static final String EDIT = "edit"; 
	
	private static final Logger LOG = 
			LogManager.getLogger(LoginCommand.class.getName());
	
	private ServiceFactory serviceFactory = ServiceFactory.getInstance();
	private UserService userService = serviceFactory.getUserService();	
	private TargetFileProvider targetFileProvider = TargetFileProvider.getInstance();

	private ChangeLanguageCommand() {}
	
	public static ChangeLanguageCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetResource = null;
		String target = request.getParameter(TARGET);
		String previousCommand = request.getParameter(PREVIOUS_COMMAND);
		HttpSession session = request.getSession(false);
		String language = LanguageProvider.getLanguage(request);
		User user = (User) session.getAttribute(Constant.USER);
		try {
			user = userService.getUserFromId(user, language);
			session.setAttribute(Constant.USER, user);
			if(EDIT.equals(target)) {
				targetResource = Constant.EDIT_USER_JSP;
				RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
				requestDispatcher.forward(request, response);
			} else {
				CommandProvider commandProvider = CommandProvider.getInstance();
				Command command = commandProvider.getCommand(previousCommand);
				command.execute(request, response);
			}
		} catch (ServiceException e) {
			LOG.log(Level.ERROR, e);
			request.setAttribute(Constant.ERROR, e.getLocalizedMessage());
			targetResource = targetFileProvider.getTargetFile(user.getRole().getRole(), Constant.FORWARD);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
	}

}
