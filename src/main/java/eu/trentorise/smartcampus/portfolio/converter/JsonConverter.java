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
package eu.trentorise.smartcampus.portfolio.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.trentorise.smartcampus.portfolio.data.Identity;
import eu.trentorise.smartcampus.portfolio.data.StudentInfoData;

public class JsonConverter {
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	private static final String FIELD_CONTENT = "content";
	private static final String FIELD_ID = "id";

	public static <T extends Identity> List<T> toClassList(String json,
			Class<T> classType) throws JsonParseException,
			JsonMappingException, IOException, JSONException {
		JSONArray list = new JSONArray(json);
		List<T> results = new ArrayList<T>();
		for (int i = 0; i < list.length(); i++) {
			results.add(toClass(list.getJSONObject(i).getString(FIELD_CONTENT),
					classType, list.getJSONObject(i).getString(FIELD_ID)));
		}

		return results;

	}

	public static <T extends Identity> T toClass(String json,
			Class<T> classType, String id) {

		try {
			if (id == null) {
				JSONObject j = new JSONObject(json);
				id = j.getString(FIELD_ID);
			}
			if (classType == StudentInfoData.class) {
				JSONObject j = new JSONObject(json);
				json = new JSONObject(j.getString(FIELD_CONTENT))
						.getString("studentData");
			}

			T obj = mapper.readValue(json, classType);
			obj.setId(id);
			return obj;
		} catch (Exception e) {
			throw new ClassCastException();
		}
	}

	public static String getId(String json) throws JSONException {
		JSONObject j = new JSONObject(json);
		return j.getString(FIELD_ID);
	}
}
