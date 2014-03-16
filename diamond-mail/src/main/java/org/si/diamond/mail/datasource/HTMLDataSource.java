package org.si.diamond.mail.datasource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class HTMLDataSource implements DataSource {

	private StringBuffer	htmlString;

	public HTMLDataSource(String _htmlString) {
		this.htmlString = new StringBuffer(_htmlString);
	}

	public String getContentType() {
		return "text/html";
	}

	public InputStream getInputStream() throws IOException {
		if (htmlString == null || htmlString.toString().trim().length() == 0) {
			throw new IOException("Empty buffer");
		}
		return new ByteArrayInputStream(htmlString.toString().getBytes());
	}

	public String getName() {
		return "JAF text/html DataSource to send eMail only";
	}

	public OutputStream getOutputStream() throws IOException {
		throw new IOException("This DataHandler cannot write HTML");
	}

}
