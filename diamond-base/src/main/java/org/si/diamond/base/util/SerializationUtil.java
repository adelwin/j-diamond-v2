/**
 * File Name	: SerializationUtil.java
 * Author		: Administrator:Adelwin
 * Create Date	: Mar 13, 2005:9:29:58 PM
 *
 * Copyright (c) 2005 Adelwin. All Rights Reserved. <BR>
 * <BR>
 * This software contains confidential and proprietary information of
 * Adelwin. ("Confidential Information").<BR>
 * <BR>
 * Such Confidential Information shall not be disclosed and it shall
 * only be used in accordance with the terms of the license agreement
 * entered into with Solveware Independent; other than in accordance with the written
 * permission of Solveware Independent. <BR>
 *
 *
 */

package org.si.diamond.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.si.diamond.base.exception.SerializationException;

public class SerializationUtil {
	
	public static final Object clone(Serializable object) throws SerializationException {
		return deserializeFromByte(serializeToByte(object));
	}

	public static void serializeToStream(Serializable obj, OutputStream outputStream) throws SerializationException {
        ObjectOutputStream out = null;
        try {
        	out = new ObjectOutputStream(outputStream);
        	out.writeObject(obj);
        } catch(IOException e) {
        	throw new SerializationException(e.getMessage(), e);
        } finally {
        	try {
        		if(out != null) out.close();
        	} catch(IOException e) {
        		throw new SerializationException(e.getMessage(), e);
        	}
        }
	}

	public static byte[] serializeToByte(Serializable obj) throws SerializationException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	serializeToStream(obj, ((OutputStream) (baos)));
    	return baos.toByteArray();
	}
	
	public static String serializeToString(Serializable obj) throws SerializationException {
		return new String(Base64.encodeBase64(serializeToByte(obj)));
	}
	
	public static Object deserializeFromStream(InputStream inputStream) throws SerializationException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(inputStream);
			Object obj = in.readObject();
			return obj;
        } catch(ClassNotFoundException e) {
        	throw new SerializationException(e.getMessage(), e);
        } catch(IOException e) {
        	throw new SerializationException(e);
        } finally {
        	try {
            	if(in != null) in.close();
        	} catch(IOException e) {
        		throw new SerializationException(e.getMessage(), e);
        	}
        }
	}

    public static Object deserializeFromByte(byte objectData[]) throws SerializationException {
    	ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
    	return deserializeFromStream(((InputStream) (bais)));
	}
    
    public static Object deserializeFromString(String strObject) throws SerializationException {
    	byte objectData[];
		try {
			objectData = Base64.decodeBase64(strObject);
			return deserializeFromByte(objectData);
		} catch (Exception e) {
			throw new SerializationException(e.getMessage(), e);
		}
    }
    
}
