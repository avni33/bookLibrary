package com.epam.library.command.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.epam.library.command.Constant;

public class ParameterProvider {
	
	public static Map<String, Object> getMapFromRequestParameters(HttpServletRequest request) {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(Constant.BOOK_CATEGORY, request.getParameter(Constant.BOOK_CATEGORY));
		requestParameters.put(Constant.TITLE, request.getParameter(Constant.TITLE));
		requestParameters.put(Constant.AUTHOR, request.getParameter(Constant.AUTHOR));
		requestParameters.put(Constant.DESCRIPTION, request.getParameter(Constant.DESCRIPTION));
		requestParameters.put(Constant.PRICE, request.getParameter(Constant.PRICE));
		requestParameters.put(Constant.PUBLISH_YEAR, request.getParameter(Constant.PUBLISH_YEAR));
		requestParameters.put(Constant.COVER_TYPE, request.getParameter(Constant.COVER_TYPE));
		requestParameters.put(Constant.PAGES, request.getParameter(Constant.PAGES));
		requestParameters.put(Constant.FILE_FORMAT, request.getParameter(Constant.FILE_FORMAT));
		return requestParameters;
	}

}
