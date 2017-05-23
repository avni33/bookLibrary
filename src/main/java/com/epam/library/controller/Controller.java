package com.epam.library.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.library.command.Command;
import com.epam.library.command.CommandProvider;

public class Controller extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
		
	private static final String COMMAND = "command";
	private static final String CHARACTER_ENCODING = "UTF-8";
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding(CHARACTER_ENCODING);
		String commandName = request.getParameter(COMMAND);
		HttpSession session = request.getSession();
		session.setAttribute(COMMAND, commandName);
		CommandProvider commandProvider = CommandProvider.getInstance();
		Command command = commandProvider.getCommand(commandName);
		command.execute(request, response);
	}
}
