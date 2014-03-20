/*
 * File Name	: $filename
 * Author		: @$user.name
 * Created Date	: $date $time
 *
 * Copyright (c) 2014 Solveware Independent. All Rights Reserved. <BR/>
 * <BR/>
 * This software contains confidential and proprietary information of Solveware Independent.<BR/>
 */

package org.si.diamond.web.service.impl;

import org.apache.log4j.Logger;
import org.si.diamond.base.dao.IBaseDao;
import org.si.diamond.base.exception.BaseDaoException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.impl.BaseServiceImpl;
import org.si.diamond.web.model.LookupModel;
import org.si.diamond.web.model.MailModel;
import org.si.diamond.web.service.ILookupService;
import org.si.diamond.web.service.IMailService;

import java.util.List;

public class MailServiceImpl extends BaseServiceImpl implements IMailService {
	
	protected Logger logger = Logger.getLogger(MailServiceImpl.class);
	
	private IBaseDao<String, MailModel> mailDao;

	public IBaseDao<String, MailModel> getMailDao() {
		return mailDao;
	}

	public void setMailDao(IBaseDao<String, MailModel> mailDao) {
		this.mailDao = mailDao;
	}

	@Override
	public MailModel getById(String mailId) throws BaseServiceException {
		try {
			return this.getMailDao().getById(mailId);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	@Override
	public MailModel getByAddress(String mailAddress) throws BaseServiceException {
		try {
			MailModel criterion = new MailModel();
			criterion.setMailAddress(mailAddress);
			criterion.setStatus("ACTIVE_RS");
			List<MailModel> resultList = this.getMailDao().getListByExample(criterion);
			if (resultList == null || resultList.size() == 0) {
				return null;
			}
			if (resultList.size() != 1) {
				throw new BaseServiceException("Mail Collection not unique");
			}
			return resultList.get(0);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}

	@Override
	public List<MailModel> getByUser(String userId) throws BaseServiceException {
		try {
			MailModel criterion = new MailModel();
			criterion.setUserId(userId);
			return this.getMailDao().getListByExample(criterion);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}
}
