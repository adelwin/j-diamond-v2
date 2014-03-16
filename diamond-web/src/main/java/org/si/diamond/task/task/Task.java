package org.si.diamond.task.task;

import org.si.diamond.base.exception.BaseException;

public interface Task {
	public abstract void doTask() throws BaseException;
}
