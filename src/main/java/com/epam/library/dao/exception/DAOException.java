package com.epam.library.dao.exception;

public class DAOException extends Exception {

	private static final long serialVersionUID = 2514733074602512998L;

	public DAOException() {
		super();
	}
	
	public DAOException(String message) {
		super(message);
	}
	
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DAOException(Throwable cause) {
		super(cause);
	}

}
