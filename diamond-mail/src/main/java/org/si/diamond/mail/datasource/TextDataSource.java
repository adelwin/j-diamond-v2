package org.si.diamond.mail.datasource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class TextDataSource implements DataSource {

	private StringBuffer	textString;

	public TextDataSource(String _textString) {
		this.textString = new StringBuffer(_textString);
	}

	public String getContentType() {
		return "text/plain";
	}

	public InputStream getInputStream() throws IOException {
		if (textString == null || textString.toString().trim().length() == 0) {
			throw new IOException("Empty buffer");
		}
		return new ByteArrayInputStream(textString.toString().getBytes());
	}

	public String getName() {
		return "JAF text/html DataSource to send eMail only";
	}

	public OutputStream getOutputStream() throws IOException {
		throw new IOException("This DataHandler cannot write text");
	}

}
