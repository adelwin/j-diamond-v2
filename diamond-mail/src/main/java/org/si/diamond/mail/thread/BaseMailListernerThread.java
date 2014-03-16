/**
 * File Name : BaseMailListernerThread.java Author : adelwin.handoyo Create Date
 * : 24-Jun-2009
 * 
 * Copyright (c) 2009 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of Adelwin.
 * ("Confidential Information").<BR>
 * <BR>
 */
package org.si.diamond.mail.thread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;

import org.apache.log4j.Logger;
import org.si.diamond.base.exception.BaseException;
import org.si.diamond.base.util.FileUtil;
import org.si.diamond.mail.exception.BaseMailException;
import org.si.diamond.mail.message.BaseMailMessage;
import org.si.diamond.mail.session.MailSession;


/**
 * Type Name : BaseMailListernerThread Package : COTSMailProject Author :
 * adelwin.handoyo Created : PM 05:54:00
 */
public class BaseMailListernerThread extends Thread implements MailListenerThread {

	private static final Logger	logger							= Logger.getLogger(BaseMailListernerThread.class);

	private Properties			mailConfiguration				= new Properties();

	private MailSession			mailSession;

	private File				listenDirectory;

	private long				listenInterval;

	private String				mailClass;

	private Authenticator		authenticator;

	private boolean				kill							= false;

	private static final String	KEY_NAME						= "name";

	// private static final String KEY_MAIL_SMTP_HOST = "mail.smtp.host";

	// private static final String KEY_MAIL_DEBUG = "mail.debug";

	// private static final String KEY_MAIL_SMTP_AUTH = "mail.smtp.auth";

	private static final String	KEY_MAIL_TRANSPORT_STATIC_TYPE	= "mail.transport.static.type";

	private static final String	KEY_MAIL_SEND_TRESHOLD			= "mail.send.treshold";

	private static final String	KEY_MAIL_CLASS					= "mail.class";

	private static final String	KEY_MAIL_LISTEN_INTERVAL		= "mail.pickup.interval";

