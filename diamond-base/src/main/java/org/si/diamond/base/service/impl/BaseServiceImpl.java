/**
 * File Name    : BaseServiceImpl.java
 * Author       : adelwin
 * Created Date : Jan 23, 2011 11:58:58 AM
 */
package org.si.diamond.base.service.impl;

import org.apache.log4j.Logger;
import org.si.diamond.base.context.LoginContext;
import org.si.diamond.base.model.CommonModel;
import org.si.diamond.base.service.IBaseService;

import java.util.Date;

public class BaseServiceImpl implements IBaseService {
	
	protected Logger logger = Logger.getLogger(BaseServiceImpl.class);
	
	public void setCommonAttributesForCreate(LoginContext loginCtx, CommonModel commonModel) {
		logger.debug("setting common attributes for create");
		commonModel.setCreateBy((String) loginCtx.getAttribute(LoginContext.KEY_USER_CODE));
		commonModel.setCreateDate(new Date());
	}

	public void setCommonAttributesForUpdate(LoginContext loginContext, CommonModel commonModel) {
		logger.debug("setting common attributes for update");
		commonModel.setUpdateBy((String) loginContext.getAttribute(LoginContext.KEY_USER_CODE));
		commonModel.setUpdateDate(new Date());
	}
}
