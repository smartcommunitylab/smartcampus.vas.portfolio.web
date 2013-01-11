/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.portfolio.manager;

import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.portfolio.util.HTTPConnector;

@Component
public class ProfileManager {
	public Map<String, Object> getProfile(String token, String serviceUri) throws Exception {
		String address = serviceUri + "/eu.trentorise.smartcampus.cm.model.Profile/current";
		Map<String, Object> reducedMap = new TreeMap<String, Object>();
		try {
			String result = HTTPConnector.doGet(address, null, null, null, token);
			if (result != null) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = mapper.readValue(result, Map.class);
				reducedMap.put("name", map.get("name"));
				reducedMap.put("surname", map.get("surname"));
				reducedMap.put("fullname", map.get("fullname"));
			}
		} catch (Exception e) {
			return null;
		}
		return reducedMap;
	}

	public Map<String, Object> getMinimalProfile(String userId, String token, String serviceUri) throws Exception {
		String address = serviceUri + "/eu.trentorise.smartcampus.cm.model.MinimalProfile/" + userId;
		Map<String, Object> reducedMap = new TreeMap<String, Object>();
		try {
			String result = HTTPConnector.doGet(address, null, null, null, token);
			if (result != null) {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> map = mapper.readValue(result, Map.class);
				reducedMap.put("name", map.get("name"));
				reducedMap.put("surname", map.get("surname"));
			}
		} catch (Exception e) {
			return null;
		}
		return reducedMap;
	}

	public String createProfile(String token, String json, String serviceUri) throws Exception {
		String address = serviceUri + "/eu.trentorise.smartcampus.cm.model.StoreProfile";
		String result = null;
		try {
			result = HTTPConnector.doPost(address, json, null, MediaType.APPLICATION_JSON, token);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
}
