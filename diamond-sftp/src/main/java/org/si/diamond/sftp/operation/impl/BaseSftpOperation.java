/*
 * File Name	: $filename
 * Author		: @$user.name
 * Created Date	: $date $time
 *
 * Copyright (c) 2014 Solveware Independent. All Rights Reserved. <BR/>
 * <BR/>
 * This software contains confidential and proprietary information of Solveware Independent.<BR/>
 */

package org.si.diamond.sftp.operation.impl;

import com.jcraft.jsch.ChannelSftp;
import org.apache.log4j.Logger;
import org.si.diamond.sftp.exception.SftpException;
import org.si.diamond.sftp.operation.SftpOperation;
import org.si.diamond.sftp.provider.DefaultSftpChannelProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by adelwin on 15/11/2014.
 */
public class BaseSftpOperation implements SftpOperation {
	protected Logger logger = Logger.getLogger(BaseSftpOperation.class);
	protected DefaultSftpChannelProvider channelProvider;

	public DefaultSftpChannelProvider getChannelProvider() {
		return channelProvider;
	}

	public void setChannelProvider(DefaultSftpChannelProvider channelProvider) {
		this.channelProvider = channelProvider;
	}

	@Override
	public void download(String remoteFile, String localFile) throws SftpException {
		logger.debug("starting download operation");
		logger.debug("remote file is [" + remoteFile + "]");
		logger.debug("local file is [" + localFile + "]");

		logger.debug("connecting the sftp provider");
		if (this.getChannelProvider().isConnected() == false) {
			this.getChannelProvider().connect();
		}

		int mode = ChannelSftp.APPEND;

		try {
			logger.debug("checking intended output file");
			File outputFileFile = new File(localFile);
			if (outputFileFile.exists()) {
				logger.info("local file exists, set for overwrite");
				mode = ChannelSftp.OVERWRITE;
			}

			if (this.getChannelProvider().isConnected()) {
				logger.debug("downloading file");
				this.getChannelProvider().getChannelSftp().get(remoteFile, localFile, null, mode);
				logger.debug("file downloaded, size = [" + new File(localFile).getAbsoluteFile().length() + "]");
			} else {
				throw new IllegalStateException("channel provider reported state not connected");
			}
		} catch (com.jcraft.jsch.SftpException e) {
			logger.error("exception during download");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		} catch (IllegalStateException e) {
			logger.error("exception during download");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		}
	}

	@Override
	public void upload(String localFile, String remoteFile) throws SftpException {
		logger.debug("starting upload operation");
		logger.debug("remote file is [" + remoteFile + "]");
		logger.debug("local file is [" + localFile + "]");

		logger.debug("connecting the sftp provider");
		if (this.getChannelProvider().isConnected() == false) {
			this.getChannelProvider().connect();
		}

		int mode = ChannelSftp.OVERWRITE;
		try {
			if (this.getChannelProvider().isConnected()) {
				logger.debug("uploading file");
				this.getChannelProvider().getChannelSftp().put(localFile, remoteFile, null, mode);
				logger.debug("file uploaded, size = [" + new File(localFile).getAbsoluteFile().length() + "]");
				logger.debug("checking file in remote location");
				Vector<ChannelSftp.LsEntry> vectorResult = this.getChannelProvider().getChannelSftp().ls(remoteFile);
				if (vectorResult != null && vectorResult.size() > 0) {
					logger.debug("remote file can be selected and found");
				} else {
					throw new SftpException("Upload is successful, but file cannot be found in the remote location");
				}
			} else {
				throw new IllegalStateException("channel provider reported state not connected");
			}
		} catch (com.jcraft.jsch.SftpException e) {
			logger.error("exception during upload");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		} catch (IllegalStateException e) {
			logger.error("exception during upload");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		}
	}

	@Override
	public void removeRemoteFile(String remoteFile) throws SftpException {
		logger.debug("starting remote remove operation");
		logger.debug("remote file is [" + remoteFile + "]");

		logger.debug("connecting the sftp provider");
		if (this.getChannelProvider().isConnected() == false) {
			this.getChannelProvider().connect();
		}

		try {
			if (this.getChannelProvider().isConnected()) {
				logger.debug("removing file");
				this.getChannelProvider().getChannelSftp().rm(remoteFile);
				logger.debug("file removed");
			} else {
				throw new IllegalStateException("channel provider reported state not connected");
			}
		} catch (com.jcraft.jsch.SftpException e) {
			logger.error("exception during upload");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		} catch (IllegalStateException e) {
			logger.error("exception during upload");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		}
	}

