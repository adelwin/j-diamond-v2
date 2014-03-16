/**
 * File Name : MailListenerThread.java Author : adelwin.handoyo Create Date :
 * 23-Jun-2009
 * 
 * Copyright (c) 2009 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of Adelwin.
 * ("Confidential Information").<BR>
 * <BR>
 */
package org.si.diamond.mail.thread;

import java.io.InputStream;

import org.si.diamond.mail.exception.BaseMailException;


/**
 * This Interface will govern the way a thread in this module works.<br>
 * this interface will force any implementing object to be threads, <br>
 * 
 * Type Name : MailListenerThread Package : COTSMailProject Author :
 * adelwin.handoyo Created : AM 10:55:29
 */
public interface MailListenerThread {

	/**
	 * Method Name : load(String) this method will load the specific
	 * configuration file for this thread <BR>
	 * one mail session instance for one thread.
	 * 
	 * @param messageFileName
	 * @return void
	 * @author adelwin.handoyo
	 */
	public void load(String configurationFileName) throws BaseMailException;

	/**
	 * Method Name : load(File) this method is a more specific method that takes
	 * a parameter of a file object<BR>
	 * 
	 * @param messageFile
	 * @return void
	 * @author adelwin.handoyo
	 */
	public void load(InputStream configurationFile) throws BaseMailException;

	/**
	 * Method Name : init(void) this method is to initialise this thread, set
	 * the listening directory, authenticate mail session or anything else
	 * 
	 * @param void
	 * @return void
	 * @author adelwin.handoyo
	 */
	public void init() throws BaseMailException;

	/**
	 * Method Name : destroy(void) this method is to finalise the thread after
	 * use, kill the file object that listens to a directory, disconnect mail
	 * session or anything else
	 * 
	 * @param void
	 * @return void
	 * @author adelwin.handoyo
	 */
	public void destroy() throws BaseMailException;

	/**
	 * Method Name : start(void) this method is to start the thread after init,
	 * 
	 * @param void
	 * @return void
	 * @author adelwin.handoyo
	 */
	public void start() throws BaseMailException;
}
