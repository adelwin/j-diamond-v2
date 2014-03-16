/**
 * File Name	: CipherException.java
 * Author		: Administrator:Adelwin
 * Create Date	: Mar 30, 2006:12:31:13 AM
 *
 * Copyright (c) 2006 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of
 * Adelwin. ("Confidential Information").<BR>
 * <BR>
 * Such Confidential Information shall not be disclosed and it shall
 * only be used in accordance with the terms of the license agreement
 * entered into with Solveware Independent; other than in accordance with the written
 * permission of Solveware Independent. <BR>
 *
 *
 */

package org.si.diamond.task.exception;

import org.si.diamond.base.exception.BaseException;

public class ParseException extends BaseException {

	private static final long	serialVersionUID	= 9110903460415856059L;
	public ParseException(String msg) {
		super(msg);
	}
	public ParseException(Throwable rootCause) {
		super(rootCause);
	}
	public ParseException() {
		super();
	}
	public ParseException(String msg, Throwable rootCause) {
		super(msg, rootCause);
	}
	public ParseException(String msg, String msgKey) {
		super(msg, msgKey);
	}
	public ParseException(String msg, String msgKey, Object[] parameterValues) {
		super(msg, msgKey, parameterValues);
	}
	public ParseException(String msg, String errorCode, Throwable rootCause) {
		super(msg, errorCode, rootCause);
	}
	public ParseException(String msg, String errorCode, Throwable rootCause, Object[] paramValues) {
		super(msg, errorCode, rootCause, paramValues);
	}
}
