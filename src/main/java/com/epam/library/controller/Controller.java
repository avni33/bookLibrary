package com.epam.library.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.library.command.Command;
import com.epam.library.command.CommandProvider;
import com.epam.library.command.Constant;

public class Controller extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
		
	private static final String COMMAND = "command";
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String commandName = request.getParameter(COMMAND);
		HttpSession session = request.getSession();
		session.setAttribute(COMMAND, commandName);
		CommandProvider commandProvider = CommandProvider.getInstance();
		Command command = commandProvider.getCommand(commandName);
		String targetResource = command.execute(request);
		String requestSendType = (String) request.getAttribute(Constant.REQUEST_SEND_TYPE);
		if(Constant.REDIRECT.equals(requestSendType)) {
			response.sendRedirect(targetResource);
		} else {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetResource);
			requestDispatcher.forward(request, response);
		}
	}
}
