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

import java.util.List;

public interface IAuthenticationService extends IBaseService {
	public LoginContext authenticate(String userCode, String password) throws BaseServiceException;
	public void updatePassword(LoginContext loginContext, String userCode, String newPassword) throws BaseServiceException;
}
