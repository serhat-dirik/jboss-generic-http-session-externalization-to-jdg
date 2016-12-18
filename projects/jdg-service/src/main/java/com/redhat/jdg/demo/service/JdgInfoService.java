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

package com.redhat.jdg.demo.service;

import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.infinispan.client.hotrod.RemoteCache;

import com.redhat.jdg.demo.resource.JdgResourcesConfig;

@Path("/cache")
public class JdgInfoService {

	@GET
	@POST
	@Path("/stats")
	@Produces({ "application/javascript" })
	public String getCacheStats(@QueryParam("callback") String callback) {
		//Convert object to JSON
	    JsonObjectBuilder builder = Json.createObjectBuilder();
	    JsonArrayBuilder jsonArray = Json.createArrayBuilder();
	    RemoteCache<Object, Object> cache = JdgResourcesConfig.remoteCache();
	    jsonArray.add(Json.createObjectBuilder().add("name", cache.getName() ));
	    jsonArray.add(Json.createObjectBuilder().add("size", cache.size() ));
	    for (Entry<String, String> entry : cache.stats().getStatsMap().entrySet()){
	    	jsonArray.add(Json.createObjectBuilder().add(entry.getKey() , entry.getValue()));
		}
	    builder.add("result", jsonArray);
	    //return as callback javascript padding
	    return (callback + "(" + builder.build().toString() + ")");
	}

}
