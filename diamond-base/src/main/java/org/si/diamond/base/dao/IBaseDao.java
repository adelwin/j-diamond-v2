/**
 * File Name    : IBaseDao.java
 * Author       : adelwin
 * Created Date : Jan 23, 2011 11:21:00 AM
 */
package org.si.diamond.base.dao;

import java.io.Serializable;
import java.util.List;

import org.si.diamond.base.exception.BaseDaoException;
import org.si.diamond.base.model.BaseModel;

/**
 * These methods are not implemented by BaseDaoImpl,
 * because these methods are a binding contract to those who implements this BaseDao
 * all implementation of this interface will have to implement these methods
 */
public interface IBaseDao<ID extends Serializable, M extends BaseModel> {
	public M getById(final ID id) throws BaseDaoException;
	public List<M> getListByExample(M criterion) throws BaseDaoException;
	public List<M> listAll() throws BaseDaoException;
	
	public int insert(M model) throws BaseDaoException;
	public int updateFull(M model) throws BaseDaoException;
	public int updatePartial(M model, M sample) throws BaseDaoException;
	public int delete(M model) throws BaseDaoException;
	
	public List<M> customSelect(M criterion, String queryName) throws BaseDaoException;
	public int customExecute(M criterion, String queryName) throws BaseDaoException;
}
