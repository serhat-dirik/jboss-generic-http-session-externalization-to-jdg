/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.redhat.jdg.demo.http;

import java.util.Enumeration;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.infinispan.client.hotrod.RemoteCache;

import com.redhat.jdg.demo.resource.JdgResourcesConfig;


@SuppressWarnings("deprecation")
public class JdgHttpSession implements HttpSession {

	private HttpSession original = null;
	private String prefix = null;
	
	private RemoteCache<Object, Object> cache = JdgResourcesConfig.remoteCache();
	private Logger log = Logger.getLogger(JdgHttpSession.class.getName());

	public JdgHttpSession(HttpSession original, String user) {
		super();
		this.original = original;
		this.prefix = user + "-";
	}

	public void setAttribute(String arg0, Object arg1) {
		log.fine("Catching attributes: (" + arg0 + ", " + arg1.toString() + ") ");
		log.fine("Updating session in the Grid");
		cache.put(this.prefix + arg0, arg1);
		this.original.removeAttribute(arg0);
	}

	public Object getAttribute(String arg0) {
		Object sessionVal = null;
		if(cache.containsKey(this.prefix + arg0)){
			sessionVal = cache.get(this.prefix + arg0);
			if (sessionVal == null)
				sessionVal = original.getAttribute(arg0);	
		}else{
			sessionVal = this.original.getAttribute(arg0);
		}
		
		return sessionVal;
	}

	public Enumeration<String> getAttributeNames() {
		Set<Object> keyset = cache.keySet();

		Vector<String> retSet = new Vector<String>();
		for (Object object : keyset) {
			if (object instanceof String) {
				String key = (String) object;
				if (key.startsWith(this.prefix)) {
					retSet.add(key.substring(this.prefix.length()));
				}
			}
		}
		Enumeration<String> origAttrs = original.getAttributeNames();
		while (origAttrs.hasMoreElements()) {
			String elem = origAttrs.nextElement();
			if (!retSet.contains(elem))
				retSet.addElement(elem);
		}
		return retSet.elements();
	}

	/**
	 * Delegated methods
	 */
	public long getCreationTime() {
		return original.getCreationTime();
	}

	public String getId() {
		return original.getId();
	}

	public long getLastAccessedTime() {
		return original.getLastAccessedTime();
	}

	public int getMaxInactiveInterval() {
		return original.getMaxInactiveInterval();
	}

	public ServletContext getServletContext() {
		return original.getServletContext();
	}

	@Deprecated
	public HttpSessionContext getSessionContext() {
		return original.getSessionContext();
	}

	public Object getValue(String arg0) {
		return original.getAttribute(arg0);
	}

	@Deprecated
	public String[] getValueNames() {
		return original.getValueNames();
	}

	public void invalidate() {
		original.invalidate();
	}

	public boolean isNew() {
		return original.isNew();
	}

	@Deprecated
	public void putValue(String arg0, Object arg1) {
		original.putValue(arg0, arg1);
	}

	public void removeAttribute(String arg0) {
		original.removeAttribute(arg0);
	}

	@Deprecated
	public void removeValue(String arg0) {
		original.removeValue(arg0);
	}

	public void setMaxInactiveInterval(int arg0) {
		original.setMaxInactiveInterval(arg0);
	}

}
