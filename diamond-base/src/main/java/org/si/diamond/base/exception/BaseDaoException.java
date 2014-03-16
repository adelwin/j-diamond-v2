/**
 * File Name    : BaseDaoException.java
 * Author       : adelwin
 * Created Date : Dec 29, 2010 3:11:33 PM
 */
package org.si.diamond.base.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BaseDaoException extends BaseException {
	private static final long serialVersionUID = 7500979249575642484L;

	/**
	 * the root of the exception (the exception that cause the error).
	 */
	private Throwable rootCause = null;

	/**
	 * The error code in the format of ExxxYYYY
	 * 
	 * @see Error code spreadsheet
	 */
	private String errorCode = null;

	/**
	 * For internally use only, to force the developers include an error code
	 * 
	 */
	public BaseDaoException(String msg) {
		super(msg);
	}

	/**
	 * For internally use only, to force the developers include an error code
	 * 
	 */
	public BaseDaoException(Throwable rootCause) {
		super(rootCause.getMessage());
		this.rootCause = rootCause;
	}

	public BaseDaoException() {
		super();
	}

	public BaseDaoException(String msg, Throwable rootCause) {
		this(msg, null, rootCause);
	}

	/**
	 * Construct an exception with a message, error code
	 * 
	 */
	public BaseDaoException(String msg, String msgKey) {
		this(msg, msgKey, null);
	}

	/**
	 * Construct an exception with a message, error code and the root cause
	 * exception, if any
	 * 
	 */
	public BaseDaoException(String msg, String errorCode, Throwable rootCause) {
		super(msg);
		this.rootCause = rootCause;
		this.errorCode = errorCode;
	}

	public void printStackTrace() {
		super.printStackTrace();
		if (rootCause != null) {
			System.err.println("==============Root Cause===============");
			rootCause.printStackTrace();
		}
	}

	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		if (rootCause != null) {
			s.println("==============Root Cause===============");
			rootCause.printStackTrace(s);
		}
	}

	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		if (rootCause != null) {
			s.println("==============Root Cause===============");
			rootCause.printStackTrace(s);
		}
	}

	/**
	 * Returns the errorCode.
	 * 
	 * @return String
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Returns the rootCause.
	 * 
	 * @return Throwable
	 */
	public Throwable getRootCause() {
		return rootCause;
	}

	/**
	 * Sets the errorCode.
	 * 
	 * @param errorCode
	 *            The errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Sets the rootCause.
	 * 
	 * @param rootCause
	 *            The rootCause to set
	 */
	public void setRootCause(Throwable rootCause) {
		this.rootCause = rootCause;
	}


}
