/**
 * File Name : FileUtil.java Author : adelwin.handoyo Create Date : 01-Jul-2009
 * 
 * Copyright (c) 2009 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of Adelwin.
 * ("Confidential Information").<BR>
 * <BR>
 */
package org.si.diamond.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.si.diamond.base.exception.BaseException;

/**
 * Type Name : FileUtil Package : 100.DiamondCoreClean Author : adelwin.handoyo
 * Created : AM 10:50:00
 */
public class FileUtil {

	public static boolean fileStreamCopy(File fromFile, File toFile) throws IOException, BaseException {
		if (!fromFile.exists()) throw new BaseException("FileCopy: " + "no such source file: " + fromFile.getName());
		if (!fromFile.isFile()) throw new BaseException("FileCopy: " + "can't copy directory: " + fromFile.getName());
		if (!fromFile.canRead()) throw new BaseException("FileCopy: " + "source file is unreadable: " + fromFile.getName());

		if (toFile.isDirectory()) toFile = new File(toFile, fromFile.getName());

		if (toFile.exists()) {
			throw new BaseException("FileCopy: " + "destination file already exist: " + toFile.getName());
		} else {
			String parent = toFile.getParent();
			if (parent == null) parent = System.getProperty("user.dir");
			File dir = new File(parent);
			if (!dir.exists()) throw new BaseException("FileCopy: " + "destination directory doesn't exist: " + parent);
			if (dir.isFile()) throw new BaseException("FileCopy: " + "destination is not a directory: " + parent);
			if (!dir.canWrite()) throw new BaseException("FileCopy: " + "destination directory is unwriteable: " + parent);
		}

		FileInputStream from = null;
		FileOutputStream to = null;
		try {
			from = new FileInputStream(fromFile);
			to = new FileOutputStream(toFile);
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = from.read(buffer)) != -1)
				to.write(buffer, 0, bytesRead); // write

		} finally {
			if (from != null) from.close();
			if (to != null) to.close();
		}
		if (fromFile.delete()) return false;
		return true;
	}
}
