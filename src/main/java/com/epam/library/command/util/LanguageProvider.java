package com.epam.library.command.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epam.library.command.Constant;

public class LanguageProvider {
	
	public static String getLanguage(HttpServletRequest request) {
		String language = (String) request.getParameter(Constant.LANGUAGE);
		if(language == null) {
			HttpSession session = request.getSession(false);
			language = (String) session.getAttribute(Constant.LANGUAGE);
		}
		return language;
	}

}
