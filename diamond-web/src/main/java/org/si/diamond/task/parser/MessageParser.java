/**
 * File Name    : MessageParser.java
 * Author       : adelwin
 * Created Date : Feb 12, 2011 10:20:03 PM
 */
package org.si.diamond.task.parser;

import java.util.List;
import java.util.Map;

import org.si.diamond.task.exception.ParseException;

public abstract class MessageParser {
	public abstract List<Map<String, Object>> parse(String text) throws ParseException;
	
	public List<Map<String, Object>> parse(String subject, String text) throws ParseException {
		return null;
	}
}
