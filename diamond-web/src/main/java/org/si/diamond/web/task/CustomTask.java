package org.si.diamond.web.task;

import org.apache.log4j.Logger;
import org.si.diamond.base.exception.BaseException;
import org.si.diamond.task.task.Task;

public class CustomTask implements Task {

	private static Logger logger = Logger.getLogger(CustomTask.class);
	
	@Override
	public void doTask() throws BaseException {
		logger.debug("do task");
	}

}
