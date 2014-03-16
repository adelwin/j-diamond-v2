package org.si.diamond.web.service;

import java.util.List;

import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.IBaseService;
import org.si.diamond.web.model.UserRoleModel;

public interface IUserRoleService extends IBaseService {

	public UserRoleModel getRoleById(String userRoleId) throws BaseServiceException;
	public List<UserRoleModel> getUserRoleListByCriteria(UserRoleModel criterion) throws BaseServiceException;
	public List<UserRoleModel> listAll() throws BaseServiceException;
	
}
