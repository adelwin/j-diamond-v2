/*
 * File Name	: $filename
 * Author		: @$user.name
 * Created Date	: $date $time
 *
 * Copyright (c) 2014 Solveware Independent. All Rights Reserved. <BR/>
 * <BR/>
 * This software contains confidential and proprietary information of Solveware Independent.<BR/>
 */

package org.si.diamond.sftp.operation;

import org.si.diamond.sftp.exception.SftpException;

import java.util.List;

/**
 * Created by adelwin on 15/11/2014.
 */
public interface SftpOperation {

	public void download(String remoteFile, String localFile) throws SftpException;
	public void upload(String remoteFile, String localFile) throws SftpException;
	public void removeRemoteFile(String remoteFile) throws SftpException;
	public String getCurrentLocation() throws SftpException;
	public String getCurrentLocalLocation() throws SftpException;
	public List<String> listRemoteFiles() throws SftpException;
	public List<String> listRemoteFiles(String remotePath) throws SftpException;
	public String getLatestFile(String remotePath) throws SftpException;

}
