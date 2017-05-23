package com.epam.library.service.validator;

import com.epam.library.domain.User;
import com.epam.library.service.exception.ServiceException;

public class Validator {
	
	private static final int ZERO = 0;
	
	public static void validateLoginDetails(User user) 
			throws ServiceException {
		if(user.getUserName().isEmpty() || user.getUserName() == null) {
			throw new ServiceException("Enter username");
		}
		if(user.getPassword().isEmpty() || user.getPassword() == null) {
			throw new ServiceException("Enter password");
		}
	}
	
	public static void validateRegisterDetails(User user) 
			throws ServiceException {
		validateLoginDetails(user);
		if(user.getName().isEmpty() || user.getName() == null) {
			throw new ServiceException("Enter name");
		}
	}
	
	public static void validateUser(User user) 
			throws ServiceException {
		if(user.getId() == ZERO) {
			throw new ServiceException(
					"Wrong details");
		}
	}
	
}
