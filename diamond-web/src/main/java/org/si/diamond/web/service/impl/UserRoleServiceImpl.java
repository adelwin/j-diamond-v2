package org.si.diamond.web.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.si.diamond.base.dao.IBaseDao;
import org.si.diamond.base.exception.BaseDaoException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.impl.BaseServiceImpl;
import org.si.diamond.web.model.UserRoleModel;
import org.si.diamond.web.service.IUserRoleService;

public class UserRoleServiceImpl extends BaseServiceImpl implements IUserRoleService {
	
	protected Logger logger = Logger.getLogger(UserRoleServiceImpl.class);
	
	private IBaseDao<String, UserRoleModel> userRoleDao;
	
	public IBaseDao<String, UserRoleModel> getUserRoleDao() {
		return userRoleDao;
	}

	public void setUserRoleDao(IBaseDao<String, UserRoleModel> userRoleDao) {
		this.userRoleDao = userRoleDao;
	}

	@Override
	public UserRoleModel getRoleById(String userRoleId) throws BaseServiceException {
		try {
			return this.userRoleDao.getById(userRoleId);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<UserRoleModel> getUserRoleListByCriteria(UserRoleModel criterion) throws BaseServiceException {
		try {
			return this.userRoleDao.getListByExample(criterion);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}

	@Override
	public List<UserRoleModel> listAll() throws BaseServiceException {
		try {
			UserRoleModel criterion = new UserRoleModel();
			criterion.setStatus("ACTIVE_RS");
			return this.userRoleDao.getListByExample(criterion);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		}
	}
	
	
}
