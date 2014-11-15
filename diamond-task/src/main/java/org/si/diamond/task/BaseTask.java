package org.si.diamond.task;

import org.si.diamond.base.exception.BaseException;

public interface BaseTask {
	public abstract void doTask() throws BaseException;
}
