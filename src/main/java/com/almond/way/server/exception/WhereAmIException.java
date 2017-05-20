package com.almond.way.server.exception;

public class WhereAmIException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5861037355464725808L;

	public WhereAmIException(String message) {
		super(message);
	}
	
	public WhereAmIException(Throwable cause) {
		super(cause);
	}
	
	public WhereAmIException(String message, Throwable cause) {
		super(message, cause);
	}

}
