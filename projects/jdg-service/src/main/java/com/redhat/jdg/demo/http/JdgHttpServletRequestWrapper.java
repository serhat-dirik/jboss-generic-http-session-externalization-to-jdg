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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class JdgHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private HttpServletRequest original;

	public JdgHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.original = request;
	}

	public HttpSession getSession() {

		HttpSession orgSession = original.getSession();

		if (orgSession == null || orgSession instanceof JdgHttpSession)
			return orgSession;
		// Wrap and return
		JdgHttpSession gridSession = new JdgHttpSession(orgSession,this.getRemoteUser());

		return gridSession;
	}

	public HttpSession getSession(boolean arg0) {
		HttpSession session = original.getSession(arg0);
		if (session != null) {
			if (session instanceof JdgHttpSession)
				return session;
			// Wrap and return
			JdgHttpSession gridSession = new JdgHttpSession(session,this.getRemoteUser());
			return gridSession;
		} else {
			return null;
		}
	}
}
