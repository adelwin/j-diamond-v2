/*************************************************************************************/
/* Copyright ? (2000 - 2008) by Citibank N.A. */
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
 * Module Name: Batch Email Gateway File Name: UTF8MultipartMailMessage.java Description : the purpose of this class is to create a generic mechanism of sending emails
 * 
 * Program Amendment History Date Ver Author Description ----------- --- ------- -------------------------------------------- 08-JUN-1009 1.0 Adelwin Initial draft
 */
package org.si.diamond.mail.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.si.diamond.mail.exception.BaseMailException;
import org.si.diamond.mail.session.IMailSession;


/**
 * This will be the base class for all UTF-8, file-based, multipart email object<br>
 * this class extends BaseMailMessage who provides generic interface to MimeMessage class <br>
 * although the class can crunch a file to construct a MineMessage mail object, <br>
 * but it is required that one file consist of only one mail message<br>
 * the format of the mail message file is hard coded INTO the code<br>
 * the input file should look like below <BR>
 * <code> subject=RE: Infoman/VC 10899383(Activity 10899530) to=ahandoyo@ncs.com.sg to=nvinay@ncs.com.sg cc=cmtan@ncs.com.sg bcc=adelwin@gmail.com file=input/file/attachment/attachment.pdf body=content of the mail message </code> <br>
 * a few implicit requirements. <br>
 * 1. the key DATE.FORMAT, should there be any, must be placed BEFORE any date <br>
 * 2. the interface file MUST be written in UTF-8 character encoding <br>
 * 3.
 * 
 * @author ahandoyo
 */
public class UTF8MultipartMailMessage extends BaseMailMessage {

	private static final Logger	logger				= Logger.getLogger(UTF8MultipartMailMessage.class);

	/**
	 * List of possible header of the interface file
	 */
	public static final String	FIELD_FROM			= "FROM";

	public static final String	FIELD_TO			= "TO";

	public static final String	FIELD_CC			= "CC";

	public static final String	FIELD_BCC			= "BCC";

	public static final String	FIELD_SUBJECT		= "SUBJECT";

	public static final String	FIELD_SENT_DATE		= "SENT.DATE";

	public static final String	FIELD_REPLY_TO		= "REPLY.TO";

	public static final String	FIELD_BODY			= "BODY";

	public static final String	FIELD_FILE			= "FILE";

	public static final String	FIELD_DATE_FORMAT	= "DATE.FORMAT";

	public static final String	CHARSET				= "UTF-8";

	public static final String	DELIMITER			= "=";

	public static final String	CARRIAGE_RETURN		= "\n";

	private String				dateFormat;

	private static String		dateFormatDefault	= "DD-MM-YYYY";

	public UTF8MultipartMailMessage(IMailSession mailSession) {
		super(mailSession.getMailSession());
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String _dateFormat) {
		this.dateFormat = _dateFormat;
	}

	/**
	 * Method Name : load(String) this method will load a file with the name specified, this will validate the file first before passing it into another load method
	 * 
	 * @param messageFileName
	 * @author adelwin.handoyo
	 */
	public void load(String messageFileName) throws BaseMailException {
		logger.info("UTF8MultipartMailMessage.load(String) :: Loading message file from name");
		File messageFile = new File(messageFileName);
		if (!messageFile.exists()) {
			throw new BaseMailException("File doesnot exist");
		}
		if (!messageFile.isFile()) {
			throw new BaseMailException("File is not a file");
		}
		if (!messageFile.canRead()) {
			throw new BaseMailException("File unreadable.");
		}
		load(messageFile);
	}

