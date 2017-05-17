package com.epam.library.service.exception;

public class ServiceException extends Exception {
	
	private static final long serialVersionUID = 6254205318775216910L;

	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
	}

}