	@Override
	public String getCurrentLocation() throws SftpException {
		logger.debug("retrieving current remote location");

		logger.debug("connecting the sftp provider");
		if (this.getChannelProvider().isConnected() == false) {
			this.getChannelProvider().connect();
		}

		try {
			if (this.getChannelProvider().isConnected()) {
				logger.debug("retrieve path");
				String currentPath = this.getChannelProvider().getChannelSftp().pwd();
				logger.debug("current remote path is [" + currentPath + "]");
				return currentPath;
			} else {
				throw new IllegalStateException("channel provider reported state not connected");
			}
		} catch (com.jcraft.jsch.SftpException e) {
			logger.error("exception during retrieval of path");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		} catch (IllegalStateException e) {
			logger.error("exception during retrieval of path");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		}
	}

	@Override
	public String getCurrentLocalLocation() throws SftpException {
		logger.debug("retrieving current remote location");

		logger.debug("connecting the sftp provider");
		if (this.getChannelProvider().isConnected() == false) {
			this.getChannelProvider().connect();
		}

		try {
			if (this.getChannelProvider().isConnected()) {
				logger.debug("retrieve path");
				String currentPath = this.getChannelProvider().getChannelSftp().lpwd();
				logger.debug("current remote path is [" + currentPath + "]");
				return currentPath;
			} else {
				throw new IllegalStateException("channel provider reported state not connected");
			}
		} catch (IllegalStateException e) {
			logger.error("exception during retrieval of path");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		}
	}

	@Override
	public List<String> listRemoteFiles() throws SftpException {
		logger.debug("retrieving file list on current path");
		String currentPath = this.getCurrentLocation();
		logger.debug("current path is [" + currentPath + "]");

		logger.debug("connecting the sftp provider");
		if (this.getChannelProvider().isConnected() == false) {
			this.getChannelProvider().connect();
		}

		try {
			if (this.getChannelProvider().isConnected()) {
				logger.debug("retrieving file list");
				List<ChannelSftp.LsEntry> fileList = this.getChannelProvider().getChannelSftp().ls(currentPath);
				logger.debug("found [" + fileList.size() + "] files");
				List<String> returnList = new ArrayList<String>();
				for (ChannelSftp.LsEntry currentEntry : fileList) {
					logger.debug("file = " + currentEntry.getLongname());
					returnList.add(currentEntry.getLongname());
				}
				return returnList;
			}
		} catch (com.jcraft.jsch.SftpException e) {
			logger.error("exception during retrieval of file list");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<String> listRemoteFiles(String remotePath) throws SftpException {
		logger.debug("retrieving file list on current path");
		logger.debug("current path is [" + remotePath + "]");

		logger.debug("connecting the sftp provider");
		if (this.getChannelProvider().isConnected() == false) {
			this.getChannelProvider().connect();
		}

		try {
			if (this.getChannelProvider().isConnected()) {
				logger.debug("retrieving file list");
				List<ChannelSftp.LsEntry> fileList = this.getChannelProvider().getChannelSftp().ls(remotePath);
				logger.debug("found [" + fileList.size() + "] files");
				List<String> returnList = new ArrayList<String>();
				for (ChannelSftp.LsEntry currentEntry : fileList) {
					logger.debug("file = " + currentEntry.getLongname());
					returnList.add(currentEntry.getLongname());
				}
				return returnList;
			}
		} catch (com.jcraft.jsch.SftpException e) {
			logger.error("exception during retrieval of file list");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String getLatestFile(String remotePath) throws SftpException {
		logger.debug("retrieving file list on current path");
		logger.debug("current path is [" + remotePath + "]");

		logger.debug("connecting the sftp provider");
		if (this.getChannelProvider().isConnected() == false) {
			this.getChannelProvider().connect();
		}

		try {
			if (this.getChannelProvider().isConnected()) {
				List<ChannelSftp.LsEntry> fileList = this.getChannelProvider().getChannelSftp().ls(remotePath);
				logger.debug("found [" + fileList.size() + "] files");
				String returnFile = new String();
				Long maxMtime = 0L;
				for (ChannelSftp.LsEntry currentEntry : fileList) {
					Long currentMtime = Long.valueOf(currentEntry.getAttrs().getMTime());
					if (currentMtime > maxMtime) {
						if (currentEntry.getFilename().equalsIgnoreCase(".") || currentEntry.getFilename().equalsIgnoreCase("..")) {
							continue;
						} else {
							returnFile = currentEntry.getFilename();
							maxMtime = currentMtime;
						}
					}
				}
				logger.info("latest file is [" + returnFile + "]");
				return returnFile;
			}
		} catch (com.jcraft.jsch.SftpException e) {
			logger.error("exception during retrieval of file list");
			logger.error(e.getMessage(), e);
			throw new SftpException(e.getMessage(), e);
		}
		return null;
	}
}