package com.epam.library.service;

import java.util.List;

import com.epam.library.domain.User;
import com.epam.library.service.exception.ServiceException;

public interface UserService {
	
	User validateGetUser(String userName, String password, String language) throws ServiceException;
	
	User getUserFromId(User user, String language) throws ServiceException;
	
	boolean registerUser(String userName, String password, String name) throws ServiceException; 
	
	boolean editUser(User user, String language) throws ServiceException;
	
	List<User> getAllUsers(String language) throws ServiceException;
	
	User getSelectedUser(int userId, String language) throws ServiceException;

}
