/*
 * File Name	: $filename
 * Author		: @$user.name
 * Created Date	: $date $time
 *
 * Copyright (c) 2014 Solveware Independent. All Rights Reserved. <BR/>
 * <BR/>
 * This software contains confidential and proprietary information of Solveware Independent.<BR/>
 */
package org.si.diamond.web.action;

import org.apache.log4j.Logger;
import org.si.diamond.base.action.BaseAction;
import org.si.diamond.base.context.LoginContext;
import org.si.diamond.base.exception.BaseActionException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.web.service.*;

public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 6507175604288282118L;
	protected Logger logger = Logger.getLogger(LoginAction.class);

	private String name;
	private String password;

	private IAuthenticationService authenticationService;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
			logger.debug("name is [" + this.getName() + "]");
			logger.debug("password is [" + this.getPassword() + "]");
			LoginContext loginContext = this.getAuthenticationService().authenticate(this.getName(), this.getPassword());
			if (loginContext != null) {
				return SUCCESS;
			} else {
				return ERROR;
			}
		} catch (BaseServiceException e) {
			logger.error(e.getMessage(), e);
			throw new BaseActionException(e.getMessage(), e);
		} finally {

		}
	}
}
