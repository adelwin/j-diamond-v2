/**
 * File Name : CommonMailMessage.java Author : adelwin.handoyo Create Date : 24-Jun-2009
 * 
 * Copyright (c) 2009 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of Adelwin. ("Confidential Information").<BR>
 * <BR>
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
 * Type Name : CommonMailMessage Package : COTSMailProject Author : adelwin.handoyo Created : AM 11:59:55 The sequence of the mail interface file is : 1. from 2. from name 3. reply to address 4. sent date <YYYYMMDDhhmmss> 5. to address, comma separated 6. subject 7. body 8. mail trailer 9. attachment required 10. attachment file 11. digital signature required 12. cc 13. bcc
 */
public class CommonMailMessage extends BaseMailMessage {

	private static final Logger	logger		= Logger.getLogger(CommonMailMessage.class);

	// this is the default character encoding of this class, this class will
	// read the interface file and construct an email object in the default
	// character encoding
	private String				CHARSET		= "UTF-8";

	// this is the default delimiter used to separate the fields in the
	// interface file
	private String				DELIMETER	= "|";

	// this is the default line feed character to be used
	private String				LINE_FEED	= "\r\n";

	/**
	 * Method Name : Constructor this is the default constructor, this will cause the constructor of the super class to be overridden
	 * 
	 * @param mailSession
	 * @author adelwin.handoyo
	 */
	public CommonMailMessage(IMailSession mailSession) {
		super(mailSession.getMailSession());
	}

	/**
	 * Method Name : load(String) this method will load a file with the name specified, this will validate the file first before passing it into another load method
	 * 
	 * @param messageFileName
	 * @author adelwin.handoyo
	 */
	public void load(String messageFileName) throws BaseMailException {
		logger.info("CommonMailMessage.load(String) :: loading message file as String");
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
	 * Method Name : load(File) this method loads the interface file to construct itself. the sequence of the fields in the interface file is unfortunately hard-coded logic.
	 * 
	 * @param messageFileName
	 * @author adelwin.handoyo
	 */
	public void load(File messageFile) throws BaseMailException {
		logger.info("CommonMailMessage.load(File) :: loading message file as File");
		InputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			logger.info("CommonMailMessage.load(File) :: Opening stream to file");
			fileInputStream = new FileInputStream(messageFile);
			inputStreamReader = new InputStreamReader(fileInputStream, CHARSET);
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer tmpMessageBuffer = new StringBuffer();
			String messageLine;

			logger.info("CommonMailMessage.load(File) :: Collecting lines");
			messageLine = bufferedReader.readLine();
			while (messageLine != null) {
				tmpMessageBuffer.append(messageLine).append(LINE_FEED);
				messageLine = bufferedReader.readLine();
			}
			logger.debug("CommonMailMessage.load(File) :: MESSAGE :: " + tmpMessageBuffer.toString());

			// parse message buffer on pipe
			logger.info("CommonMailMessage.load(File) :: Parsing message");
			StringTokenizer stringTokenizer = new StringTokenizer(tmpMessageBuffer.toString(), DELIMETER);
			// 1. from
			String fromAddress = stringTokenizer.nextToken();
			String fromName = stringTokenizer.nextToken();
			this.setFrom(new InternetAddress(fromAddress, fromName));
			logger.debug("CommonMailMessage.load(File) :: From :: " + fromName + "<" + fromAddress + ">");

			// 2. reply to address
			String replyToAddress = stringTokenizer.nextToken();
			this.setReplyTo(InternetAddress.parse(replyToAddress));
			logger.debug("CommonMailMessage.load(File) :: Reply-To :: " + replyToAddress);

			// 3. sent date
			String dateFormat = "yyyyMMddhhmmss";
			String sentDateString = stringTokenizer.nextToken();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			this.setSentDate(simpleDateFormat.parse(sentDateString));
			logger.debug("CommonMailMessage.load(File) :: Sent-Date :: " + sentDateString);

			// 4. recipient to
			String toAddresses = stringTokenizer.nextToken();
			this.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddresses));
			logger.debug("CommonMailMessage.load(File) :: RCPT-TO :: " + toAddresses);

