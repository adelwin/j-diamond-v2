/**
 * File Name	: ConversionUtil.java
 * Author		: Administrator:Adelwin
 * Create Date	: Apr 9, 2006:1:28:06 PM
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

public class ConversionUtil {
	public boolean booleanValue(String boolStr) throws BaseException {
		if ("TRUE".equalsIgnoreCase(boolStr)) {
			return true;
		} else if ("Y".equalsIgnoreCase(boolStr)) {
			return true;
		} else if ("1".equalsIgnoreCase(boolStr)) {
			return true;
		} else if ("YES".equalsIgnoreCase(boolStr)) {
			return true;
		} else if (Long.parseLong(boolStr) > 0) {
			return true;
		} else if ("FALSE".equalsIgnoreCase(boolStr)) {
			return false;
		} else if ("N".equalsIgnoreCase(boolStr)) {
			return false;
		} else if ("0".equalsIgnoreCase(boolStr)) {
			return false;
		} else if ("NO".equalsIgnoreCase(boolStr)) {
			return false;
		} else if (Long.parseLong(boolStr) <= 0) {
			return false;
		} else {
			throw new BaseException("Conversion Error, unable to find a matching pattern");
		}
	}
}
 