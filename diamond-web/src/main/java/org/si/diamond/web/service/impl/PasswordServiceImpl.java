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
import org.si.diamond.base.exception.CipherException;
import org.si.diamond.base.service.impl.BaseServiceImpl;
import org.si.diamond.base.util.CipherUtil;
import org.si.diamond.web.model.PasswordModel;
import org.si.diamond.web.model.UserRoleModel;
import org.si.diamond.web.service.IPasswordService;
import org.si.diamond.web.service.IUserRoleService;

import javax.crypto.Cipher;
import java.util.List;

public class PasswordServiceImpl extends BaseServiceImpl implements IPasswordService {
	
	protected Logger logger = Logger.getLogger(PasswordServiceImpl.class);
	private IBaseDao<String, PasswordModel> passwordDao;

	public IBaseDao<String, PasswordModel> getPasswordDao() {
		return passwordDao;
	}

	public void setPasswordDao(IBaseDao<String, PasswordModel> passwordDao) {
		this.passwordDao = passwordDao;
	}

	@Override
	public PasswordModel getPasswordById(String passwordId) throws BaseServiceException {
		try {
			return this.getPasswordDao().getById(passwordId);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<PasswordModel> getPasswordListByCriteria(PasswordModel criterion) throws BaseServiceException {
		try {
			return this.getPasswordDao().getListByExample(criterion);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<PasswordModel> getPasswordListByUserId(String userId) throws BaseServiceException {
		try {
			PasswordModel criterion = new PasswordModel();
			criterion.setUserId(userId);
			criterion.setStatus("ACTIVE_RS");
			criterion.setPasswordStatus("ACTIVE_PS");
			return this.getPasswordDao().getListByExample(criterion);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	public String decrypt(String password, String salt) throws CipherException {
		try {
			logger.debug("passwprd = " + password);
			logger.debug("salt = " + salt);
			String step1 = CipherUtil.decrypt(password);
			logger.debug("step1 = " + step1);
			if (!step1.endsWith(salt)) {
				throw new CipherException(("unmatched salt"));
			}
			String step2 = step1.substring(0, step1.length() - salt.length());
			logger.debug(password.length());
			logger.debug(salt.length());
			logger.debug("step2 = " + step2);
			return step2;
		} catch (CipherException e) {
			throw new CipherException(e.getMessage(), e);
		}
	}
}
