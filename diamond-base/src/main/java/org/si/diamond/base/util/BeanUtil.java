/**
 * File Name	: BeanUtil.java
 * Author		: Administrator:Adelwin
 * Create Date	: Mar 13, 2006:1:22:45 AM
 *
 * Copyright (c) 2006 Adelwin. All Rights Reserved. <BR>
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



public class BeanUtil {
	public static final String GETTER_METHOD_PREFIX = "get";
	public static final String SETTER_METHOD_PREFIX = "set";
	public static final String METHOD_SPLICE_BEGIN_INDEX = "3";
	public static final String MODEL_NAME_POSTFIX = "Model";
	public static final String TABLE_NAME_SUFFIX = "s";
	public static final String ID_FIELD_SUFFIX = "Id";
	public static final String SINGLETON_ACCESSOR_METHOD_NAME = "getInstance";
	
	public static final String getFieldFromMethod(Method method) {
		String retVal = new String();
		retVal = method.getName().substring(Integer.parseInt(METHOD_SPLICE_BEGIN_INDEX));
		return retVal;
	}
	public static final String getFieldFromMethod(String methodName) {
		String retVal = new String();
		retVal = methodName.substring(Integer.parseInt(METHOD_SPLICE_BEGIN_INDEX));
		return retVal;
	}
	
	public static final String getMethodName(Method method) {
		String retVal;
		int lastDotPost = method.getName().lastIndexOf(".");
		retVal = method.getName().substring(lastDotPost + 1);
		return retVal;
	}
	
	/**
	 * extracts all the public property of a javabean
	 * @param clazz
	 * @return
	 */
	public static final List<Method> extractMethods(Class<?> clazz) {
		List<Method> retVal = new ArrayList<Method>();
		Method[] method = clazz.getMethods();
		for (int i = 0; i < method.length; i++) {
			retVal.add(method[i]);
		}
		return retVal;
	}
	public static final List<Method> extractBeanMethods(Class<?> clazz) {
		List<Method> retVal = BeanUtil.extractMethods(clazz);
		List<Method> objMethod = BeanUtil.extractMethods(Object.class);
		retVal.removeAll(objMethod);
		return retVal;
	}
	
	/**
	 * extracts only the getter methods of a javabean
	 * @param clazz
	 * @return
	 */
	public static final List<String> extractGetterMethods(Class<?> clazz) {
		List<String> retVal = new ArrayList<String>();
		Method[] method = clazz.getMethods();
		for (int i = 0; i < method.length; i++) {
			if (method[i].getName().startsWith(BeanUtil.GETTER_METHOD_PREFIX)) {
				retVal.add(method[i].getName());				
			}
		}
		return retVal;		
	}
	public static final List<String> extractBeanGetterMethods(Class<?> clazz) {
		List<String> retVal = BeanUtil.extractGetterMethods(clazz);
		List<String> objGetterMethod = BeanUtil.extractGetterMethods(Object.class);
		retVal.removeAll(objGetterMethod);
		return retVal;
	}
	
	/**
	 * extracts only the setter method of a javabean
	 * @param clazz
	 * @return
	 */
	public static final List<Method> extractSetterMethods(Class<?> clazz) {
		List<Method> retVal = new ArrayList<Method>();
		Method[] method = clazz.getMethods();
		for (int i = 0; i < method.length; i++) {
			if (method[i].getName().startsWith(BeanUtil.SETTER_METHOD_PREFIX)) {
				retVal.add(method[i]);
			}
		}
		return retVal;
	}
	public static final List<Method> extractBeanSetterMethods(Class<?> clazz) {
		List<Method> retVal = BeanUtil.extractSetterMethods(clazz);
		List<Method> objSetterMethod = BeanUtil.extractSetterMethods(Object.class);
		retVal.removeAll(objSetterMethod);
		return retVal;
	}
	
	/**
	 * extracts fields from a javabean
	 * @param clazz
	 * @return
	 */
	public static final List<String> extractFields(Class<?> clazz) {
		List<String> retVal = new ArrayList<String>();
		Method[] methods = clazz.getMethods();
		Method method;
		String methodName;
		for (int i = 0; i < methods.length; i++) {
			method = methods[i];
			if (method.getName().startsWith(GETTER_METHOD_PREFIX)) {
				methodName = BeanUtil.getFieldFromMethod(method);
				if (!retVal.contains(methodName)) {
					retVal.add(methodName);				
				}				
			}
		}
		return retVal;
	}
	public static final List<String> extractBeanFields(Class<?> clazz) {
		List<String> retVal = BeanUtil.extractFields(clazz);
		List<String> objField = BeanUtil.extractFields(Object.class);
		retVal.removeAll(objField);
		return retVal;
	}
	
	/**
	 * invoke method and returns it result(s)
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static final Object invokeMethod(Object caller, String methodName, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
//		Class clazz = caller.getClass();
		Method method = null;
		Class<?>[] paramTypes = (params == null ? null : getObjectTypes(params));
		method = findMethod(caller.getClass(), methodName, paramTypes);
		return (method == null ? null : method.invoke(caller, params));
	}
	public static final Method findMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, paramTypes);
			return method;
		} catch (NoSuchMethodException e) {
			return findMethod(clazz.getSuperclass(), methodName, paramTypes);
		} catch (NullPointerException e) {
			return null;
		}
	}
	public static Class<?>[] getObjectTypes(Object[] objs) {
		Class<?>[] objTypes = new Class[objs.length];
		for (int i = 0; i < objs.length; i++) {
			objTypes[i] = objs[i].getClass();
		}
		return objTypes;
	}
	/**
	 * extract table name from Model <BR>
	 * the algorithm is like this <BR>
	 * extract the last part of the full qualified class name <BR>
	 * by finding the last "." position, get the last part, <BR>
	 * splice that part, and add a trailing "S" as table name
	 */
	public static final String getTableName(Class<?> clazz) {
		String retVal = new String();
		int lastDotPost = clazz.getName().lastIndexOf(".");
		String modelName = clazz.getName().substring(lastDotPost + 1);
		retVal = modelName.substring(0, modelName.length() - MODEL_NAME_POSTFIX.length());
		retVal = (retVal + BeanUtil.TABLE_NAME_SUFFIX);
		return retVal;
	}

	/**
	 * extract id field from model <BR>
	 * the algorithm is like this <BR>
	 * extract the last part of the full qualified class name <BR>
	 * by finding the last "." position, get the last part, <BR>
	 * splice that part, and add a trailing "id" as id field
	 */
	public static final String getIdField(Class<?> clazz) {
		String retVal = new String();
		int lastDotPost = clazz.getName().lastIndexOf(".");
		String modelName = clazz.getName().substring(lastDotPost + 1);
		retVal = modelName.substring(0, modelName.length() - MODEL_NAME_POSTFIX.length());
		retVal = (retVal + BeanUtil.ID_FIELD_SUFFIX);
		return retVal;
	}
	
	/**
	 * extract id field value from model <BR>
	 * the algorithm is like this <BR>
	 * get the id field name first<BR>
	 * construct a getter method name from that id field name<BR>
	 * invoke that method name
	 */
	public static final Object getIdFieldValue(Object obj) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String idFieldName = getIdField(obj.getClass());
		Character[] idFieldNameC = StringUtil.convertToCharArray(idFieldName);
		
		idFieldNameC[0] = new Character(Character.toUpperCase(idFieldNameC[0].charValue()));
		idFieldName = new String(StringUtil.convertToNativeCharArray(idFieldNameC));
		String idGetterMethod = GETTER_METHOD_PREFIX + idFieldName;
		Object retVal = null;
		retVal = invokeMethod(obj, idGetterMethod, null);
		return retVal;
	}
	
	public static final Object getProperty(Object parent, String property) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Character chrPropertyName[] = StringUtil.convertToCharArray(property);
		chrPropertyName[0] = new Character(Character.toUpperCase(chrPropertyName[0].charValue()));
		String strPropertyName = new String(StringUtil.convertToNativeCharArray(chrPropertyName));
		String getterMethodName = BeanUtil.GETTER_METHOD_PREFIX + strPropertyName;
		return BeanUtil.invokeMethod(parent, getterMethodName, null);
	}
	
	public static final Object getProperty(Object parent, Field property) throws IllegalArgumentException, IllegalAccessException {
		return property.get(parent);
	}

	public static final Object instantiateClass(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> clazz = Class.forName(className);
		return instantiateClass(clazz);
	}
	
	public static final Object instantiateClass(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		return clazz.newInstance();
	}
}