	/**
	 * Method Name : load(String) this method loads the interface file to construct itself. the sequence of the fields in the interface file is unspecified. the implementor may put any fields in jumbled position and still built the correct object it will read the interface file as a key and value pair, according to the key found, it will set the correct field in the mail message object.
	 * 
	 * @param messageFileName
	 * @author adelwin.handoyo
	 */
	public void load(File messageFile) throws BaseMailException {
		logger.info("UTF8MultipartMailMessage.load(File) :: Loading message file");
		InputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			logger.info("UTF8MultipartMailMessage.load(File) :: Opening stream to the file");
			fileInputStream = new FileInputStream(messageFile);
			inputStreamReader = new InputStreamReader(fileInputStream, CHARSET);
			bufferedReader = new BufferedReader(inputStreamReader);
			logger.info("UTF8MultipartMailMessage.load(File) :: creating Mime Body Part for main message body");
			MimeBodyPart mainBodyPart = new MimeBodyPart();

			// read line by line.
			logger.info("UTF8MultipartMailMessage.load(File) :: reading the file");
			List<MimeBodyPart> attachmentsBodyPart = new ArrayList<MimeBodyPart>();
			String line = bufferedReader.readLine();
			while (line != null && line.trim().length() > 0) {
				logger.debug("UTF8MultipartMailMessage.load(File) :: line = " + line);
				logger.info("UTF8MultipartMailMessage.load(File) :: setting up field");
				// tokenize the String based on '=' to get key and value pair
				StringTokenizer stringTokenizer = new StringTokenizer(line, DELIMITER);
				// only get the first 2 token, key and value pair
				String key = stringTokenizer.nextToken();
				String value = stringTokenizer.nextToken();
				if (FIELD_FILE.equalsIgnoreCase(key)) {
					setAttachments(value, attachmentsBodyPart);
				} else {
					setField(key, value, mainBodyPart);
				}
				// read next line
				line = bufferedReader.readLine();
			}
			logger.info("UTF8MultipartMailMessage.load(File) :: craeting Multipart object");
			Multipart multipart = new MimeMultipart();

			logger.info("UTF8MultipartMailMessage.load(File) :: Registering mail body part to the Multipart");
			multipart.addBodyPart(mainBodyPart);

			logger.info("UTF8MultipartMailMessage.load(File) :: Registering the attachments to the Multipart");
			Iterator<MimeBodyPart> attachmentsBodyPartIterator = attachmentsBodyPart.iterator();
			while (attachmentsBodyPartIterator.hasNext()) {
				MimeBodyPart item = (MimeBodyPart) attachmentsBodyPartIterator.next();
				multipart.addBodyPart(item);
			}

			logger.info("UTF8MultipartMailMessage.load(File) :: setting up the Multipart to the message");
			this.setContent(multipart);

			logger.info("UTF8MultipartMailMessage.load(File) :: save object state");
			this.saveChanges();
		} catch (FileNotFoundException e) {
			logger.error("UTF8MultipartMailMessage.load(String) :: FileNotFoundException occured :: " + e.getMessage());
			throw new BaseMailException(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error("UTF8MultipartMailMessage.load(String) :: UnsupportedEncodingException occured :: " + e.getMessage());
			throw new BaseMailException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error("UTF8MultipartMailMessage.load(String) :: IOException occured :: " + e.getMessage());
			throw new BaseMailException(e.getMessage(), e);
		} catch (MessagingException e) {
			logger.error("UTF8MultipartMailMessage.load(String) :: MessagingException occured :: " + e.getMessage());
			throw new BaseMailException(e.getMessage(), e);
		} finally {
			try {
				logger.info("UTF8MultipartMailMessage.load(File) :: Closing stream to the message file");
				if (bufferedReader != null) bufferedReader.close();
				if (inputStreamReader != null) inputStreamReader.close();
				if (fileInputStream != null) fileInputStream.close();
				logger.info("UTF8MultipartMailMessage.load(File) :: Stream to file closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method Name : setField(String, String, MimeBodypart) 
	 * This method will set the appropriate value in the appropriate type to the appropriate field in the message object
	 * 
	 * @param key
	 * @param value
	 * @param bodyPart
	 * @return void
	 * @throws GenericMailException
	 * @author adelwin.handoyo
	 */
	private void setField(String key, String value, MimeBodyPart bodyPart) throws BaseMailException {
		logger.debug("UTF8MultipartMailMessage.setField(String, String, MimeBodyPart) :: FIELD :: " + key + " :: " + value);
		try {
			if (FIELD_FROM.equalsIgnoreCase(key)) {
				this.setFrom(new InternetAddress(value));
			}
			if (FIELD_TO.equalsIgnoreCase(key)) {
				this.addRecipients(Message.RecipientType.TO, InternetAddress.parse(value));
			}
			if (FIELD_CC.equalsIgnoreCase(key)) {
				this.addRecipients(Message.RecipientType.CC, InternetAddress.parse(value));
			}
			if (FIELD_BCC.equalsIgnoreCase(key)) {
				this.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(value));
			}
			if (FIELD_SUBJECT.equalsIgnoreCase(key)) {
				this.setSubject(value, CHARSET);
			}
			if (FIELD_DATE_FORMAT.equalsIgnoreCase(key)) {
				this.setDateFormat(value);
			}
			if (FIELD_SENT_DATE.equalsIgnoreCase(key)) {
				if (this.getDateFormat() == null || this.getDateFormat().trim().length() == 0) {
					this.setDateFormat(dateFormatDefault);
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.getDateFormat());
				this.setSentDate(simpleDateFormat.parse(value));
			}
			if (FIELD_REPLY_TO.equalsIgnoreCase(key)) {
				this.setReplyTo(InternetAddress.parse(value));
			}
			if (FIELD_BODY.equalsIgnoreCase(key)) {
				StringBuffer currentBodyContent = new StringBuffer((String) bodyPart.getContent());
				currentBodyContent.append(CARRIAGE_RETURN).append(value);
				bodyPart.setText(currentBodyContent.toString(), CHARSET);
			}
		} catch (AddressException e) {
			logger.error("UTF8MultipartMailMessage.setField(String, String, MimeBodyPart) :: AddressException occured :: " + e.getMessage());
			throw new BaseMailException(e.getMessage(), e);
		} catch (MessagingException e) {
			logger.error("UTF8MultipartMailMessage.setField(String, String, MimeBodyPart) :: MessagingException occured :: " + e.getMessage());
			throw new BaseMailException(e.getMessage(), e);
		} catch (ParseException e) {
			logger.error("UTF8MultipartMailMessage.setField(String, String, MimeBodyPart) :: ParseException occured :: " + e.getMessage());
			throw new BaseMailException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error("UTF8MultipartMailMessage.setField(String, String, MimeBodyPart) :: IOException occured :: " + e.getMessage());
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	// TODO : extend this object to automatically detect either absolute path or
	// relative path of the attachment
	/**
	 * Method Name : setAttachments(String, List) this method will handle the way this message file object takes attachments
	 * 
	 * @param attachmentFileName
	 * @param bodyPartList
	 * @return void
	 * @throws GenericMailException
	 * @author adelwin.handoyo
	 */
	private void setAttachments(String attachmentFileName, List<MimeBodyPart> bodyPartList) throws BaseMailException {
		try {
			logger.info("UTF8MultipartMailMessage.setAttachments(String, List) :: validating attachment file");
			// check if the file for attachment is valid or not
			File attachmentFile = new File(attachmentFileName);
			if (!attachmentFile.exists()) throw new BaseMailException("File does not exist");
			if (!attachmentFile.canRead()) throw new BaseMailException("File cannot be read");
			if (!attachmentFile.isFile()) throw new BaseMailException("File is not a file");
			// attach!
			logger.info("UTF8MultipartMailMessage.setAttachments(String, List) :: loading attachment file");
			FileDataSource fileDataSource = new FileDataSource(attachmentFile);
			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setDataHandler(new DataHandler(fileDataSource));
			bodyPart.setFileName(fileDataSource.getName());

			logger.info("UTF8MultipartMailMessage.setAttachments(String, List) :: adding header lines to the body part");
			bodyPart.addHeader("Content-Type", fileDataSource.getContentType());

			logger.info("UTF8MultipartMailMessage.setAttachments(String, List) :: Registering attachment body part");
			bodyPartList.add(bodyPart);
		} catch (MessagingException e) {
			logger.error("UTF8MultipartMailMessage.setAttachments(String, String, List) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}
}
