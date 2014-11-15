/*
 * File Name	: $filename
 * Author		: @$user.name
 * Created Date	: $date $time
 *
 * Copyright (c) 2014 Solveware Independent. All Rights Reserved. <BR/>
 * <BR/>
 * This software contains confidential and proprietary information of Solveware Independent.<BR/>
 */

package org.si.diamond.sftp.provider;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.si.diamond.sftp.exception.SftpException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by adelwin on 15/11/2014.
 */
public class DefaultSftpChannelProvider {

	protected Logger logger = Logger.getLogger(DefaultSftpChannelProvider.class);
	private boolean connected = false;
	private String userName;
	private String password;
	private String host;
	private String port;
	private String publicKeyLocation;
	private String keyLocation;
	private String maxRetry;
	private String retryInterval;

	private JSch jschSshChannel;
	private Session sshSession;
	private ChannelSftp channelSftp;

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPublicKeyLocation() {
		return publicKeyLocation;
	}

	public void setPublicKeyLocation(String publicKeyLocation) {
		this.publicKeyLocation = publicKeyLocation;
	}

	public String getKeyLocation() {
		return keyLocation;
	}

	public void setKeyLocation(String keyLocation) {
		this.keyLocation = keyLocation;
	}

	public String getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(String maxRetry) {
		this.maxRetry = maxRetry;
	}

	public String getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(String retryInterval) {
		this.retryInterval = retryInterval;
	}

	public JSch getJschSshChannel() {
		return jschSshChannel;
	}

	public void setJschSshChannel(JSch jschSshChannel) {
		this.jschSshChannel = jschSshChannel;
	}

	public Session getSshSession() {
		return sshSession;
	}

	public void setSshSession(Session sshSession) {
		this.sshSession = sshSession;
	}

	public ChannelSftp getChannelSftp() {
		return channelSftp;
	}

	public void setChannelSftp(ChannelSftp channelSftp) {
		this.channelSftp = channelSftp;
	}

	public DefaultSftpChannelProvider() {
		this.jschSshChannel = new JSch();
	}

	public synchronized void connect() throws SftpException {
		if (this.isConnected()) {
			throw new IllegalStateException("already connected");
		}
		try {
			if (this.getKeyLocation() != null && this.getKeyLocation().trim().length() > 0) {
				logger.debug("setting key location at [" + this.getKeyLocation() + "]");
				this.jschSshChannel.addIdentity(this.getKeyLocation());
			}

			logger.debug("Checking possible authentication mechanism");
			if (this.getKeyLocation() != null && this.getKeyLocation().trim().length() > 0) {
				logger.debug("key location provided at [" + this.getKeyLocation() + "], authenticating with public key");
				this.jschSshChannel.addIdentity(this.getUserName(), this.readPrivateKeyFromFile(this.getKeyLocation()), null, null);
			}
			logger.debug("Creating SSH Session");
			this.sshSession = jschSshChannel.getSession(this.userName, this.host, Integer.parseInt(this.port));

			sshSession.setConfig("StrictHostKeyChecking", "no");

			if (this.getPassword() != null && this.getPassword().trim().length() > 0) {
				logger.debug("password provided, authenticating with password");
				this.sshSession.setPassword(this.password);
			}

			if (	(this.getKeyLocation() == null || this.getKeyLocation().trim().length() == 0)
					&&	(this.getPassword() == null || this.getPassword().trim().length() == 0)) {
				logger.debug("no possible method of authentication");
				throw new SftpException("no possible method of authentication");
			}

			logger.debug("trying to connect");
			int retryCount = 0;
			while (retryCount <= Integer.parseInt(this.getMaxRetry())) {
				try {
					logger.debug("retry count = [" + retryCount + "]");
					logger.debug("connecting");
					this.getSshSession().connect();
					logger.debug("connected");
					Channel channel = this.getSshSession().openChannel("sftp");
					this.setChannelSftp((ChannelSftp) channel);
					this.getChannelSftp().connect();
					this.setConnected(true);
					return;
				} catch (JSchException e) {
					logger.error("connect failed");
					logger.error(e.getMessage(), e);
					retryCount++;
					if (retryCount >= Integer.parseInt(this.getMaxRetry())) {
						logger.error("failed to connect to specified host", e);
						throw new SftpException(e.getMessage(), e);
					} else {
						logger.debug("sleeping before another retry");
						try {
							Thread.sleep(Integer.parseInt(this.getRetryInterval()));
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		} catch (JSchException e) {
			logger.error("failed to connect to specified host", e);
			throw new SftpException(e.getMessage(), e);
		} finally {
		}
	}

	private byte[] readPrivateKeyFromFile(String filePath) throws SftpException {
		logger.debug("reading private key");
		logger.debug("checking parameters");
		if (filePath == null || filePath.trim().length() == 0) {
			throw new SftpException("Private Key File not specified");
		}

		File privateKeyFile = new File(filePath);
		if (!privateKeyFile.exists() || !privateKeyFile.canRead() || !privateKeyFile.isFile()) {
			throw new SftpException("Private Key File no accessible");
		}

		try {
			logger.debug("reading as byte array");
			byte[] returnBytes = IOUtils.toByteArray(new FileInputStream(privateKeyFile));
			logger.debug("files read as bytes, returning");
			return returnBytes;
		} catch (IOException e) {
			logger.error("exception during reading private key file");
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw new SftpException(e.getMessage(), e);
		}
	}

	public synchronized void disconnect() throws SftpException {
		if (this.isConnected()) {
			if (this.getChannelSftp().isConnected()) {
				this.getChannelSftp().disconnect();
			}
			if (this.getSshSession().isConnected()) {
				this.getSshSession().disconnect();
			}
			this.setConnected(false);
		}
	}
}
