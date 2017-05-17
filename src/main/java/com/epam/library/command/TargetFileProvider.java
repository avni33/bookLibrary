package com.epam.library.command;

import java.util.HashMap;
import java.util.Map;

public class TargetFileProvider {
	
	private final static TargetFileProvider INSTANCE = new TargetFileProvider();
	private static final String ADMINISTRATOR = "administrator";
	private static final String USER = "user";
	
	private Map<String, String> userRequestSendMap = new HashMap<String, String>();
	private Map<String, String> administratorRequestSendMap = new HashMap<String, String>();
	private Map<String, Map<String, String>> roleMap = new HashMap<String, Map<String, String>>();

	private TargetFileProvider() {
		userRequestSendMap.put(Constant.REDIRECT, Constant.USER_JSP_REDIRECT);
		userRequestSendMap.put(Constant.FORWARD, Constant.USER_JSP_FORWARD);
		administratorRequestSendMap.put(Constant.REDIRECT, Constant.ADMINISTRATOR_JSP_REDIRECT);
		administratorRequestSendMap.put(Constant.FORWARD, Constant.ADMINISTRATOR_JSP_FORWARD);
		roleMap.put(ADMINISTRATOR, administratorRequestSendMap);
		roleMap.put(USER, userRequestSendMap);
	}
	
	public static TargetFileProvider getInstance() {
		return INSTANCE;
	}
	
	public String getTargetFile(String role, String requestSendType) {
		Map<String, String> requestSendMap = roleMap.get(role);
		return requestSendMap.get(requestSendType);
	}

}
