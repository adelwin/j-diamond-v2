/*
 * File Name	: $filename
 * Author		: @$user.name
 * Created Date	: $date $time
 *
 * Copyright (c) 2014 Solveware Independent. All Rights Reserved. <BR/>
 * <BR/>
 * This software contains confidential and proprietary information of Solveware Independent.<BR/>
 */

package org.si.diamond.web.service;

import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.IBaseService;
import org.si.diamond.web.model.LookupModel;
import org.si.diamond.web.model.MailModel;

import java.util.List;

public interface IMailService extends IBaseService {

	public MailModel getById(String mailId) throws BaseServiceException;
	public MailModel getByAddress(String mailAddress) throws BaseServiceException;
	public List<MailModel> getByUser(String userId) throws BaseServiceException;

}
