/**
 * File Name    : BaseModel.java
 * Author       : adelwin
 * Created Date : Dec 29, 2010 3:13:29 PM
 */
package org.si.diamond.base.model;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.si.diamond.base.util.BeanUtil;

public class BaseModel implements Serializable {

	private Logger logger = Logger.getLogger(BaseModel.class);
	private static final long serialVersionUID = 6155755814969785686L;
	
	public String toString() {
		try {
			StringBuffer propertyString = new StringBuffer("{");
			propertyString.append("Class Name = " + this.getClass()).append(" -> ");
			List<String> beanPropertyList = BeanUtil.extractBeanFields(this.getClass());
			for (String beanPropertyName : beanPropertyList) {
				Object realBeanPropertyValue = BeanUtil.getProperty(this, beanPropertyName);
				if (realBeanPropertyValue != null) {
					realBeanPropertyValue = (String) realBeanPropertyValue.toString();
				}
				propertyString.append(" [" + beanPropertyName + "=" + realBeanPropertyValue + "]").append(" ");
			}
			return propertyString.toString();
		} catch (Exception e) {
			logger.error("failed invoking read method of property from class " + this.getClass());
			e.printStackTrace();
		}
		return super.toString();
	}

}
