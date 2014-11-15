package org.si.diamond.web.task;

import org.apache.log4j.Logger;
import org.si.diamond.base.exception.BaseException;
import org.si.diamond.task.BaseTask;

public class WatchDogTask implements BaseTask {

	private static Logger logger = Logger.getLogger(WatchDogTask.class);
	
	@Override
	public void doTask() throws BaseException {
		logger.debug("WatchDog Task Execute");
	}

}
