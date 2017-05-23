package com.epam.library.dao;

import com.epam.library.dao.exception.DAOException;
import com.epam.library.domain.User;

public interface UserDAO {

	User getUserDetails(User user, String language) throws DAOException;
	
	User getUserWithChangedLanguage(User user, String language) throws DAOException;
	
	boolean checkIfUserNameExists(String userName) throws DAOException;
	
	boolean insertUser(User user) throws DAOException;
	
	boolean editUser(User user, String language) throws DAOException;

}
