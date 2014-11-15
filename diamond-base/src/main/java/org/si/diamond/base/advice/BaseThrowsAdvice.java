/**
 * File Name : GenericThrowsAdvice.java Author : Administrator:Adelwin Create
 * Date : Mar 25, 2006:9:38:12 AM
 * 
 * Copyright (c) 2006 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of Adelwin.
 * ("Confidential Information").<BR>
 * <BR>
 * Such Confidential Information shall not be disclosed and it shall only be
 * used in accordance with the terms of the license agreement entered into with
 * Solveware Independent; other than in accordance with the written permission
 * of Solveware Independent. <BR>
 * 
 * 
 */

/**
 * an advice inherited from spring advice for advance logging of exception
 * thrown every exception thrown is always encapsulated inside PackageResponse
 * so exception wont explode the page this class is only for logging all the
 * exception thrown by any class logs the type of exception, message, result,
 * user running the method, method, param, audit trail to make it easier to trap
 * the error
 */
package org.si.diamond.base.advice;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.aop.ThrowsAdvice;

public class BaseThrowsAdvice implements ThrowsAdvice {

	private static final long	serialVersionUID	= -992405676130788361L;

	private static Logger		logger				= null;

	/**
	 * logs the exception thrown
	 */
	public void afterThrowing(Method method, Object[] args, Object target, Throwable t) {
		logger = Logger.getLogger(target.getClass());
		logger.error("Exception thrown from " + target.getClass() + " running method " + method.getName(), t);
		logger.error("---- Params given : ");
		for (int i = 0; i < args.length; i++) {
			logger.error("---- ---- " + (args[i] != null ? args[i].getClass().getName() : "null") + ":" + (args[i] != null ? args[i].toString() : "null"));
		}
		logger.error(t.getStackTrace());
		t.printStackTrace();
	}
}
