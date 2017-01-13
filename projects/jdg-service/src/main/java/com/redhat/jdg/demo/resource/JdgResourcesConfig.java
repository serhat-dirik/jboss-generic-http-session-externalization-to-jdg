/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other
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
package com.redhat.jdg.demo.resource;

import java.io.IOException;
import java.util.logging.Logger;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

/**
 * This is the configuration class for DG.
 */
public class JdgResourcesConfig {

	private static final Logger log = Logger.getLogger(JdgResourcesConfig.class.getName());

	private static RemoteCacheManager cacheManager = null;
	private static final String REALM = "ApplicationRealm";
	private static final String SERVER_NAME = "jdgserver";

	public static RemoteCache<Object, Object> remoteCache() {
		String cacheName = System.getProperty("infinispan.client.hotrod.cache");
		log.fine("\n\t ->>> Get the remote cache " + cacheName + "\n");
		return remoteCacheManager().getCache(cacheName);
	}

	/**
	 * Infinispan Security Realm configuration Security Realms are used by the
	 * server to provide authentication and authorization information for both
	 * the management and application interfaces
	 *
	 * <pre>
	 * {@code
	 *       <management>
	 *           <security-realm name="ApplicationRealm">
	 *               <authentication>
	 *                   <properties path=
	"application-users.properties" relative-to="jboss.server.config.dir"/>
	 *               </authentication>
	 *               <authorization>
	 *                   <properties path=
	"application-roles.properties" relative-to="jboss.server.config.dir"/>
	 *               </authorization>
	 *           </security-realm>
	 *       </management>
	 * }
	 * </pre>
	 *
	 * Enpoint subsystem definition: The following configuration enables
	 * authentication against ApplicationRealm, using the DIGEST-MD5 SASL
	 * mechanism:
	 *
	 * <pre>
	 * {@code
	 *       <subsystem xmlns="urn:infinispan:server:endpoint:6.1">
	 *          ...
	 *           <hotrod-connector socket-binding="hotrod" cache-container=
	"local">
	 *               <topology-state-transfer lazy-retrieval=
	"false" lock-timeout="1000" replication-timeout="5000"/>
	 *               <authentication security-realm="ApplicationRealm">
	 *                   <sasl server-name="jdgserver" mechanisms=
	"DIGEST-MD5" qop="auth">
	 *                       <policy>
	 *                           <no-anonymous value="true"/>
	 *                       </policy>
	 *                       <property name=
	"com.sun.security.sasl.digest.utf8">true</property>
	 *                   </sasl>
	 *               </authentication>
	 *           </hotrod-connector>
	 *           ...
	 *       </subsystem>
	 * }
	 * </pre>
	 *
	 * @return CacheManager
	 */

	public static RemoteCacheManager remoteCacheManager() {
		if (cacheManager == null) {
			String serverList = System.getProperty("infinispan.client.hotrod.server_list");
			final String userName = System.getProperty("infinispan.client.hotrod.username");
			final String password = System.getProperty("infinispan.client.hotrod.password");
			log.fine("\n\t ->>> Builds the REMOTE-CacheManager \n Servers:" + serverList);// ,
																							// userName:"+userName);

			ConfigurationBuilder confBuilder = new org.infinispan.client.hotrod.configuration.ConfigurationBuilder()
					.tcpNoDelay(true).connectionPool().numTestsPerEvictionRun(3).testOnBorrow(true).testOnReturn(true)
					.testWhileIdle(true).addServers(serverList);
			org.infinispan.client.hotrod.configuration.Configuration remoteConf = null;
			if (userName != null && password != null) {
				remoteConf = confBuilder
						// add configuration for authentication
						.security().authentication().serverName(SERVER_NAME) // define
																				// server
																				// name,
																				// should
																				// be
																				// specified
																				// in
																				// XML
																				// configuration
						.saslMechanism("DIGEST-MD5") // define SASL mechanism,
														// in this example we
														// use DIGEST with MD5
														// hash
						.callbackHandler(new CallbackHandler() {

							@Override
							public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
								for (Callback callback : callbacks) {
									if (callback instanceof NameCallback) {
										((NameCallback) callback).setName(userName);
									} else if (callback instanceof PasswordCallback) {
										((PasswordCallback) callback).setPassword(password.toCharArray());
									} else if (callback instanceof RealmCallback) {
										((RealmCallback) callback).setText(REALM);
									} else {
										throw new UnsupportedCallbackException(callback);
									}
								}
							}
						}) // define login handler, implementation defined
						.enable().build();
			} else {
				remoteConf = confBuilder.build();
			}
			cacheManager = new RemoteCacheManager(remoteConf);
		}
		return cacheManager;
	}

	public static void closeRemoteCacheManager() {
		log.fine("\n\t ->>> Stops REMOTE-cache's RemoteCacheManager \n");
		remoteCacheManager().stop();
		cacheManager = null;
	}

}
