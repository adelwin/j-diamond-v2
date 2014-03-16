package org.si.diamond.mail.session;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.si.diamond.mail.exception.BaseMailException;

public interface IMailSession {

	/**
	 * the common configuration key in the properties file
	 */
	public static final String MAIL_SMTP_HOST_KEY = "mail.smtp.host";

	public static final String MAIL_SMTP_USER_KEY = "mail.smtp.user";

	public static final String MAIL_SMTP_PORT_KEY = "mail.smtp.port";

	public static final String MAIL_SMTP_CONNECTIONTIMEOUT_KEY = "mail.smtp.connectiontimeout";

	public static final String MAIL_SMTP_FROM_KEY = "mail.smtp.from";

	public static final String MAIL_DEBUG_KEY = "mail.debug";

	public static final String MAIL_IMAP_HOST = "mail.imap.host";

	public static final String MAIL_IMAP_PORT = "mail.imap.port";

	public static final String MAIL_IMAP_USER = "mail.imap.user";

	public static final String MAIL_IMAP_SOCKETFACTORY_CLASS = "mail.imap.socketFactory.class";

	public static final String MAIL_IMAP_SOCKETFACTORY_FALLBACK = "mail.imap.socketFactory.fallback";

	public static final String DEFAULT_MAIL_IMAP_PORT = "993";

	public static final String DEFAULT_MAIL_IMAP_SOCKETFACTORY_CLASS = "javax.net.ssl.SSLSocketFactory";

	public static final String DEFAULT_MAIL_IMAP_SOCKETFACTORY_FALLBACK = "false";

	public abstract String getIdentifier();

	public abstract Properties getMailConfigurationProperties();

	public abstract Session getMailSession();

	public abstract Transport getMailTransport();

	/**
	 * Method Name : connect
	 * 
	 * @return void
	 * @author ahandoyo
	 */
	public abstract void connect() throws BaseMailException;

	/**
	 * Method Name : connect(String, String)
	 * 
	 * @return void
	 * @author ahandoyo
	 * @param smtpUser
	 * @param smtpPassword
	 */
	public abstract void connect(String smtpUser, String smtpPassword) throws BaseMailException;

	/**
	 * Method Name : disconnect
	 * 
	 * @return void
	 * @author ahandoyo
	 */
	public abstract void disconnect() throws BaseMailException;

	/**
	 * Method Name : send(COTS1MailMessage)
	 * 
	 * @author ahandoyo
	 * @param mailMessage
	 * @return int
	 */
	public abstract int send(MimeMessage mailMessage) throws BaseMailException;

	/**
	 * Method Name : reconnect(void)
	 * 
	 * @author ahandoyo
	 * @param mailMessage
	 * @return void
	 */
	public abstract void reconnect() throws BaseMailException;
}