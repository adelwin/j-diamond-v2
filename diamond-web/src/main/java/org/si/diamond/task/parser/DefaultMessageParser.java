/**
 * File Name    : DefaultMessageParser.java
 * Author       : adelwin
 * Created Date : Feb 12, 2011 10:21:15 PM
 */
package org.si.diamond.task.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.si.diamond.base.util.GUIDUtil;
import org.si.diamond.task.exception.ParseException;

public class DefaultMessageParser extends MessageParser {

	public List<Map<String, Object>> parse(String subject, String text) throws ParseException {
		try {
			String subSubject = subject.split(":")[1];
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			Date transactionDate = sdf.parse(subSubject);

			List<Map<String, Object>> returnValue = new ArrayList<Map<String, Object>>();
			
			StringTokenizer stringTokenizer1 = new StringTokenizer(text, "\n");
			while (stringTokenizer1.hasMoreTokens()) {

				String line = stringTokenizer1.nextToken();
				if (line.trim().equals("-")) break;
				
				StringTokenizer stringTokenizer2 = new StringTokenizer(line, ":");
					
				String accountId = stringTokenizer2.nextToken().trim().toLowerCase();
				String currency = stringTokenizer2.nextToken().trim();
				String description = stringTokenizer2.nextToken().trim();
				double amount = Double.parseDouble(stringTokenizer2.nextToken().trim());
				String txType = stringTokenizer2.nextToken().trim();
				
				Map<String, Object> tx = new HashMap<String, Object>();
				
				tx.put("Account", accountId);
				tx.put("Currency", currency);
				tx.put("TransactionDescription", description);
				tx.put("Amount", amount);
				tx.put("TransactionTime", transactionDate);
				tx.put("TransactionId", GUIDUtil.getGUID());
				tx.put("TransactionType", txType);
				
				returnValue.add(tx);
			}
			return returnValue;
		} catch (NoSuchElementException e) {
			throw new ParseException(e.getMessage(), e);
		} catch (java.text.ParseException e) {
			throw new ParseException("Unknown Date format in the subject", e);
		}
	}
	
	public List<Map<String, Object>> parse(String text) throws ParseException {
		try {
			List<Map<String, Object>> returnValue = new ArrayList<Map<String, Object>>();
			
			StringTokenizer stringTokenizer1 = new StringTokenizer(text, "\n");
			while (stringTokenizer1.hasMoreTokens()) {

				String line = stringTokenizer1.nextToken();
				if (line.trim().equals("-")) break;
				
				StringTokenizer stringTokenizer2 = new StringTokenizer(line, ":");
					
				String accountId = stringTokenizer2.nextToken().trim().toLowerCase();
				String currency = stringTokenizer2.nextToken().trim();
				String description = stringTokenizer2.nextToken().trim();
				double amount = Double.parseDouble(stringTokenizer2.nextToken().trim());
				String txType = stringTokenizer2.nextToken().trim();
				
				Map<String, Object> tx = new HashMap<String, Object>();
				
				tx.put("Account", accountId);
				tx.put("Currency", currency);
				tx.put("TransactionDescription", description);
				tx.put("Amount", amount);
				tx.put("TransactionId", GUIDUtil.getGUID());
				tx.put("TransactionType", txType);
				
				returnValue.add(tx);
			}
			return returnValue;
		} catch (NoSuchElementException e) {
			throw new ParseException(e.getMessage(), e);
		}
	}
	
}