			// 5. subject
			this.setSubject(stringTokenizer.nextToken(), CHARSET);
			logger.debug("CommonMailMessage.load(File) :: Subject :: " + this.getSubject());

			logger.info("CommonMailMessage.load(File) :: Creating Multipart object as the content of the mail");
			Multipart multipart = new MimeMultipart();

			logger.info("CommonMailMessage.load(File) :: Creating main body part, the message body");
			MimeBodyPart mainBodypart = new MimeBodyPart();

			logger.info("CommonMailMessage.load(File) :: Setting up message body headers");
			mainBodypart.addHeaderLine("method=REQUEST;charset=" + CHARSET + ";");

			String textBody = stringTokenizer.nextToken();
			String textFooter = stringTokenizer.nextToken();
			StringBuffer inputString = new StringBuffer();
			inputString.append(textBody);
			inputString.append(LINE_FEED);
			inputString.append(LINE_FEED);
			inputString.append(textFooter);
			mainBodypart.setText(inputString.toString(), CHARSET);
			logger.debug("CommonMailMessage.load(File) :: BODY :: " + inputString.toString());

			logger.info("CommonMailMessage.load(File) :: Add main body part to Multipart");
			multipart.addBodyPart(mainBodypart);

			String isAttachmentPresent = stringTokenizer.nextToken();
			if (isAttachmentPresent.equalsIgnoreCase("Y")) {
				logger.info("CommonMailMessage.load(File) :: Attachment is present, loading");
				String attachmentFileName = stringTokenizer.nextToken();
				logger.debug("CommonMailMessage.load(File) :: Attachment file name = " + attachmentFileName);
				FileDataSource fileDataSource = new FileDataSource(attachmentFileName);
				MimeBodyPart attachmentBodyPart = new MimeBodyPart();
				attachmentBodyPart.setDataHandler(new DataHandler(fileDataSource));
				attachmentBodyPart.setFileName(fileDataSource.getName());
				attachmentBodyPart.addHeader("Content-Type", fileDataSource.getContentType());
				logger.info("CommonMailMessage.load(File) :: Add attachment body part to Multipart");
				multipart.addBodyPart(attachmentBodyPart);
			} else {
				logger.info("CommonMailMessage.load(File) :: Attachment is not present");
			}

			String isDigitalSignRequired = stringTokenizer.nextToken();
			// TODO : find a library that supports email signing
			if (isDigitalSignRequired.equalsIgnoreCase("Y")) {
				logger.info("CommonMailMessage.load(File) :: Digital Signature is required, creating Signed Content");
			} else {
				logger.info("CommonMailMessage.load(File) :: Set the content with Multipart");
			}
			this.setContent(multipart);

			logger.info("CommonMailMessage.load(File) :: Get CC/BCC Fields");
			if (stringTokenizer.hasMoreTokens()) {
				this.addRecipients(Message.RecipientType.CC, InternetAddress.parse(stringTokenizer.nextToken()));
				this.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(stringTokenizer.nextToken()));
			}

			logger.info("CommonMailMessage.load(File) :: Save object state");
			this.saveChanges();
		} catch (FileNotFoundException e) {
			logger.error("CommonMailMessage.load(String) :: FileNotFoundException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error("CommonMailMessage.load(String) :: UnsupportedEncodingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error("CommonMailMessage.load(String) :: IOException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (AddressException e) {
			logger.error("CommonMailMessage.load(String) :: AddressException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (MessagingException e) {
			logger.error("CommonMailMessage.load(String) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (ParseException e) {
			logger.error("CommonMailMessage.load(String) :: ParseException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} finally {
			try {
				logger.info("CommonMailMessage.load(File) :: Closing file streams");
				if (bufferedReader != null) bufferedReader.close();
				if (inputStreamReader != null) inputStreamReader.close();
				if (fileInputStream != null) fileInputStream.close();
				logger.info("CommonMailMessage.load(File) :: File streams closed");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
