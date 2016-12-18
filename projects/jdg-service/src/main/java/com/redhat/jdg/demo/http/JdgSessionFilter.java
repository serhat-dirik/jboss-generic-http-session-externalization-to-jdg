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

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
/**
 * 
 * @author serhatSessionBean
 *
 */
public class JdgSessionFilter implements Filter {

	// private final String JDGSESSION = "JDGSESSION";

	private static final Logger log = Logger.getLogger(JdgSessionFilter.class.getName());

	public void destroy() {
		log.fine("Destroy !! ");
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		log.fine("doFilter() !! ");

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (request.getUserPrincipal() != null && request.isUserInRole("jdguser")
				&& !(request instanceof JdgHttpServletRequestWrapper)) {
			request = new JdgHttpServletRequestWrapper(request);
		}

		/*
		 * String jdgsession = null;
		 * 
		 * // Check if we have a session in the cookie Cookie[] cookies =
		 * request.getCookies(); if ( cookies != null ) { for ( Cookie c :
		 * cookies ) { if ( c.getName().equalsIgnoreCase( JDGSESSION ) ) {
		 * jdgsession = c.getValue(); log.fine( "Old JDGSESSION: " + jdgsession
		 * ); ((GridHttpServletRequest)request).setJdgsession( jdgsession );
		 * ((GridHttpSession) request.getSession()).checkAndPopulateSession(); }
		 * } } // New JDGSession if (jdgsession == null) { GridAPI grid =
		 * GridFactory.getInstance(); jdgsession = grid.getNewUUID().toString();
		 * Cookie jdgcookie = new Cookie( "JDGSESSION", jdgsession );
		 * response.addCookie( jdgcookie ); log.fine( "New JDGSESSION: " +
		 * jdgsession ); ((GridHttpServletRequest)request).setJdgsession(
		 * jdgsession ); }
		 */
		// Change the original request by the wrapper
		chain.doFilter(request, response);
	}

	public void init(FilterConfig cfg) throws ServletException {
		log.fine("Init !! ");
	}

}
