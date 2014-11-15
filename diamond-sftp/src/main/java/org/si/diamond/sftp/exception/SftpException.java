/*
 * File Name	: $filename
 * Author		: @$user.name
 * Created Date	: $date $time
 *
 * Copyright (c) 2014 Solveware Independent. All Rights Reserved. <BR/>
 * <BR/>
 * This software contains confidential and proprietary information of Solveware Independent.<BR/>
 */

package org.si.diamond.sftp.exception;

import org.si.diamond.base.exception.BaseException;

/**
 * Created by adelwin on 15/11/2014.
 */
public class SftpException extends BaseException {

	private static final long	serialVersionUID	= 9110903460415856059L;
	public SftpException(String msg) {
		super(msg);
	}
	public SftpException(Throwable rootCause) {
		super(rootCause);
	}
	public SftpException() {
		super();
	}
	public SftpException(String msg, Throwable rootCause) {
		super(msg, rootCause);
	}
	public SftpException(String msg, String msgKey) {
		super(msg, msgKey);
	}
	public SftpException(String msg, String msgKey, Object[] parameterValues) {
		super(msg, msgKey, parameterValues);
	}
	public SftpException(String msg, String errorCode, Throwable rootCause) {
		super(msg, errorCode, rootCause);
	}
	public SftpException(String msg, String errorCode, Throwable rootCause, Object[] paramValues) {
		super(msg, errorCode, rootCause, paramValues);
	}
}
