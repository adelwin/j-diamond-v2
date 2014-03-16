/**
 * Module Name: Batch Email Gateway 
 * File Name: MailSession.java 
 * Description : the purpose of this class is to create a generic mechanism of sending emails
 * 
 * Program Amendment History 
 * Date        Ver Author  Description 
 * ----------- --- ------- -------------------------------------------- 
 * 08-JUN-1009 1.0 Adelwin Initial draft
 */
package org.si.diamond.mail.session;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.si.diamond.mail.exception.BaseMailException;


/**
 * This class will hold the most common configuration and transport mechanism for sending emails. 
 * email messages will be built outside of this class but will then be passed into this class to be sent. 
 * Those who wish to make use of this class needs to provide the compulsory configuration for JavaMail. 
 * This class will initialize all of the most common configuration and create the main session. 
 * It is recommended that one MailSessionObject will be the default mechanism to send an email over one particular SMTP, <br>
 * if it is required to send via another SMTP Host which is not defined in this class, <br>
 * then it is recomended to create another instance of this class
 * <code> 
 * 	MailSession mailSession = new MailSession("COTS_EOD", "spartan.ncs.com.sg", null); 
 * 	mailSession.connect("ahandoyo", "MailPassword0"); 
 * 	-- create COTSMailMessage here as mailMessage 
 * 	mailSession.send(mailMessage); 
 * </code> 
 * This class is designed to be collected to a HashMap to make it faster and easier to send batch emails over multiple transport.
 * <code> 
 * 	Map mailSessionMap = new HashMap(); 
 * 	MailSession mailSessionEOD = new MailSession("COTS_EOD"); 
 * 	mailSessionMap.put(mailSessionEOD.getIdentifier(), mailSessionEOD); 
 * 	MailSession mailSessionIOD = new MailSession("COTS_IOD"); 
 * 	mailSessionMap.put(mailSessionIOD.getIdentifier(), mailSessionIOD); 
 * </code> 
 * This means later on when the caller wishes to retrieve some mailSession object, 
 * they can just retrieve from a Map. n order to reduce overhead on creating new session and transport object all over again, 
 * it is advised to use an easy to relate identifier, like an SMTP Name, or maybe SubModule as an identifier.
 * 
 * @author ahandoyo
 */
public class MailSession implements IMailSession {

	private static final Logger	logger							= Logger.getLogger(MailSession.class);

	private String				identifier;

	private String				smtpHost;

	private String				smtpPort;

	private String				smtpUserLogin;

	private String				smtpPassword;

	private Session				mailSession;

	private Transport			mailTransport;

	private String				transportStaticType;

	private int					sendCounter;

	private int					sendCounterThreshold			= 500;

	/**
	 * the common configuration properties file that JavaMail takes as an input to build the transport
	 */
	private Properties			mailConfigurationProperties		= new Properties();

	/**
	 * Session Transport Type
	 */
	public static final String	SESSION_TRANSPORT_TYPE_SMTP		= "smtp";

	public static final String	SESSION_TRANSPORT_TYPE_DEFAULT	= SESSION_TRANSPORT_TYPE_SMTP;

	/**
	 * the usage of Transport object, as a static object of as an instance
	 */
	public static final String	TRANSPORT_STATIC_TRUE			= "true";

	public static final String	TRANSPORT_STATIC_FALSE			= "false";

	public static final String	TRANSPORT_STATIC_DEFAULT		= TRANSPORT_STATIC_FALSE;

	/**
	 * Method Name : Constructor 
	 * this is the constructor with least parameter. 
	 * the object created by this constructor will not be usable, 
	 * the user needs to set a few parameters manually before using it.
	 * 
	 * @author ahandoyo
	 * @param identifier
	 */
	public MailSession(String _identifier) {
		this.identifier = _identifier;
		this.mailConfigurationProperties.put(MAIL_DEBUG_KEY, "false");
		logger.debug("MailSession :: MailSession(String) Initiated - identifier = " + this.identifier);
		logger.warn("MailSession :: MailSession(String) Initiated - compulsory configuration is missing, please set up first.");
	}

