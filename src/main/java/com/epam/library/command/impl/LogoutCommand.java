package com.epam.library.command.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.library.command.Command;
import com.epam.library.command.Constant;

public class LogoutCommand implements Command {
	
    private static final LogoutCommand INSTANCE = new LogoutCommand();
	
	private LogoutCommand() {}
	
	public static LogoutCommand getInstance() {
		return INSTANCE;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String targetResource = Constant.HOME_JSP_REDIRECT;
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		response.sendRedirect(targetResource);
	}

}
