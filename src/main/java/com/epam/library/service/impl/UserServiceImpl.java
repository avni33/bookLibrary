package com.epam.library.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import com.epam.library.dao.DAOFactory;
import com.epam.library.dao.UserDAO;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.User;
import com.epam.library.service.UserService;
import com.epam.library.service.exception.ServiceException;
import com.epam.library.service.validator.Validator;

public class UserServiceImpl implements UserService {
	
	private static final UserServiceImpl INSTANCE = new UserServiceImpl();
	
	private static final String CHAR_ENCODING = "utf-8";
	
	private DAOFactory daoFactory = DAOFactory.getInstance();
	private UserDAO userDAO = daoFactory.getUserDAO();
	
	private UserServiceImpl() {}
	
	public static UserServiceImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	public User validateGetUser(String userName, String password, String language) 
			throws ServiceException {
		User user = new User();
		user.setUserName(userName);
		user.setPassword(encodePassword(password));
		Validator.validateLoginDetails(user);
		user = getUser(user, language);
		Validator.validateUser(user);
		return user;
	}
	
	private User getUser(User user, String language) throws ServiceException {
		try {
			user = userDAO.getUserDetails(user, language);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		return user;
	}
	
	private String encodePassword(String password) throws ServiceException {
		try {
			return Base64.getEncoder().encodeToString(password.getBytes(CHAR_ENCODING));
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("Problem during encoding the password!", e);
		}
	}

	@Override
	public User getUserFromId(User user, String language) throws ServiceException {
		try {
			user = userDAO.getUserWithChangedLanguage(user, language);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
		Validator.validateUser(user);
		return user;
	}

}
