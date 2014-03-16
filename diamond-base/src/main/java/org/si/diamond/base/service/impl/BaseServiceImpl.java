/**
 * File Name    : BaseServiceImpl.java
 * Author       : adelwin
 * Created Date : Jan 23, 2011 11:58:58 AM
 */
package org.si.diamond.base.service.impl;

import org.apache.log4j.Logger;
import org.si.diamond.base.context.LoginContext;
import org.si.diamond.base.model.CommonModel;

public class BaseServiceImpl {
	
	protected Logger logger = Logger.getLogger(BaseServiceImpl.class);
	
	protected void setCommonAttributes(LoginContext loginCtx, CommonModel model) {
		
	}
}
