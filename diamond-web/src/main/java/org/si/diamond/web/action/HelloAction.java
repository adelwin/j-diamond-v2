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
import org.si.diamond.base.exception.BaseActionException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.web.model.LookupModel;
import org.si.diamond.web.model.UserModel;
import org.si.diamond.web.model.UserRoleModel;
import org.si.diamond.web.service.ILookupService;
import org.si.diamond.web.service.IUserRoleService;
import org.si.diamond.web.service.IUserService;

/**
 * Type Name : HelloAction Package : sme-web Author : adelwin.handoyo Created :
 * PM 12:59:14
 */
public class HelloAction extends BaseAction {

	private static final long serialVersionUID = 6507175604288282118L;
	protected Logger logger = Logger.getLogger(HelloAction.class);
	private ILookupService lookupService;
	private IUserService userService;
	private IUserRoleService userRoleService;
	
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

	@Override
	public String action() throws BaseActionException {
		try {
			logger.debug("on action method of hello action");
			List<LookupModel> lookups = lookupService.getLookupByType("");
			this.lookup = lookups.get(0);
			logger.debug(lookups);
			
			logger.debug("name is " + this.name);
			
			List<UserModel> userList = this.userService.getUserByName("Adelwin");
			logger.debug("user id is " + userList.get(0).getUserId());
			logger.debug("user role id = " + userList.get(0).getUserRoleId());
			
			UserRoleModel role = this.userRoleService.getRoleById(userList.get(0).getUserRoleId());
			logger.debug("user role name = " + role.getUserRoleName());
			
			if (userList.get(0).getUserId().equals("8")) {
				throw new BaseActionException("test");
			}
		} catch (BaseServiceException e) {
			logger.error(e.getMessage(), e);
			throw new BaseActionException(e.getMessage(), e);
		} finally {
		}
		return SUCCESS;
	}
}
