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
 * Module Name: Batch Email Gateway File Name: BaseMailMessage.java Description
 * : the purpose of this class is to create a generic mechanism of sending
 * emails
 * 
 * Program Amendment History Date Ver Author Description ----------- --- -------
 * -------------------------------------------- 08-JUN-1009 1.0 Adelwin Initial
 * draft
 */
package org.si.diamond.mail.message;

import java.io.File;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.si.diamond.mail.exception.BaseMailException;



/**
 * This class will be the base class of all COTS Mail Message. This class will
 * have the most common
 * 
 * @author ahandoyo
 */
public abstract class BaseMailMessage extends MimeMessage {

	public BaseMailMessage(Session _session) {
		super(_session);
	}

	public abstract void load(String messageFileName) throws BaseMailException;

	public abstract void load(File messageFile) throws BaseMailException;
}
