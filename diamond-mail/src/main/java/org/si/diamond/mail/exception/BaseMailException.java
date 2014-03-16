package org.si.diamond.mail.exception;

import org.si.diamond.base.exception.BaseException;

public class BaseMailException extends BaseException {

	private static final long	serialVersionUID	= 6046927819414834641L;

	public BaseMailException() {
		super();
	}

	public BaseMailException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseMailException(String message) {
		super(message);
	}

	public BaseMailException(Throwable cause) {
		super(cause);
	}

}
