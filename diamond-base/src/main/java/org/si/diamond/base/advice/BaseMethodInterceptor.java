/**
 * File Name	: GenericMethodInterceptor.java
 * Author		: Administrator:Adelwin
 * Create Date	: Mar 25, 2006:9:32:08 AM
 *
 * Copyright (c) 2006 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of
 * Adelwin. ("Confidential Information").<BR>
 * <BR>
 * Such Confidential Information shall not be disclosed and it shall
 * only be used in accordance with the terms of the license agreement
 * entered into with Firium; other than in accordance with the written
 * permission of Firium. <BR>
 *
 *
 */

package org.si.diamond.base.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

public class BaseMethodInterceptor implements MethodInterceptor {

	private Logger logger = null;

	/**
	 * implemented for advance logging
	 * TODO: implement method access authentication based from login context
	 * schema :
	 * 	- get the method params
	 * 	- get login context
	 * 	- get the user code and role
	 * 	- user role must be higher or at least match the required role for running the method
	 * 	- the required role for running the method will be stored in each object able to be called
	 *    but assigned from app load by using spring config 
	 */
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		logger = Logger.getLogger(methodInvocation.getThis().getClass());
		logger.debug("Trying to run method " + methodInvocation.getMethod().getName() + " from accessor class " + methodInvocation.getThis().getClass().getName());
		Object returnResult =  methodInvocation.proceed();
		logger.debug("Method execution complete");
		return returnResult;
	}

}
