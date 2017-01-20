
package com.unnsvc.malmoe.common.exceptions;

public class AccessException extends MalmoeException {

	private static final long serialVersionUID = 1L;

	public AccessException(String message) {

		super(message);
	}

	public AccessException(String message, Throwable t) {

		super(message, t);
	}
}
