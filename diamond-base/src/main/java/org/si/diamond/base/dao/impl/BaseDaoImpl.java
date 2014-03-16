/**
 * File Name    : BaseDaoImpl.java
 * Author       : adelwin
 * Created Date : Jan 23, 2011 11:41:35 AM
 */
package org.si.diamond.base.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.si.diamond.base.dao.IBaseDao;
import org.si.diamond.base.exception.BaseDaoException;
import org.si.diamond.base.model.BaseModel;

/**
 * a base class for every Data Access Object used in this framework
 * there is no need to implement this DAO object for every type of base model being used since generics is being used here
 * implement to use by doing as such
 * <code>
 * IBaseDao<String, UserModel> userDao = new BaseDaoImpl<String, UserModel>();
 * </code>
 * 
 * @author adelwin
 *
 * @param <ID> type of the primary key of the base model class being used to implement this base dao
 * @param <M> base model class being used to implement this dao
 */

public class BaseDaoImpl<ID extends Serializable, M extends BaseModel> extends SqlSessionDaoSupport implements IBaseDao<ID, M> {
	protected Logger logger = Logger.getLogger(BaseDaoImpl.class);
	protected Class<? extends BaseModel> modelClass;

	/**
	 * @return the modelClass
	 */
	public Class<? extends BaseModel> getModelClass() {
		return modelClass;
	}

	/**
	 * @param modelClass the modelClass to set
	 */
	public void setModelClass(Class<? extends BaseModel> modelClass) {
		this.modelClass = modelClass;
	}
	
	public StringBuffer getModelClassName() {
		return new StringBuffer(this.modelClass.getName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public M getById(ID id) throws BaseDaoException {
		logger.debug("trying to execute statement : " + this.getModelClassName().append(".getById").toString());
		return (M) this.getSqlSession().selectOne(this.getModelClassName().append(".getById").toString(), id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> getListByExample(M criterion) throws BaseDaoException {
		logger.debug("trying to execute statement : " + this.getModelClassName().append(".getListByExample").toString());
		return (List<M>) this.getSqlSession().selectList(this.getModelClassName().append(".getListByExample").toString(), criterion);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<M> listAll() throws BaseDaoException {
		logger.debug("trying to execute statement : " + this.getModelClassName().append(".listAll").toString());
		return (List<M>) this.getSqlSession().selectList(this.getModelClassName().append(".listAll").toString());
	}

	@Override
	public int insert(M model) throws BaseDaoException {
		logger.debug("trying to execute statement : " + this.getModelClassName().append(".insert").toString());
		return this.getSqlSession().insert(this.getModelClassName().append(".insert").toString(), model);
	}

	@Override
	public int updateFull(M model) throws BaseDaoException {
		logger.debug("trying to execute statement : " + this.getModelClassName().append(".updateFull").toString());
		return this.getSqlSession().update(this.getModelClassName().append(".updateFull").toString(), model);
	}

	@Override
	public int updatePartial(M model, M sample) throws BaseDaoException {
		logger.debug("trying to execute statement : " + this.getModelClassName().append(".updatePartial").toString());
		return this.getSqlSession().update(this.getModelClassName().append(".updatePartial").toString(), model);
	}

	@Override
	public int delete(M model) throws BaseDaoException {
		logger.debug("trying to execute statement : " + this.getModelClassName().append(".delete").toString());
		return this.getSqlSession().delete(this.getModelClassName().append(".delete").toString(), model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<M> customSelect(M criterion, String queryName) throws BaseDaoException {
		return this.getSqlSession().selectList(this.getModelClassName().append("." + queryName).toString(), criterion);
	}

	/* (non-Javadoc)
	 * @see org.si.diamond.base.dao.IBaseDao#customExecute(org.si.diamond.base.model.BaseModel, java.lang.String)
	 */
	@Override
	public int customExecute(M criterion, String queryName) throws BaseDaoException {
		return this.getSqlSession().update(this.getModelClassName().append("." + queryName).toString(), criterion);
	}
}
