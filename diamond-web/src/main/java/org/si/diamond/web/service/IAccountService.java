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

import org.si.diamond.base.context.LoginContext;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.IBaseService;
import org.si.diamond.web.model.AccountModel;
import org.si.diamond.web.model.UserModel;

import java.util.List;

public interface IAccountService extends IBaseService {

	public AccountModel getAccountById(String accountId) throws BaseServiceException;
	public List<AccountModel> getUserListByCriteria(AccountModel criterion) throws BaseServiceException;
	public void addAccount(LoginContext loginContext, AccountModel accountModel) throws BaseServiceException;
	public void updateAccount(LoginContext loginContext, AccountModel accountModel) throws BaseServiceException;
}
