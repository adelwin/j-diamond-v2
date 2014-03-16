/**
 * File Name	: ContextUtil.java
 * Author		: Administrator:Adelwin
 * Create Date	: Mar 18, 2006:10:34:01 AM
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

package org.si.diamond.base.util;

import org.si.diamond.base.exception.BaseException;

public abstract class ContextUtil {
	private static org.springframework.context.ApplicationContext springContext = null;
	private static org.si.diamond.base.context.ApplicationContext diamondContext = null;
	
	public static org.springframework.context.ApplicationContext getSpringContext() {
		return springContext;
	}
	
	public static void setSpringContext(org.springframework.context.ApplicationContext springContext) throws BaseException {
		if (ContextUtil.springContext == null) {
			ContextUtil.springContext = springContext;
		} else {
			throw new BaseException("context already loaded");
		}
	}
	
	public static org.si.diamond.base.context.ApplicationContext getDiamondContext() {
		return diamondContext;
	}
	
	public static void setDiamondContext(org.si.diamond.base.context.ApplicationContext diamondConfigMap) throws BaseException {
		if (ContextUtil.diamondContext == null) {
			ContextUtil.diamondContext = diamondConfigMap;
		} else {
			throw new BaseException("config map already exist");
		}
	}
}
