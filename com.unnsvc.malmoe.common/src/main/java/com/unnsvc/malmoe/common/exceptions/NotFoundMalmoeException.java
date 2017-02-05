
package com.unnsvc.malmoe.common.exceptions;

public class NotFoundMalmoeException extends MalmoeException {

	private static final long serialVersionUID = 1L;

	public NotFoundMalmoeException(String message) {

		super(message);
	}

	public NotFoundMalmoeException(String message, Throwable t) {

		super(message, t);
	}
}
