package com.epam.library.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import com.epam.library.dao.DAOFactory;
import com.epam.library.dao.UserDAO;
import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.Role;
import com.epam.library.domain.User;
import com.epam.library.service.UserService;
import com.epam.library.service.exception.ServiceException;
import com.epam.library.service.validator.Validator;

public class UserServiceImpl implements UserService {
	
	private static final UserServiceImpl INSTANCE = new UserServiceImpl();
	
	private static final String CHAR_ENCODING = "utf-8";
	private static final int ONE = 1;
	
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

	@Override
	public boolean registerUser(String userName, String password, String name) throws ServiceException {
		User user = constructUser(userName, password, name);
		Validator.validateRegisterDetails(user);
		checkIfUserNameExists(userName);
		boolean userRegistered = false;
		try {
			userRegistered = userDAO.insertUser(user);
		} catch(DAOException e) {
			throw new ServiceException(e);
		}
		return userRegistered;
	}
	
	private User constructUser(String userName, String password, String name) throws ServiceException {
		User user = new User();
		user.setUserName(userName);
		user.setPassword(encodePassword(password));
		user.setName(name);
		Role role = new Role();
		role.setId(ONE);
		user.setRole(role);
		return user;
	}
	
	private void checkIfUserNameExists(String userName) throws ServiceException {
		try {
			if(userDAO.checkIfUserNameExists(userName)) {
				throw new ServiceException("user exist");
			}
		} catch(DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean editUser(User user, String language) throws ServiceException {
		boolean userEdited = false;
		try {
			userEdited = userDAO.editUser(user, language);
		} catch(DAOException e) {
			throw new ServiceException(e);
		}
		return userEdited;
	}

}
