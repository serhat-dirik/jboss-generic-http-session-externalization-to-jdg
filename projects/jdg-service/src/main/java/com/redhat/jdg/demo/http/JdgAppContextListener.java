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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.redhat.jdg.demo.resource.JdgResourcesConfig;

public class JdgAppContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String jdg_serverList = sce.getServletContext().getInitParameter("jdg_serverList");
		String jdg_cache = sce.getServletContext().getInitParameter("jdg_cache");
		if(jdg_serverList == null || jdg_cache == null){
			throw new IllegalStateException("jdg_serverList and jdg_cache context parameters must be specified!");
		}
		String jdg_userName = sce.getServletContext().getInitParameter("jdg_userName");
		String jdg_password = sce.getServletContext().getInitParameter("jdg_password");

		System.setProperty("infinispan.client.hotrod.server_list", jdg_serverList);
		System.setProperty("infinispan.client.hotrod.cache", jdg_cache);
		if(jdg_userName != null && jdg_password != null){
			System.setProperty("infinispan.client.hotrod.username", jdg_userName);
			System.setProperty("infinispan.client.hotrod.password", jdg_password);
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
        JdgResourcesConfig.closeRemoteCacheManager();
	}

}