	private static final String	KEY_MAIL_LISTEN_DIRECTORY		= "mail.listen.directory";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.thread.MailListenerThread#destroy()
	 * 
	 * @deprecated : only usable when the implementor still has control over the
	 * thread. otherwise, it's safer to kill the process from shell
	 */
	public void destroy() {
		try {
			logger.info("BaseMailListernerThread.destroy(void) :: " + this.getName() + " :: Destroying Thread");
			logger.info("BaseMailListernerThread.destroy(void) :: " + this.getName() + " :: Disconnecting Mail Session");
			this.mailSession.disconnect();
			logger.info("BaseMailListernerThread.destroy(void) :: " + this.getName() + " :: Mail Session Disconnected");

			logger.info("BaseMailListernerThread.destroy(void) :: " + this.getName() + " :: Sending Kill Signal");
			this.kill = true;
		} catch (BaseMailException e) {
			logger.error("BaseMailListernerThread.destroy(String) :: " + this.getName() + " :: GenericMailException occured :: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.thread.MailListenerThread#init()
	 */
	public void init() throws BaseMailException {
		// create mail session
		logger.debug("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Initializing " + new Date(System.currentTimeMillis()).toString());
		if (this.authenticator == null) {
			logger.info("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Not Authenticating");
			this.mailSession = new MailSession(this.getName(), mailConfiguration);
		} else {
			logger.info("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Authenticating");
			this.mailSession = new MailSession(this.getName(), mailConfiguration, authenticator);
		}
		logger.info("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Set extra configuration");
		logger.debug("BaseMailListernerThread.init(void) :: " + this.getName() + " :: tranport static type = " + mailConfiguration.getProperty(KEY_MAIL_TRANSPORT_STATIC_TYPE));
		this.mailSession.setTransportStaticType(mailConfiguration.getProperty(KEY_MAIL_TRANSPORT_STATIC_TYPE));

		logger.debug("BaseMailListernerThread.init(void) :: " + this.getName() + " :: mail treshold = " + mailConfiguration.getProperty(KEY_MAIL_SEND_TRESHOLD));
		this.mailSession.setSendCounterThreshold(Integer.parseInt(mailConfiguration.getProperty(KEY_MAIL_SEND_TRESHOLD)));

		logger.debug("BaseMailListernerThread.init(void) :: " + this.getName() + " :: listen interval = " + mailConfiguration.getProperty(KEY_MAIL_LISTEN_INTERVAL));
		this.listenInterval = Long.parseLong(mailConfiguration.getProperty(KEY_MAIL_LISTEN_INTERVAL));

		logger.debug("BaseMailListernerThread.init(void) :: " + this.getName() + " :: mail message class = " + mailConfiguration.getProperty(KEY_MAIL_CLASS));
		this.mailClass = mailConfiguration.getProperty(KEY_MAIL_CLASS);

		// create directory to listen
		logger.info("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Registering directory to listen ");
		logger.debug("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Directory = " + mailConfiguration.getProperty(KEY_MAIL_LISTEN_DIRECTORY));
		this.listenDirectory = new File(mailConfiguration.getProperty(KEY_MAIL_LISTEN_DIRECTORY));
		if (this.listenDirectory.isDirectory() == false) {
			throw new BaseMailException("Path is not a directory");
		}
		logger.info("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Directory registered");

		logger.info("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Connecting Mail Session");
		this.mailSession.connect();
		logger.info("BaseMailListernerThread.init(void) :: " + this.getName() + " :: Mail Session Connected");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.thread.MailListenerThread#load(java.lang.String)
	 */
	public void load(String configurationFileName) throws BaseMailException {
		try {
			// load the properties file
			logger.debug("BaseMailListernerThread.load(String) :: " + this.getName() + " :: Loading specific configuratin file = " + configurationFileName);
			logger.info("BaseMailListernerThread.load(String) :: " + this.getName() + " :: Opening file");
			URL configFileUrl = ClassLoader.getSystemResource("smtp/" + configurationFileName);

			logger.info("BaseMailListernerThread.load(String) :: " + this.getName() + " :: Loading file");
			load(configFileUrl.openStream());

			logger.info("BaseMailListernerThread.load(String) :: " + this.getName() + " :: Creating Authentication Class");
			String authenticatorClass = this.mailConfiguration.getProperty("mail.authenticator");
			if (authenticatorClass == null || authenticatorClass.trim().length() == 0) {
				logger.info("BaseMailListernerThread.load(String) :: " + this.getName() + " :: No authentication required");
				this.authenticator = null;
			} else {
				logger.info("BaseMailListernerThread.load(String) :: " + this.getName() + " :: Authentication required");
				this.authenticator = (Authenticator) Class.forName(authenticatorClass).newInstance();
			}
		} catch (IOException e) {
			logger.error("BaseMailListernerThread.load(String) :: " + this.getName() + " :: IOException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (InstantiationException e) {
			logger.error("BaseMailListernerThread.load(String) :: " + this.getName() + " :: InstantiationException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error("BaseMailListernerThread.load(String) :: " + this.getName() + " :: IllegalAccessException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error("BaseMailListernerThread.load(String) :: " + this.getName() + " :: ClassNotFoundException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.thread.MailListenerThread#load(java.io.File)
	 */
	public void load(InputStream configurationFile) throws BaseMailException {
		try {
			// load smtp configuration
			logger.info("BaseMailListernerThread.load(InputStream) :: Loading configuration file");
			this.mailConfiguration.load(configurationFile);

			logger.info("BaseMailListernerThread.load(String) :: " + this.getName() + " :: Set Thread name");
			this.setName(this.mailConfiguration.getProperty(KEY_NAME));
		} catch (IOException e) {
			logger.error("BaseMailListernerThread.load(InputStream) :: " + this.getName() + " :: IOException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("BaseMailListernerThread.run(void) :: " + this.getName() + " :: Starting thread run");
		while (!kill) {
			logger.info("BaseMailListernerThread.load(void) :: " + this.getName() + " :: No kill signal");
			logger.info("BaseMailListernerThread.run(void) :: " + this.getName() + " :: Processing directory");
			process();
			try {
				logger.info("BaseMailListernerThread.run(void) :: " + this.getName() + " :: Thread sleep");
				sleep(this.listenInterval);
			} catch (InterruptedException e) {
				logger.error("BaseMailListernerThread.run(void) :: " + this.getName() + " :: InterruptedException occured :: " + e.getMessage(), e);
			}
		}
	}

	private void process() {
		try {
			logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: Starting to process directory");
			logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: Listing out files");
			File interfaceFile[] = this.listenDirectory.listFiles();
			File backupFile;

			logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: Preparing mail message class");
			Class<?> mailInterfaceClass = Class.forName(mailClass);
			BaseMailMessage mailMessage;

			// retrieve constructor
			logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: retrieving constructor for mail message class");
			Constructor<?> constructor = mailInterfaceClass.getConstructors()[0];

			// prepare init argument of the constructor
			logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: preparing init argument class for constructor");
			Object initArguments[] = new Object[1];
			initArguments[0] = this.mailSession;

			logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: Iterating file list");
			for (int i = 0; i < interfaceFile.length; i++) {
				// check if it is a file or a directory
				if (!interfaceFile[i].isDirectory()) {
					// backup the file
					logger.debug("BaseMailListernerThread.process(void) :: " + this.getName() + " :: " + interfaceFile[i].getName() + " :: backing up interface file");
					String backupFileName = interfaceFile[i].getName();
					backupFileName = this.listenDirectory + "/temp/" + backupFileName;
					backupFile = new File(backupFileName);
					FileUtil.fileStreamCopy(interfaceFile[i], backupFile);

					// create mail message object from the backup file
					logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: " + backupFileName + " :: creating mail message object from backup file");
					mailMessage = (BaseMailMessage) constructor.newInstance(initArguments);
					mailMessage.load(backupFile);

					logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: " + backupFileName + " :: sending mail");
					this.mailSession.send(mailMessage);
				} else {
					logger.info("BaseMailListernerThread.process(void) :: " + this.getName() + " :: " + interfaceFile[i].getName() + " :: is a directory, skipping");
				}
			}
		} catch (ClassNotFoundException e) {
			logger.error("BaseMailListernerThread.process(void) :: " + this.getName() + " :: ClassNotFoundException occured :: " + e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error("BaseMailListernerThread.process(void) :: " + this.getName() + " :: IllegalArgumentException occured :: " + e.getMessage(), e);
		} catch (InstantiationException e) {
			logger.error("BaseMailListernerThread.process(void) :: " + this.getName() + " :: InstantiationException occured :: " + e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error("BaseMailListernerThread.process(void) :: " + this.getName() + " :: IllegalAccessException occured :: " + e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error("BaseMailListernerThread.process(void) :: " + this.getName() + " :: InvocationTargetException occured :: " + e.getMessage(), e);
		} catch (BaseMailException e) {
			logger.error("BaseMailListernerThread.process(void) :: " + this.getName() + " :: GenericMailException occured :: " + e.getMessage(), e);
		} catch (BaseException e) {
			logger.error("BaseMailListernerThread.process(void) :: " + this.getName() + " :: GenericCoreException occured :: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("BaseMailListernerThread.process(void) :: " + this.getName() + " :: IOException occured :: " + e.getMessage(), e);
		}
	}
}
