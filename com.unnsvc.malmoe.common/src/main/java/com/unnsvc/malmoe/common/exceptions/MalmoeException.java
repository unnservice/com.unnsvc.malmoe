
package com.unnsvc.malmoe.common.exceptions;

public class MalmoeException extends Exception {

	private static final long serialVersionUID = 1L;

	public MalmoeException(String message) {

		super(message);
	}

	public MalmoeException(String message, Throwable t) {

		super(message, t);
	}
}
