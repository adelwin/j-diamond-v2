/**
 * File Name    : IBaseService.java
 * Author       : adelwin
 * Created Date : Jan 23, 2011 11:58:26 AM
 */
package org.si.diamond.base.service;

import org.si.diamond.base.context.LoginContext;
import org.si.diamond.base.model.CommonModel;

public interface IBaseService {
	void setCommonAttributesForCreate(LoginContext loginContext, CommonModel commonModel);
	void setCommonAttributesForUpdate(LoginContext loginContext, CommonModel commonModel);
}
