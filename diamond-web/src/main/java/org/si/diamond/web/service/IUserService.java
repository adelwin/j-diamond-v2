package org.si.diamond.web.service;

import java.util.List;

import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.IBaseService;
import org.si.diamond.web.model.UserModel;

public interface IUserService extends IBaseService {

	public UserModel getUserById(String userId) throws BaseServiceException;
	public List<UserModel> getUserListByCriteria(UserModel criterion) throws BaseServiceException;
	public UserModel getUserByCode(String userCode) throws BaseServiceException;
	public List<UserModel> getUserByName(String name) throws BaseServiceException;

}
