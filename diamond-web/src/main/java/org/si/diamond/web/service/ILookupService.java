package org.si.diamond.web.service;

import java.util.List;

import org.si.diamond.base.exception.BaseServiceException;
import org.si.diamond.base.service.IBaseService;
import org.si.diamond.web.model.LookupModel;

public interface ILookupService extends IBaseService {

	public List<LookupModel> getLookupByType(String lookupType) throws BaseServiceException;

}
