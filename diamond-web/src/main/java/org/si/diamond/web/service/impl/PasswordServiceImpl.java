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
import org.si.diamond.base.exception.CipherException;
import org.si.diamond.base.service.impl.BaseServiceImpl;
import org.si.diamond.base.util.CipherUtil;
import org.si.diamond.base.util.GUIDUtil;
import org.si.diamond.web.model.PasswordModel;
import org.si.diamond.web.model.UserRoleModel;
import org.si.diamond.web.service.IPasswordService;
import org.si.diamond.web.service.IUserRoleService;

import javax.crypto.Cipher;
import java.util.Date;
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

	@Override
	public void addPassword(LoginContext loginContext, PasswordModel passwordModel) throws BaseServiceException {
		try {
			logger.debug("validating new password model");
			if (passwordModel.getPasswordValue() == null || passwordModel.getPasswordValue().trim().length() < 1) {
				throw new BaseServiceException("New Password Model incomplete, password value is empty");
			}
			if (passwordModel.getPasswordSalt() == null || passwordModel.getPasswordSalt().trim().length() < 1) {
				throw new BaseServiceException("New Password Model incomplete, password salt is empty");
			}
			if (passwordModel.getLoginAttempt() == null) {
				passwordModel.setLoginAttempt(0);
			}
			if (passwordModel.getUserId() == null || passwordModel.getUserId().trim().length() < 1) {
				throw new BaseServiceException("New Password Model does not have user id owner");
			}
			logger.debug("New Password Model validated");

			logger.debug("setting other parameters");
			passwordModel.setPasswordStatus("ACTIVE_PS");
			passwordModel.setPasswordId(GUIDUtil.getGUID());
			passwordModel.setCreateBy((String) loginContext.getAttribute(LoginContext.KEY_USER_CODE));
			passwordModel.setCreateDate(new Date());
			passwordModel.setUpdateBy(null);
			passwordModel.setUpdateDate(null);

			this.getPasswordDao().updatePartial(passwordModel, passwordModel);
			logger.debug("new password added");
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	@Override
	public void deactivatePassword(LoginContext loginContext, PasswordModel passwordModel) throws BaseServiceException {
		try {
			passwordModel.setPasswordStatus("INACTIVE_PS");
			passwordModel.setUpdateBy((String) loginContext.getAttribute(LoginContext.KEY_USER_CODE));
			passwordModel.setUpdateDate(new Date());

			this.getPasswordDao().updatePartial(passwordModel, passwordModel);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	public String decrypt(String password, String salt) throws CipherException {
		try {
			logger.debug("password = " + password);
			logger.debug("salt = " + salt);
			String step1 = CipherUtil.decrypt(password);
			logger.debug("step1 = " + step1);
			if (!step1.endsWith(salt)) {
				throw new CipherException(("unmatched salt"));
			}
			String step2 = step1.substring(0, step1.length() - salt.length());
			logger.debug("step2 = " + step2);
			return step2;
		} catch (CipherException e) {
			throw new CipherException(e.getMessage(), e);
		}
	}

	public String encrypt(String password, String salt) throws CipherException {
		try {
			logger.debug("password = " + password);
			logger.debug("salt = " + salt);

			String step1 = password.concat(salt);
			logger.debug("step1 = " + step1);

			String step2 = CipherUtil.encrypt(step1);
			logger.debug("step2 = " + step2);
			return step2;
		} finally {}
	}
}
