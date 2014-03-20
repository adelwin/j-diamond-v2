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
import org.si.diamond.base.exception.CipherException;
import org.si.diamond.base.service.IBaseService;
import org.si.diamond.web.model.PasswordModel;

import java.util.List;

public interface IPasswordService extends IBaseService {

	public PasswordModel getPasswordById(String passwordId) throws BaseServiceException;
	public List<PasswordModel> getPasswordListByCriteria(PasswordModel criterion) throws BaseServiceException;
	public List<PasswordModel> getPasswordListByUserId(String userId) throws BaseServiceException;
	public String decrypt(String password, String salt) throws CipherException;
}
