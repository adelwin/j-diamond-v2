/**
 * File Name	: CipherWrapper.java
 * Author		: Administrator:Adelwin
 * Create Date	: Mar 30, 2006:10:09:39 AM
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

import org.si.diamond.base.exception.CipherException;

public abstract class CipherWrapper {
	
	/**
	 * not designed as singleton
	 */
	public static final CipherWrapper getCipher(String cipherClass) throws CipherException {
		try {
			Class<?> clazz = Class.forName(cipherClass);
			return (CipherWrapper) clazz.newInstance();
		} catch (Exception e) {
			throw new CipherException(e.getMessage(), e);
		}
	}
	
	public abstract byte[] encrypt(byte[] input, String key) throws CipherException;
	public abstract byte[] decrypt(byte[] input, String key) throws CipherException;
}