	/**
	 * Method Name : Constructor 
	 * this is the constructor with the least parameter needed. 
	 * the object created by this constructor will be directly usable
	 * 
	 * @author ahandoyo
	 * @param _identifier
	 * @param _smtpHost
	 * @param _smtpPort
	 * @throws GenericMailException
	 */
	public MailSession(String _identifier, String _smtpHost, String _smtpPort) throws BaseMailException {
		try {
			logger.info("MailSession(String, String, String) :: Initializing");
			this.identifier = _identifier;
			logger.debug("MailSession(String, String, String) :: identifier = " + this.identifier);

			if (this.mailConfigurationProperties == null) this.mailConfigurationProperties = new Properties();
			this.mailConfigurationProperties.put(MAIL_SMTP_HOST_KEY, _smtpHost);
			logger.debug("MailSession(String, String, String) :: smtp host = " + this.smtpHost);
			if (_smtpPort != null && _smtpPort.trim().length() > 0) {
				this.mailConfigurationProperties.put(MAIL_SMTP_PORT_KEY, _smtpPort);
				logger.debug("MailSession(String, String, String) :: smtp port = " + this.smtpPort);
			}

			logger.info("MailSession(String, String, String) :: Creating SMTP Session");
			this.mailSession = Session.getInstance(this.mailConfigurationProperties, null);

			logger.info("MailSession(String, String, String) :: Creating SMTP Mail Transport object");
			this.mailTransport = this.mailSession.getTransport(SESSION_TRANSPORT_TYPE_DEFAULT);
			logger.info("MailSession(String, String, String) :: Initialized");
		} catch (NoSuchProviderException e) {
			logger.error("MailSession(String, String, String) :: MailSession Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	/**
	 * Method Name : Constructor 
	 * this is the complete constructor, 
	 * the user needs to populate the mail configuration properties and pass it into this method.
	 * 
	 * @author ahandoyo
	 * @param identifier
	 * @param mailConfigurationProperties
	 * @throws GenericMailException
	 */
	public MailSession(String _identifier, Properties _mailConfigurationProperties) throws BaseMailException {
		try {
			logger.info("MailSession(String, Properties) :: Initializing");
			this.identifier = _identifier;
			logger.debug("MailSession(String, Properties) :: identifier = " + this.identifier);

			this.mailConfigurationProperties = _mailConfigurationProperties;
			this.smtpHost = this.mailConfigurationProperties.getProperty("MAIL_SMTP_HOST_KEY");
			logger.debug("MailSession(String, Properties) :: smtp host = " + this.smtpHost);

			this.smtpPort = this.mailConfigurationProperties.getProperty("MAIL_SMTP_PORT_KEY");
			logger.debug("MailSession(String, Properties) :: smtp port = " + this.smtpPort);

			logger.info("MailSession(String, Properties) :: Creating SMTP Session");
			this.mailSession = Session.getInstance(this.mailConfigurationProperties);

			logger.info("MailSession(String, Properties) :: Creating SMTP Mail Transport object");
			this.mailTransport = this.mailSession.getTransport(SESSION_TRANSPORT_TYPE_DEFAULT);
			logger.info("MailSession(String, Properties) :: Initialized");
		} catch (NoSuchProviderException e) {
			logger.error("MailSession(String, Properties) :: MailSession Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	/**
	 * Method Name : Constructor 
	 * this is the complete constructor with authentication. 
	 * create an instance of Authenticator class before hand to pass to the constructor 
	 * <code>
	 * 		Authenticator authenticator = new SMTPAuthenticator();
	 * 		MailSession mailSession = new MailSession("COTS", mailConfigurationProperties, authenticator);
	 * </code>
	 * 
	 * @author ahandoyo
	 * @param identifier
	 * @param mailConfigurationProperties
	 * @param authenticator
	 * @throws GenericMailException
	 */
	public MailSession(String _identifier, Properties _mailConfigurationProperties, Authenticator authenticator) throws BaseMailException {
		try {
			logger.info("MailSession(String, Properties, Authenticator) :: Initializing");
			this.identifier = _identifier;
			logger.debug("MailSession(String, Properties, Authenticator) :: identifier = " + this.identifier);

			this.mailConfigurationProperties = _mailConfigurationProperties;
			this.smtpHost = this.mailConfigurationProperties.getProperty("MAIL_SMTP_HOST_KEY");
			logger.debug("MailSession(String, Properties, Authenticator) :: smtp host = " + this.smtpHost);

			this.smtpPort = this.mailConfigurationProperties.getProperty("MAIL_SMTP_PORT_KEY");
			logger.debug("MailSession(String, Properties, Authenticator) :: smtp port = " + this.smtpPort);

			logger.info("MailSession(String, Propertie, Authenticators) :: Creating SMTP Session");
			this.mailSession = Session.getInstance(this.mailConfigurationProperties, authenticator);

			logger.info("MailSession(String, Properties, Authenticator) :: Creating SMTP Mail Transport object");
			this.mailTransport = this.mailSession.getTransport(SESSION_TRANSPORT_TYPE_DEFAULT);
			logger.info("MailSession(String, Properties, Authenticator) :: Initialized");
		} catch (Exception e) {
			logger.error("MailSession(String, Properties, Authenticator) :: MailSession Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	/**
	 * Method Name : Constructor 
	 * this is the complete constructor with authentication. 
	 * create an instance of Authenticator class before hand to pass to the constructor 
	 * <code>
	 * 		Authenticator authenticator = new SMTPAuthenticator();
	 * 		MailSession mailSession = new MailSession("COTS", mailConfigurationProperties, authenticator);
	 * </code>
	 * 
	 * @author ahandoyo
	 * @param identifier
	 * @param mailConfigurationProperties
	 * @param authenticator
	 * @throws GenericMailException
	 */
	public MailSession(String _identifier, String _mailConfigurationPropertiesFileName, Authenticator authenticator) throws BaseMailException {
		try {
			logger.debug("Loading mail configuration properties");
			logger.debug("configuration properties = " + _mailConfigurationPropertiesFileName);
			Properties _mailConfigurationProperties = new Properties();
			_mailConfigurationProperties.load(this.getClass().getClassLoader().getResourceAsStream(_mailConfigurationPropertiesFileName));

			logger.info("MailSession(String, Properties, Authenticator) :: Initializing");
			this.identifier = _identifier;
			logger.debug("MailSession(String, Properties, Authenticator) :: identifier = " + this.identifier);

			this.mailConfigurationProperties = _mailConfigurationProperties;
			this.smtpHost = this.mailConfigurationProperties.getProperty("MAIL_SMTP_HOST_KEY");
			logger.debug("MailSession(String, Properties, Authenticator) :: smtp host = " + this.smtpHost);

			this.smtpPort = this.mailConfigurationProperties.getProperty("MAIL_SMTP_PORT_KEY");
			logger.debug("MailSession(String, Properties, Authenticator) :: smtp port = " + this.smtpPort);

			logger.info("MailSession(String, Propertie, Authenticators) :: Creating SMTP Session");
			this.mailSession = Session.getInstance(this.mailConfigurationProperties, authenticator);

			logger.info("MailSession(String, Properties, Authenticator) :: Creating SMTP Mail Transport object");
			this.mailTransport = this.mailSession.getTransport(SESSION_TRANSPORT_TYPE_DEFAULT);
			logger.info("MailSession(String, Properties, Authenticator) :: Initialized");
		} catch (Exception e) {
			logger.error("MailSession(String, Properties, Authenticator) :: MailSession Initialization Failed - identifier = " + this.identifier, e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.session.ICOTSMailSession1#getIdentifier()
	 */
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String _identifier) {
		this.identifier = _identifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.session.ICOTSMailSession1#getMailConfigurationProperties ()
	 */
	public Properties getMailConfigurationProperties() {
		return mailConfigurationProperties;
	}

	public void setMailConfigurationProperties(Properties _mailConfigurationProperties) {
		this.mailConfigurationProperties = _mailConfigurationProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.session.ICOTSMailSession1#getMailSession()
	 */
	public Session getMailSession() {
		return mailSession;
	}

	public void setMailSession(Session _mailSession) {
		this.mailSession = _mailSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.session.ICOTSMailSession1#getMailTransport()
	 */
	public Transport getMailTransport() {
		return mailTransport;
	}

	public void setMailTransport(Transport _mailTransport) {
		this.mailTransport = _mailTransport;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String _smtpHost) {
		this.smtpHost = _smtpHost;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String _smtpPassword) {
		this.smtpPassword = _smtpPassword;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String _smtpPort) {
		this.smtpPort = _smtpPort;
	}

	public String getSmtpUserLogin() {
		return smtpUserLogin;
	}

	public void setSmtpUserLogin(String _smtpUserLogin) {
		this.smtpUserLogin = _smtpUserLogin;
	}

	public String getTransportStaticType() {
		return transportStaticType;
	}

	public void setTransportStaticType(String _transportStaticType) {
		this.transportStaticType = _transportStaticType;
	}

	public int getSendCounterThreshold() {
		return sendCounterThreshold;
	}

	public void setSendCounterThreshold(int _sendCounterThreshold) {
		this.sendCounterThreshold = _sendCounterThreshold;
	}

	/**
	 * Method Name : connect(void) 
	 * this method will provide basic connect mechanism, 
	 * the initialization of this class is recommended to call this method 
	 * if the implementor provides smtpUser and Password while creating this class, 
	 * that detail will be used to connect, 
	 * if not, then a normal non parameterized connect method of the transport object will be used 
	 * this connect method should only be needed to called only and if only, a Non-Static Transport type is being used
	 * 
	 * @param void
	 * @return void
	 * @throws GenericMailException
	 * @author adelwin.handoyo
	 */
	public void connect() throws BaseMailException {
		logger.info("MailSession.connect(void) :: Connecting");
		if (this.transportStaticType.equalsIgnoreCase(TRANSPORT_STATIC_FALSE)) {
			logger.debug("MailSession.connect(void) :: Transport static type = " + this.transportStaticType);
			try {
				if (this.smtpUserLogin != null && this.smtpUserLogin.trim().length() > 0 && this.smtpPassword != null && this.smtpPassword.trim().length() > 0) {
					logger.debug("MailSession.connect(void) :: Parameterised connection");
					this.connect(this.smtpUserLogin, this.smtpPassword);
				} else {
					logger.debug("MailSession.connect(void) :: Plain, Non Parameterised connection");
					this.mailTransport.connect();
				}
			} catch (MessagingException e) {
				logger.error("MailSession.connect(void) :: MessagingException occured :: " + e.getMessage(), e);
				throw new BaseMailException(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.session.IMailSession#connect(java.lang.String, java.lang.String)
	 */
	public void connect(String _smtpUser, String _smtpPassword) throws BaseMailException {
		logger.info("MailSession.connect(String, String) :: Connecting");
		this.smtpUserLogin = _smtpUser;
		this.smtpPassword = _smtpPassword;
		if (this.transportStaticType.equalsIgnoreCase(TRANSPORT_STATIC_FALSE)) {
			try {
				logger.debug("MailSession.connect(String, String) :: Mail Transport Connection");
				this.mailTransport.connect(this.smtpHost, this.smtpUserLogin, this.smtpPassword);
			} catch (MessagingException e) {
				logger.error("MailSession.connect(String, String) :: MessagingException occured :: " + e.getMessage(), e);
				throw new BaseMailException(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.session.IMailSession#dissconnect(java.lang.String, java.lang.String)
	 */
	public void disconnect() throws BaseMailException {
		logger.info("MailSession.disconnect(void) :: Starting");
		try {
			logger.info("MailSession.disconnect(void) :: Disposing Mail Transport Object");
			this.mailTransport.close();
		} catch (MessagingException e) {
			logger.error("MailSession.disconnect(void) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.si.diamond.mail.session.IMailSession#send(com.si.diamond.mail.message .COTS1MailMessage)
	 */
	public int send(MimeMessage mailMessage) throws BaseMailException {
		logger.info("MailSession.send(MimeMessage) :: Starting");

		// counts how many message have been sent by this session.
		// if the count already reaches the threshold, 
		// then reconnect this mail session
		logger.info("MailSession.send(MimeMessage) :: Evaluating send counter against treshold");
		if (this.sendCounter >= this.sendCounterThreshold) {
			logger.info("MailSession.send(MimeMessage) :: Send counter reached treshold, reconnecting");
			this.reconnect();
			this.sendCounter = 0;
		}
		this.sendCounter++;

		if (this.transportStaticType == null || this.transportStaticType.trim().length() == 0) {
			logger.info("MailSession.send(MimeMessage) :: Transport Static Type undefined, set to DEFAULT");
			this.transportStaticType = TRANSPORT_STATIC_DEFAULT;
		}

		try {
			if (this.transportStaticType.equalsIgnoreCase(TRANSPORT_STATIC_TRUE)) {
				logger.info("MailSession.send(MimeMessage) :: Sending mail with Static Transport");
				return this.transportStaticSend(mailMessage);
			} else {
				logger.info("MailSession.send(MimeMessage) :: Sending mail with Non Static Transport");
				return this.transportNonStaticSend(mailMessage);
			}
		} catch (Exception e) {
			logger.error("MailSession.send(MimeMessage) :: Exception occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	public int transportStaticSend(MimeMessage mailMessage) throws BaseMailException {
		logger.info("MailSession.transportStaticSend(MimeMessage) :: Sending mail with Static Transport");
		try {
			logger.info("MailSession.transportStaticSend(MimeMessage) :: Sending..");
			Transport.send(mailMessage);
			return 1;
		} catch (MessagingException e) {
			logger.error("MailSession.transportStaticSend(MimeMessage) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	public int transportNonStaticSend(MimeMessage mailMessage) throws BaseMailException {
		logger.info("MailSession.transportNonStaticSend(MimeMessage) :: Sending mail with Non Static Transport");
		if (this.mailTransport == null) {
			logger.info("MailSession.transportNonStaticSend(MimeMessage) :: Transport object is null");
			try {
				logger.info("MailSession.transportNonStaticSend(MimeMessage) :: Retrieving new Mail Transport Object");
				this.mailTransport = mailSession.getTransport(SESSION_TRANSPORT_TYPE_DEFAULT);
			} catch (NoSuchProviderException e) {
				logger.error("MailSession.transportNonStaticSend(MimeMessage) :: NoSuchProviderException occured :: " + e.getMessage(), e);
				throw new BaseMailException(e.getMessage(), e);
			}
		}
		try {
			if (!this.mailTransport.isConnected()) {
				logger.info("MailSession.transportNonStaticSend(MimeMessage) :: Mail Transport object is not connected, connecting");
				this.connect();
			}
			logger.info("MailSession.transportNonStaticSend(MimeMessage) :: Sending");
			this.mailTransport.sendMessage(mailMessage, mailMessage.getAllRecipients());
			return 1;
		} catch (MessagingException e) {
			logger.error("MailSession.transportStaticSend(MimeMessage) :: MessagingException occured :: " + e.getMessage(), e);
			throw new BaseMailException(e.getMessage(), e);
		}
	}

	public void reconnect() throws BaseMailException {
		logger.info("MailSession.reconnect(void) :: Disconnecting");
		this.disconnect();
		logger.info("MailSession.reconnect(void) :: Connecting");
		this.connect();
		logger.info("MailSession.reconnect(void) :: Connected");
	}
}
