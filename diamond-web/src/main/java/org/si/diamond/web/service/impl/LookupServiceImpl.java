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
	protected LookupModel ROOT = null;
	
	private IBaseDao<String, LookupModel> lookupDao;
	
	public IBaseDao<String, LookupModel> getLookupDao() {
		return lookupDao;
	}

	public void setLookupDao(IBaseDao<String, LookupModel> lookupDao) {
		this.lookupDao = lookupDao;
	}

	public void initialize() throws BaseServiceException {
		LookupModel rootCandidate = this.getLookupById("1");
		this.populateNode(rootCandidate);
		this.ROOT = rootCandidate;
	}

	private void populateNode(LookupModel node) throws BaseServiceException{
		List<LookupModel> nodeChild = this.getLookupByType(node.getLookupCode());
		if (nodeChild != null && nodeChild.size() > 0) {
			node.setChild(nodeChild);
			for (LookupModel currentNode : nodeChild) {
				this.populateNode(currentNode);
			}
		}
	}

	@Override
	public LookupModel getLookupById(String id) throws BaseServiceException {
		try {
			return this.getLookupDao().getById(id);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}

	@Override
	public LookupModel getLookupByCode(String code) throws BaseServiceException {
		try {
			LookupModel criterion = new LookupModel();
			criterion.setStatus("ACTIVE_RS");
			criterion.setLookupCode(code);
			List<LookupModel> returnCandidates = this.getLookupDao().getListByExample(criterion);
			if (returnCandidates == null) {
				return null;
			}
			if (returnCandidates.size() == 1) {
				return returnCandidates.get(1);
			} else if (returnCandidates.size() == 0) {
				return null;
			} else if (returnCandidates.size() > 1) {
				throw new BaseServiceException(("Error - Multiple Lookups with the same code [" + code + "]"));
			} else {
				throw new BaseServiceException(("Error - Unknown error when retrieving lookup with code [" + code + "]"));
			}
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}

	@Override
	public List<LookupModel> getLookupByType(String lookupType) throws BaseServiceException {
		try {
			LookupModel criterion = new LookupModel();
			criterion.setLookupType(lookupType);
			criterion.setStatus("ACTIVE_RS");
			return lookupDao.getListByExample(criterion);
		} catch (BaseDaoException e) {
			logger.error(e.getMessage(), e);
			throw new BaseServiceException(e.getMessage(), e);
		} finally {}
	}

	@Override
	public LookupModel findLookupByCode(LookupModel startFrom, String code) throws BaseServiceException {
		if (startFrom == null) {
			startFrom = this.ROOT;
		}
		if (startFrom.getLookupCode().equals(code)) {
			return startFrom;
		} else if (startFrom.getChild() != null && startFrom.getChild().size() > 0) {
			for (LookupModel child : startFrom.getChild()) {
				LookupModel returnCandidate = this.findLookupByCode(child, code);
				if (returnCandidate == null) {
					continue;
				} else {
					return returnCandidate;
				}
			}
		} else {
			return null;
		}
		return null;
	}
}
