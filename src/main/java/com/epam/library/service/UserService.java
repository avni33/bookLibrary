package com.epam.library.service;

import com.epam.library.domain.User;
import com.epam.library.service.exception.ServiceException;

public interface UserService {
	
	User validateGetUser(String userName, String password, String language) throws ServiceException;
	
	User getUserFromId(User user, String language) throws ServiceException;

}
