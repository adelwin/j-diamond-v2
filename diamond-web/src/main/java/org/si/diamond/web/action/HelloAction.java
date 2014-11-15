/**
 * File Name	: HelloAction.java
 * Author		: adelwin.handoyo
 * Create Date	: 11-Dec-2010
 *
 * Copyright (c) 2010 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of
 * Adelwin. ("Confidential Information").<BR>
 * <BR>
 */
package org.si.diamond.web.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.si.diamond.base.action.BaseAction;
import org.si.diamond.base.context.LoginContext;
import org.si.diamond.base.exception.BaseActionException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.exception.CipherException;
import org.si.diamond.base.util.CipherUtil;
import org.si.diamond.web.model.*;
import org.si.diamond.web.service.*;

public class HelloAction extends BaseAction {

	private static final long serialVersionUID = 6507175604288282118L;
	protected Logger logger = Logger.getLogger(HelloAction.class);
	private ILookupService lookupService;
	private IUserService userService;
	private IUserRoleService userRoleService;
	private IPasswordService passwordService;
	private IMailService mailService;
	private IAccountService accountService;
	private IAuthenticationService authenticationService;

	private String name;
	private LookupModel lookup;
	
	public LookupModel getLookup() {
		return lookup;
	}

	public void setLookup(LookupModel lookup) {
		this.lookup = lookup;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ILookupService getLookupService() {
		return lookupService;
	}

	public void setLookupService(ILookupService lookupService) {
		this.lookupService = lookupService;
	}

	public IUserRoleService getUserRoleService() {
		return userRoleService;
	}

	public void setUserRoleService(IUserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	public IPasswordService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(IPasswordService passwordService) {
		this.passwordService = passwordService;
	}

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public IAccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(IAccountService accountService) {
		this.accountService = accountService;
	}

	public IAuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(IAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	public String action() throws BaseActionException {
		try {
			logger.debug("on action method of hello action");

			logger.debug("Testing named parameter");
			logger.debug("name is " + this.name);

			logger.debug("Testing Lookups");
			List<LookupModel> lookups = lookupService.getLookupByType("");

			this.lookup = lookups.get(0);
			logger.debug(lookups);

			logger.debug(this.getLookupService().findLookupByCode(null, "LITERAL_A"));

			logger.debug("Testing Users");
			List<UserModel> userList = this.userService.getUserByName("Adelwin");
			logger.debug("user id is " + userList.get(0).getUserId());
			logger.debug("user role id = " + userList.get(0).getUserRoleId());

			logger.debug("Testing User Roles");
			UserRoleModel role = this.userRoleService.getRoleById(userList.get(0).getUserRoleId());
			logger.debug("user role name = " + role.getUserRoleName());

			logger.debug("Testing Password");
			List<PasswordModel> passwords = this.getPasswordService().getPasswordListByUserId(userList.get(0).getUserId());
			logger.debug("encrypted password is " + passwords.get(0).getPasswordValue());
			logger.debug("decrypted password is " + this.getPasswordService().decrypt(passwords.get(0).getPasswordValue(), passwords.get(0).getPasswordSalt()));

			logger.debug("Testing Mails");
			MailModel mail1 = this.getMailService().getByAddress("adelwin@gmail.com");
			logger.debug("mail owner is " + mail1.getUserId());
			List<MailModel> mails = this.getMailService().getByUser(userList.get(0).getUserId());
			logger.debug("mails are " + mails.toArray());

			LoginContext loginContext = this.getAuthenticationService().authenticate("adelwin", "p01ntbl4nk");
			logger.debug("authenticated!!");

			logger.debug("testing accounts");
			AccountModel account = this.getAccountService().getAccountById("1");
			account.setAccountName("Testing Update");
			this.getAccountService().updateAccount(loginContext, account);

			logger.debug("Testing exception handling");
			if (userList.get(0).getUserId().equals("8")) {
				throw new BaseActionException("test");
			} else {
				return SUCCESS;
			}
		} catch (BaseServiceException e) {
			logger.error(e.getMessage(), e);
			throw new BaseActionException(e.getMessage(), e);
		} catch (CipherException e) {
			logger.error(e.getMessage(), e);
			throw new BaseActionException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BaseActionException(e.getMessage(), e);
		} finally {

		}
	}
}
