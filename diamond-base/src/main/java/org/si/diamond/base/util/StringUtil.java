/**
 * File Name : StringUtil.java Author : Administrator:Adelwin Create Date : Mar
 * 12, 2005:12:40:35 PM
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StringUtil {

	public static String	EMPTY_STRING	= "";

	public static int		PAD_INFRONT		= 0;

	public static int		PAD_ATREAR		= 1;

	public static final boolean isEmptyString(String string) {
		if (string == null) return true;
		if (string.length() == 0) return true;
		return false;
	}

	public static final Character[] convertToCharArray(String string) {
		int length = string.length();
		char[] stringChar = string.toCharArray();
		Character[] retVal = new Character[length];
		for (int i = 0; i < length; i++) {
			retVal[i] = new Character(stringChar[i]);
		}
		return retVal;
	}

	public static final char[] convertToNativeCharArray(Character[] chars) {
		int length = chars.length;
		char[] chr = new char[length];
		for (int i = 0; i < length; i++) {
			chr[i] = chars[i].charValue();
		}
		return chr;
	}

	public static final String padString(String input, int length, String pad, int padPosition) {
		String retVal = new String();
		for (int i = 0; i < length; i++) {
			retVal += pad;
		}
		if (padPosition == PAD_INFRONT) {
			retVal = retVal + input;
			retVal = retVal.substring(retVal.length() - length);
		} else if (padPosition == PAD_ATREAR) {
			retVal = input + retVal;
			retVal = retVal.substring(0, length);
		}
		return retVal;
	}

	public static String createCamelCase(String input, String splitter) {
		String retVal = "";
		input = input.toLowerCase();
		// split the string by splitter
		String[] words = input.split(splitter);
		// change the first letter of each words to upper case
		// and concatenates back the word to retVal
		for (int i = 0; i < words.length; i++) {
			char[] word = words[i].toCharArray();
			word[0] = Character.toUpperCase(word[0]);
			retVal += new String(word);
		}
		return retVal;
	}

	public static boolean contains(String strLong, String strSmall) {
		if (strLong.indexOf(strSmall) < 0) return false;
		return true;
	}

	public static boolean containsIgnoreCase(String strLong, String strSmall) {
		strLong = strLong.toUpperCase();
		strSmall = strSmall.toUpperCase();
		if (strLong.indexOf(strSmall) < 0) return false;
		return true;
	}

	/**
	 * This method converts the byte array from the <b>currentEncoding</b>
	 * format to the <b>desiredEncoding</b> format When a character in the byte
	 * array matches the <b>errorMatch</b> , the <b>errorString</b> is printed
	 * 
	 * @param byte[] input
	 * @param String
	 *            currentEncoding
	 * @param String
	 *            desiredEncoding
	 * @param char errorMatch
	 * @param String
	 *            errorString
	 * @return byte[]
	 */
	public static byte[] convert(byte[] input, String currentEncoding, String desiredEncoding, char errorMatch, String errorString) throws IOException {

		ByteArrayInputStream bis = new ByteArrayInputStream(input);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		InputStreamReader in = new InputStreamReader(bis, currentEncoding);
		OutputStreamWriter out = new OutputStreamWriter(bos, desiredEncoding);
		for (int ch; (ch = in.read()) != -1;) {
			out.write(ch);
			if (ch == errorMatch) System.err.println(errorString);
		}
		out.close();
		return bos.toByteArray();
	}

}
