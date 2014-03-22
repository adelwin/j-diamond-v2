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
import org.si.diamond.base.context.LoginContext;
import org.si.diamond.base.dao.IBaseDao;
import org.si.diamond.base.exception.BaseDaoException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.impl.BaseServiceImpl;
import org.si.diamond.web.model.AccountModel;
import org.si.diamond.web.service.IAccountService;

import java.util.Date;
import java.util.List;

public class AccountServiceImpl extends BaseServiceImpl implements IAccountService {

	protected Logger logger = Logger.getLogger(AccountServiceImpl.class);
	private IBaseDao<String, AccountModel> accountDao;

	public IBaseDao<String, AccountModel> getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(IBaseDao<String, AccountModel> accountDao) {
		this.accountDao = accountDao;
	}

	@Override
	public AccountModel getAccountById(String accountId) throws BaseServiceException {
		try {
			return this.getAccountDao().getById(accountId);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}

	@Override
	public List<AccountModel> getUserListByCriteria(AccountModel criterion) throws BaseServiceException {
		try {
			return this.getAccountDao().getListByExample(criterion);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}

	@Override
	public void addAccount(LoginContext loginContext, AccountModel accountModel) throws BaseServiceException {
		try {
			accountModel.setCreateBy((String) loginContext.getAttribute(LoginContext.KEY_USER_ID));
			accountModel.setCreateDate(new Date());
			this.getAccountDao().insert(accountModel);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}

	@Override
	public void updateAccount(LoginContext loginContext, AccountModel accountModel) throws BaseServiceException {
		try {
			accountModel.setUpdateBy((String) loginContext.getAttribute(LoginContext.KEY_USER_ID));
			accountModel.setUpdateDate(new Date());
			this.getAccountDao().updatePartial(accountModel, accountModel);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}
}
