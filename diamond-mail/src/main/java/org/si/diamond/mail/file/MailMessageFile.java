package org.si.diamond.mail.file;

import org.si.diamond.mail.exception.BaseMailException;

public interface MailMessageFile {
	public void load(String messageFileName) throws BaseMailException;
	public boolean hasNextMessage() throws BaseMailException;
	public String nextMessage() throws BaseMailException;
}
