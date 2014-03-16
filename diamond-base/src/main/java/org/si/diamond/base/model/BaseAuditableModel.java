/**
 * File Name    : BaseAuditableModel.java
 * Author       : adelwin
 * Created Date : Dec 29, 2010 3:13:29 PM
 */
package org.si.diamond.base.model;

import java.util.Date;

public class BaseAuditableModel extends BaseModel {

	private static final long serialVersionUID = 6155755814969785686L;
	private String tableName;
	private String createBy;
	private Date createDate;
	private String updateBy;
	private Date updateDate;
	private String status;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
