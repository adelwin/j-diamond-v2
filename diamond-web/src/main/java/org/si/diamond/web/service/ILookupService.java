package org.si.diamond.web.service;

import java.util.List;

import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.IBaseService;
import org.si.diamond.web.model.LookupModel;

public interface ILookupService extends IBaseService {

	public LookupModel getLookupById(String id) throws BaseServiceException;
	public LookupModel getLookupByCode(String code) throws BaseServiceException;
	public List<LookupModel> getLookupByType(String lookupType) throws BaseServiceException;

	public LookupModel findLookupByCode(LookupModel startFrom, String code) throws BaseServiceException;
}
