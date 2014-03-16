/**
 * File Name    : MailStore.java
 * Author       : adelwin
 * Created Date : Feb 3, 2011 1:29:36 PM
 */
package org.si.diamond.mail.store;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import org.apache.log4j.Logger;
import org.si.diamond.mail.authenticator.SMTPAuthenticator;
import org.si.diamond.mail.exception.BaseMailException;

public class MailStore implements IMailStore {
	
	protected Logger logger = Logger.getLogger(MailStore.class);
	protected String identifier;
	protected String imapHost;
	protected String imapPort;
	protected String imapUser;
	protected String imapPassword;
	protected Session imapSession;
	protected Store imapStore;
	protected Folder imapFolder;
	protected String socketFactoryClass;
	protected String socketFactoryFallback;
	
	protected Properties mailConfigurationProperties = new Properties();

	public MailStore(String _identifier) {
		this.identifier = _identifier;
		this.mailConfigurationProperties.put(MAIL_DEBUG_KEY, "false");
		logger.debug("Initiated - identifier = " + this.identifier);
		logger.warn("Initiated - compulsory configuration is missing, please set up first.");
	}
	
	public MailStore(String _identifier, String _imapHost, String _imapPort) throws BaseMailException {
		try {
			logger.debug("Initializing");
			this.identifier = _identifier;
			logger.debug("identifier = " + this.identifier);

			if (this.mailConfigurationProperties == null) this.mailConfigurationProperties = new Properties();
			this.mailConfigurationProperties.put(MAIL_IMAP_HOST, _imapHost);
			logger.debug("imap host = " + this.imapHost);
			
			if (_imapPort != null && _imapPort.trim().length() > 0) {
				this.mailConfigurationProperties.put(MAIL_IMAP_PORT, _imapPort);
			} else {
				this.mailConfigurationProperties.put(MAIL_IMAP_PORT, DEFAULT_MAIL_IMAP_PORT);
			}
			logger.debug("imap port = " + this.imapPort);

			this.mailConfigurationProperties.put(MAIL_IMAP_SOCKETFACTORY_CLASS, DEFAULT_MAIL_IMAP_SOCKETFACTORY_CLASS);
			this.mailConfigurationProperties.put(MAIL_IMAP_SOCKETFACTORY_FALLBACK, DEFAULT_MAIL_IMAP_SOCKETFACTORY_FALLBACK);
			this.mailConfigurationProperties.put(MAIL_IMAP_STORE_PROTOCOL, DEFAULT_MAIL_MAP_STORE_PROTOCOL);
			
			logger.debug("Creating iMAP Session");
			this.imapSession = Session.getInstance(this.mailConfigurationProperties, null);

			logger.debug("Creating Mail Store object");
			this.imapStore = this.imapSession.getStore();
			logger.debug("Mail Store Obtained");
			logger.debug("Initialized");
		} catch (NoSuchProviderException e) {
			logger.error("MailSession Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}
	
	public MailStore(String _identifier, Properties _mailConfigurationProperties) throws BaseMailException {
		try {
			logger.debug("Initializing");
			this.identifier = _identifier;
			logger.debug("identifier = " + this.identifier);

			this.mailConfigurationProperties = _mailConfigurationProperties;
			this.imapHost = this.mailConfigurationProperties.getProperty(MAIL_IMAP_HOST);
			logger.debug("imap host = " + this.imapHost);

			this.imapPort = this.mailConfigurationProperties.getProperty(MAIL_IMAP_PORT);
			logger.debug("imap port = " + this.imapPort);

			this.mailConfigurationProperties.put(MAIL_IMAP_SOCKETFACTORY_CLASS, DEFAULT_MAIL_IMAP_SOCKETFACTORY_CLASS);
			this.mailConfigurationProperties.put(MAIL_IMAP_SOCKETFACTORY_FALLBACK, DEFAULT_MAIL_IMAP_SOCKETFACTORY_FALLBACK);
			this.mailConfigurationProperties.put(MAIL_IMAP_STORE_PROTOCOL, DEFAULT_MAIL_MAP_STORE_PROTOCOL);
			
			logger.debug("Creating iMAP Session");
			this.imapSession = Session.getInstance(this.mailConfigurationProperties, null);

			logger.debug("Creating Mail Store object");
			this.imapStore = this.imapSession.getStore();
			logger.debug("Mail Store Obtained");
			logger.debug("Initialized");
		} catch (NoSuchProviderException e) {
			logger.error("MailStore Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	public MailStore(String _identifier, String _mailConfigurationPropertiesFileName, SMTPAuthenticator authenticator) throws BaseMailException {
		try {
			logger.debug("Loading mail configuration properties");
			logger.debug("configuration properties = " + _mailConfigurationPropertiesFileName);
			Properties props = new Properties();
			props.load(this.getClass().getClassLoader().getResourceAsStream(_mailConfigurationPropertiesFileName));
			
			logger.info("Initializing");
			this.identifier = _identifier;
			logger.debug("identifier = " + this.identifier);

			this.mailConfigurationProperties = props;
			this.imapHost = this.mailConfigurationProperties.getProperty(MAIL_IMAP_HOST);
			logger.debug("imap host = " + this.imapHost);

			this.imapPort = this.mailConfigurationProperties.getProperty(MAIL_IMAP_PORT);
			logger.debug("imap port = " + this.imapPort);
			
			this.imapUser = authenticator.getUserName();
			this.imapPassword = authenticator.getPassword();

			this.mailConfigurationProperties.put(MAIL_IMAP_SOCKETFACTORY_CLASS, DEFAULT_MAIL_IMAP_SOCKETFACTORY_CLASS);
			this.mailConfigurationProperties.put(MAIL_IMAP_SOCKETFACTORY_FALLBACK, DEFAULT_MAIL_IMAP_SOCKETFACTORY_FALLBACK);
			this.mailConfigurationProperties.put(MAIL_IMAP_STORE_PROTOCOL, DEFAULT_MAIL_MAP_STORE_PROTOCOL);
			
			logger.debug("Creating iMAP Session");
			this.imapSession = Session.getInstance(this.mailConfigurationProperties, null);

			logger.debug("Creating Mail Store object");
			this.imapStore = this.imapSession.getStore();
			logger.debug("Mail Store Obtained");
			logger.debug("Initialized");
		} catch (IOException e) {
			logger.error("MailStore Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (NoSuchProviderException e) {
			logger.error("MailStore Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		}
		
	}
	
	public MailStore(String _identifier, Properties _mailConfigurationProperties, SMTPAuthenticator authenticator) throws BaseMailException {
		try {
			logger.debug("Initializing");
			this.identifier = _identifier;
			logger.debug("identifier = " + this.identifier);

			this.mailConfigurationProperties = _mailConfigurationProperties;
			this.imapHost = this.mailConfigurationProperties.getProperty(MAIL_IMAP_HOST);
			logger.debug("imap host = " + this.imapHost);

			this.imapPort = this.mailConfigurationProperties.getProperty(MAIL_IMAP_PORT);
			logger.debug("imap port = " + this.imapPort);

			this.imapUser = authenticator.getUserName();
			this.imapPassword = authenticator.getPassword();

			this.mailConfigurationProperties.put(MAIL_IMAP_SOCKETFACTORY_CLASS, DEFAULT_MAIL_IMAP_SOCKETFACTORY_CLASS);
			this.mailConfigurationProperties.put(MAIL_IMAP_SOCKETFACTORY_FALLBACK, DEFAULT_MAIL_IMAP_SOCKETFACTORY_FALLBACK);
			this.mailConfigurationProperties.put(MAIL_IMAP_STORE_PROTOCOL, DEFAULT_MAIL_MAP_STORE_PROTOCOL);
			
			logger.debug("Creating iMAP Session");
			this.imapSession = Session.getInstance(this.mailConfigurationProperties, authenticator);

			logger.debug("Creating Mail Store object");
			this.imapStore = this.imapSession.getStore();
			logger.debug("Mail Store Obtained");
			logger.debug("Initialized");
		} catch (Exception e) {
			logger.error("MailSession(String, Properties, Authenticator) :: MailSession Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	@Override
	public void connect() throws BaseMailException {
		logger.debug("Connecting");
		try {
			if (this.imapUser != null && this.imapUser.trim().length() > 0 && this.imapPassword != null && this.imapPassword.trim().length() > 0) {
				logger.debug("Parameterised connection");
				this.connect(this.imapUser, this.imapPassword);
			} else {
				logger.debug("Plain, Non Parameterised connection");
				this.imapStore.connect();
			}
		} catch (MessagingException e) {
			logger.error("MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	@Override
	public void connect(String _imapUser, String _imapPassword) throws BaseMailException, IllegalStateException {
		logger.debug("Connecting");
		this.imapUser = _imapUser;
		this.imapPassword = _imapPassword;
		try {
			logger.debug("Mail Store Connection");
			this.imapStore.connect(this.imapHost, this.imapUser, this.imapPassword);
		} catch (MessagingException e) {
			logger.error("MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (IllegalStateException e) {
			logger.debug("Already connected");
		}
	}

	@Override
	public void disconnect() throws BaseMailException {
		logger.debug("Starting");
		try {
			logger.debug("Disposing Mail Store Object");
			this.imapStore.close();
		} catch (MessagingException e) {
			logger.error("MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	@Override
	public String getIdentifier() {
		return this.identifier;
	}

	@Override
	public Properties getMailConfigurationProperties() {
		return this.mailConfigurationProperties;
	}
	
	public void setMailConfigurationProperties(Properties _mailConfigurationProperties) {
		this.mailConfigurationProperties = _mailConfigurationProperties;
	}

	@Override
	public Session getMailSession() {
		return this.imapSession;
	}
	
	public void setMailSession(Session _mailSession) {
		this.imapSession = _mailSession;
	}

	@Override
	public Message[] getMessages(int mode, String folderName) throws BaseMailException {
		logger.debug("Retrieving mail from imap store");
		if (this.imapStore == null) {
			logger.debug("iMAP Store object is null");
			try {
				logger.debug("Retrieving new iMap Store Object");
				this.imapStore = this.imapSession.getStore();
			} catch (NoSuchProviderException e) {
				logger.error("NoSuchProviderException occured :: " + e.getMessage(), e);
				throw new BaseMailException(e.getMessage(), e);
			}
		}
		try {
			if (!this.imapStore.isConnected()) {
				logger.debug("Mail Store object is not connected, connecting");
				this.connect();
			}
			logger.debug("Retrieving Folder");
			Folder folder = this.imapStore.getFolder(folderName);
			folder.open(mode);
			return folder.getMessages();
		} catch (MessagingException e) {
			logger.error("MailSession.transportStaticSend(MimeMessage) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (BaseMailException e) {
			logger.error("MailSession.transportStaticSend(MimeMessage) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	@Override
	public Message[] getMessages(int mode, String folderName, SearchTerm searchTerm) throws BaseMailException {
		if (searchTerm == null) {
			return this.getMessages(mode, folderName);
		}
		logger.debug("Retrieving mail from imap store");
		if (this.imapStore == null) {
			logger.debug("iMAP Store object is null");
			try {
				logger.debug("Retrieving new iMap Store Object");
				this.imapStore = this.imapSession.getStore();
			} catch (NoSuchProviderException e) {
				logger.error("NoSuchProviderException occured :: " + e.getMessage(), e);
				throw new BaseMailException(e.getMessage(), e);
			}
		}
		try {
			if (!this.imapStore.isConnected()) {
				logger.debug("Mail Store object is not connected, connecting");
				this.connect();
			}
			logger.debug("Retrieving Folder");
			Folder folder = this.imapStore.getFolder(folderName);
			folder.open(mode);
			return folder.search(searchTerm);
		} catch (MessagingException e) {
			logger.error("MailSession.transportStaticSend(MimeMessage) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		} catch (BaseMailException e) {
			logger.error("MailSession.transportStaticSend(MimeMessage) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	@Override
	public void reconnect() throws BaseMailException {
		logger.debug("Disconnect");
		this.disconnect();
		logger.debug("Connecting");
		try {
			this.connect();
		} catch (IllegalStateException e) {
			logger.debug("Allready Connected");
		}
		logger.debug("Connected");
	}

}
