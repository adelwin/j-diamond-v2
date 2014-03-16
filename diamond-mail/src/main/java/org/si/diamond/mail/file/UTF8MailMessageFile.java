/*************************************************************************************/
/* Copyright ? (2000 - 2009) by Citibank N.A. */
/*                                                                                   */
/* All rights reserved. These materials are confidential and proprietary to */
/* Citibank N.A. and no part of these materials should be reproduced, */
/* published in any form by means, electronic or mechanical including */
/* photocopy or any information storage or retrieval system nor should */
/* materials be disclosed to third parties without the express written */
/* authorization of Citibank N.A. */
/*                                                                                   */
/*************************************************************************************/

/**
 * Module Name: Batch Email Gateway File Name: UTF8MailMessageFile.java
 * 
 * Program Amendment History Date Ver Author Description ----------- --- -------
 * -------------------------------------------- 08-JUN-1009 1.0 Adelwin Initial
 * draft
 */
package org.si.diamond.mail.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.si.diamond.mail.exception.BaseMailException;


/**
 * this class will hold the mail message file this class will load the file and
 * parse it line by line as a UTF 8 file and pass the read line into MailMessage
 * class to process it into an email. <code>
 * 		MailMessageFile messageFile = new UTF8MessageFile("input/inputFile.msg");
 * 		while (messageFile.hasNextMessage()) {
 * 			MimeMessage message = new PipeSeparatedCOTSMailMessage();
 * 			String messageLine = messageFile.nextMessage();
 * 			message.load(messageLine);
 * 		}  
 * </code>
 * 
 * @author ahandoyo
 */
public class UTF8MailMessageFile implements MailMessageFile {

	private static final Logger	logger	= Logger.getLogger(UTF8MailMessageFile.class);

	private static final String	CHARSET	= "UTF-8";

	private BufferedReader		bufferedReader;

	private String				nextLine;

	public void load(String messageFileName) throws BaseMailException {
		try {
			File messageFile = new File(messageFileName);
			InputStream fileInputStream = new FileInputStream(messageFile);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, CHARSET);
			this.bufferedReader = new BufferedReader(inputStreamReader);
		} catch (FileNotFoundException e) {
			logger.error("UTF8MailMessageFile.load() :: FileNotFoundException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error("UTF8MailMessageFile.load() :: UnsupportedEncodingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	public boolean hasNextMessage() throws BaseMailException {
		if (this.nextLine == null || this.nextLine.trim().length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public String nextMessage() throws BaseMailException {
		try {
			String tmpNextLine = this.bufferedReader.readLine();
			String returnVal = this.nextLine;
			this.nextLine = tmpNextLine;
			return returnVal;
		} catch (IOException e) {
			logger.error("UTF8MailMessageFile.load() :: IOException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

}
