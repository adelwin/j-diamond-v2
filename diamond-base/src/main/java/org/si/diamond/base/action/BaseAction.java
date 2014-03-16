/**
 * File Name    : BaseAction.java
 * Author       : adelwin
 * Created Date : Jan 23, 2011 3:11:35 PM
 */
package org.si.diamond.base.action;

import org.apache.log4j.Logger;
import org.si.diamond.base.exception.BaseActionException;

import com.opensymphony.xwork2.ActionSupport;

public abstract class BaseAction extends ActionSupport {

	private static final long serialVersionUID = -7194179951025775996L;
	protected Logger logger = Logger.getLogger(BaseAction.class);

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() throws Exception {
		logger.debug("Execute Action on BaseAction invoked");
		logger.debug("Executing PreAction");
		this.preAction();
		logger.debug("PreAction Completed");
		
		logger.debug("Executing main action method");
		String originalActionResult = null;
		try {
			originalActionResult = this.action();
		} catch (Exception e) {
			logger.debug("Exception was thrown from inside the action() method");
			this.addActionError("An Error has occurred, for details of the error, please contact your system administrator");
			this.addActionError("Error Code : " + System.currentTimeMillis());
			this.addActionError(e.getMessage());
		}
		logger.debug("Main Action Method completed, Original Result = " + originalActionResult);
		
		logger.debug("Executing Post Action method");
		String finalResult = this.postAction(originalActionResult);
		logger.debug("Post Action Method completed, Result = " + finalResult);
		
		logger.debug("returning final result");
		return (finalResult == null ? BaseAction.ERROR : finalResult);
	}
	
	public void preAction() throws BaseActionException {
		// no implementation
	}
	
	public abstract String action() throws BaseActionException;
	
	public String postAction(String originalAction) throws BaseActionException {
		return originalAction;
	}
}
