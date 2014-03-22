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
import org.si.diamond.base.exception.BaseDaoException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.exception.CipherException;
import org.si.diamond.base.service.impl.BaseServiceImpl;
import org.si.diamond.base.util.GUIDUtil;
import org.si.diamond.web.model.LookupModel;
import org.si.diamond.web.model.PasswordModel;
import org.si.diamond.web.model.UserModel;
import org.si.diamond.web.service.*;

import java.util.Date;


public class AuthenticationServiceImpl extends BaseServiceImpl implements IAuthenticationService {

	protected Logger logger = Logger.getLogger(AuthenticationServiceImpl.class);
	private IUserService userService;
	private IPasswordService passwordService;
	private IUserRoleService userRoleService;
	private ILookupService lookupService;

	public IPasswordService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(IPasswordService passwordService) {
		this.passwordService = passwordService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IUserRoleService getUserRoleService() {
		return userRoleService;
	}

	public void setUserRoleService(IUserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	public ILookupService getLookupService() {
		return lookupService;
	}

	public void setLookupService(ILookupService lookupService) {
		this.lookupService = lookupService;
	}

	@Override
	public LoginContext authenticate(String userCode, String password) throws BaseServiceException {
		try {
			logger.info("retrieving user model");
			UserModel userModel = this.getUserService().getUserByCode(userCode);
			logger.info("user model retrieved [" + userModel + "]");

			logger.info("retrieving correct password in db");
			PasswordModel correctPassword = this.getPasswordService().getPasswordListByUserId(userModel.getUserId()).get(0);
			logger.info("correct password retrieved [" + correctPassword + "]");

			logger.info("decrypting password");
			String clearCorrectPassword = this.getPasswordService().decrypt(correctPassword.getPasswordValue(), correctPassword.getPasswordSalt());
			logger.info("clear password is [" + clearCorrectPassword + "]");

			logger.info("matching password");
			if (password.equals(clearCorrectPassword)) {
				logger.info("password matches, building login context");
				LoginContext loginContext = new LoginContext();
				loginContext.setAttribute(LoginContext.KEY_USER_ID, userModel.getUserId());
				loginContext.setAttribute(LoginContext.KEY_USER_NAME, userModel.getUserName());
				loginContext.setAttribute(LoginContext.KEY_USER_ROLE, this.getUserRoleService().getRoleById(userModel.getUserRoleId()));
				return loginContext;
			} else {
				logger.info("password missmatch, returning null");
				return null;
			}
		} catch (BaseServiceException e) {
			logger.error("Error during Authentication");
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} catch (CipherException e) {
			logger.error("Error during Authentication");
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}

	@Override
	public void updatePassword(LoginContext loginContext, String userCode, String newPassword) throws BaseServiceException {
		try {
			logger.info("preparing lookup models");
			LookupModel passwordActiveLookupModel = lookupService.findLookupByCode(null, "ACTIVE_PS");
			LookupModel recordActiveLookupModel = lookupService.findLookupByCode(null, "ACTIVE_RS");
			logger.info("lookup models prepared");

			logger.info("retrieving user model");
			UserModel userModel = this.getUserService().getUserByCode(userCode);
			logger.info("user model retrieved [" + userModel + "]");

			logger.info("retrieving currently active password for user");
			PasswordModel oldPasswordModel = this.getPasswordService().getPasswordListByUserId(userModel.getUserId()).get(0);
			logger.info("currently active password for user, retrieved");

			logger.info("deactivating active password");
			this.getPasswordService().deactivatePassword(loginContext, oldPasswordModel);

			logger.info("building new password model");
			PasswordModel newPasswordModel = new PasswordModel();
			newPasswordModel.setPasswordId(GUIDUtil.getGUID());
			newPasswordModel.setPasswordSalt(GUIDUtil.getGUID());
			newPasswordModel.setPasswordValue(this.getPasswordService().encrypt(newPassword, newPasswordModel.getPasswordSalt()));
			newPasswordModel.setLoginAttempt(0);
			newPasswordModel.setUserId(userModel.getUserId());
			newPasswordModel.setPasswordStatus(passwordActiveLookupModel.getLookupCode());
			newPasswordModel.setCreateBy(userModel.getUserCode());
			newPasswordModel.setCreateDate(new Date());
			newPasswordModel.setStatus(recordActiveLookupModel.getLookupCode());
			logger.info("new password model built [" + newPasswordModel + "]");

			logger.info("adding new password");
			this.getPasswordService().addPassword(loginContext, newPasswordModel);
			logger.info("new password added and old password deactivated");

		} catch (BaseServiceException e) {
			logger.error("error during updating password");
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} catch (CipherException e) {
			logger.error("error during updating password");
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}
}
