package org.si.diamond.mail.authenticator;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator {

	private String	userName;

	private String	password;

	public SMTPAuthenticator(String _userName, String _password) {
		this.userName = _userName;
		this.password = _password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.userName, this.password);
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getPassword() {
		return this.password;
	}
}
