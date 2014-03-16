package org.si.diamond.web.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.si.diamond.base.dao.IBaseDao;
import org.si.diamond.base.exception.BaseDaoException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.impl.BaseServiceImpl;
import org.si.diamond.web.model.UserModel;
import org.si.diamond.web.service.IUserService;

public class UserServiceImpl extends BaseServiceImpl implements IUserService {
	
	protected Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	private IBaseDao<String, UserModel> userDao;
	
	public IBaseDao<String, UserModel> getUserDao() {
		return userDao;
	}

	public void setUserDao(IBaseDao<String, UserModel> userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserModel getUserById(String userId) throws BaseServiceException {
		try {
			return this.getUserDao().getById(userId);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<UserModel> getUserListByCriteria(UserModel criterion) throws BaseServiceException {
		return null;
	}

	/**
	 * Returns a single active user with the same user code as the one stated in the parameter
	 * If more than one record found, BaseServiceException will be thrown
	 */
	@Override
	public UserModel getUserByCode(String userCode) throws BaseServiceException {
		try {
			UserModel criterion = new UserModel();
			criterion.setUserCode(userCode);
			criterion.setUserStatus("ACTIVE_US");
			criterion.setStatus("ACTIVE_RS");
			List<UserModel> result = this.getUserDao().customSelect(criterion, "getByCode");
			if (result.size() == 0) {
				return null;
			} else if (result.size() == 1) {
				return result.get(0);
			} else {
				throw new BaseServiceException("Duplicate User with same user code");
			}
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	/**
	 * Returns a list of active user with the same user name as the one stated in the parameter
	 */
	@Override
	public List<UserModel> getUserByName(String name) throws BaseServiceException {
		try {
			UserModel criterion = new UserModel();
			criterion.setUserName("%" + name + "%");
			criterion.setUserStatus("ACTIVE_US");
			criterion.setStatus("ACTIVE_RS");
			List<UserModel> result = this.getUserDao().customSelect(criterion, "getByCriteria");
			if (result.size() == 0) {
				return null;
			} else {
				return result;
			}
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

}
