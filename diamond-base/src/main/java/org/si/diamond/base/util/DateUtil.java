/**
 * File Name : DateUtil.java Author : Administrator:Adelwin Create Date : Mar
 * 12, 2005:12:53:22 PM
 * 
 * Copyright (c) 2005 Adelwin. All Rights Reserved. <BR>
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

package org.si.diamond.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static final Date getTodayDate() {
		// Calendar calendar = Calendar.getInstance();
		Date retVal = new Date();
		return retVal;
	}

	public static final String getDateAsString(Date inputDate, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(inputDate);
	}

	public static final Date formatDate(String inputDate, String pattern) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(inputDate);
	}

}
