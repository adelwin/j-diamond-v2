/**
 * File Name    : BaseContext.java
 * Author       : adelwin
 * Created Date : Dec 29, 2010 2:43:15 PM
 */
package org.si.diamond.base.context;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseContext implements Serializable {

	private static final long serialVersionUID = 8359523658098312976L;
	
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public Set<String> getKeySet() {
		return attributes.keySet();
	}
	
	public boolean containsKey(String key) {
		return attributes.containsKey(key);
	}
	
	public boolean containsValue(Object value) {
		return attributes.containsValue(value);
	}
	
	public void clearAttributes() {
		this.attributes.clear();
	}
	
	public Collection<Object> getValues() {
		return attributes.values();
	}
	
	public void remove(String key) {
		this.attributes.remove(key);
	}
	
	
}
