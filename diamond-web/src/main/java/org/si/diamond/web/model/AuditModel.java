package org.si.diamond.web.model;

import java.sql.Date;

import org.si.diamond.base.model.BaseModel;

public class AuditModel extends BaseModel {
	private static final long serialVersionUID = 3058027014693909279L;
	
	private String auditId;
	private String auditType;
	private String auditPk;
	private String auditor;
	private Date auditDate;
	private String beforeValue;
	private String tableName;
	
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getAuditPk() {
		return auditPk;
	}
	public void setAuditPk(String auditPk) {
		this.auditPk = auditPk;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getBeforeValue() {
		return beforeValue;
	}
	public void setBeforeValue(String beforeValue) {
		this.beforeValue = beforeValue;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
