package com.epam.library.dao.exception;

public class DAORuntimeException extends RuntimeException {

	private static final long serialVersionUID = -4018069965960568112L;
	
	public DAORuntimeException() {
		super();
	}
	
	public DAORuntimeException(String message) {
		super(message);
	}
	
	public DAORuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DAORuntimeException(Throwable cause) {
		super(cause);
	}

}
