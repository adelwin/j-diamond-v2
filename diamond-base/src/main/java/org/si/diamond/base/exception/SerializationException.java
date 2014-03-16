/**
 * File Name	: SerializationException.java
 * Author		: Administrator:Adelwin
 * Create Date	: Mar 13, 2005:9:30:16 PM
 *
 * Copyright (c) 2005 Adelwin. All Rights Reserved. <BR>
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

package org.si.diamond.base.exception;



public class SerializationException extends BaseException {
	
	private static final long	serialVersionUID	= 4221532475871978427L;
	public SerializationException() {
		super();
	}
	public SerializationException(String msg) {
		super(msg);
	}
	public SerializationException(String msg, String msgKey) {
		super(msg, msgKey);
	}
	public SerializationException(String msg, String msgKey, Object[] parameterValues) {
		super(msg, msgKey, parameterValues);
	}
	public SerializationException(String msg, String errorCode, Throwable rootCause) {
		super(msg, errorCode, rootCause);
	}
	public SerializationException(String msg, String errorCode, Throwable rootCause, Object[] paramValues) {
		super(msg, errorCode, rootCause, paramValues);
	}
	public SerializationException(String msg, Throwable rootCause) {
		super(msg, rootCause);
	}
	public SerializationException(Throwable rootCause) {
		super(rootCause);
	}
}
