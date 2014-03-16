package org.si.diamond.web.model;

import org.si.diamond.base.model.CommonModel;

public class UserRoleModel extends CommonModel {

	private static final long serialVersionUID = -698876310896709712L;
	
	private String userRoleId;
	private String userRoleName;
	private String loginLevel;
	public String getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getUserRoleName() {
		return userRoleName;
	}
	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}
	public String getLoginLevel() {
		return loginLevel;
	}
	public void setLoginLevel(String loginLevel) {
		this.loginLevel = loginLevel;
	}


}