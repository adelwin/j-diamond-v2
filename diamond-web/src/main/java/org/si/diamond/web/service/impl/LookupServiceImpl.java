package org.si.diamond.web.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.si.diamond.base.dao.IBaseDao;
import org.si.diamond.base.exception.BaseDaoException;
import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.impl.BaseServiceImpl;
import org.si.diamond.web.model.LookupModel;
import org.si.diamond.web.service.ILookupService;

public class LookupServiceImpl extends BaseServiceImpl implements ILookupService {
	
	protected Logger logger = Logger.getLogger(LookupServiceImpl.class);
	
	private IBaseDao<String, LookupModel> lookupDao;
	
	public IBaseDao<String, LookupModel> getLookupDao() {
		return lookupDao;
	}

	public void setLookupDao(IBaseDao<String, LookupModel> lookupDao) {
		this.lookupDao = lookupDao;
	}

	@Override
	public List<LookupModel> getLookupByType(String lookupType) throws BaseServiceException {
		try {
			LookupModel criterion = new LookupModel();
			criterion.setLookupType(lookupType);
			return lookupDao.customSelect(criterion, "getByType");
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}
	
	
}
