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
package eu.trentorise.smartcampus.portfolio.util;

import it.sayservice.platform.client.DomainObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;

public class PortfolioUtils {

	private static ObjectMapper fullMapper = new ObjectMapper();
	static {
		fullMapper.setAnnotationIntrospector(NopAnnotationIntrospector.nopInstance());
		fullMapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, true);
		fullMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		fullMapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, true);

		fullMapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, true);
		fullMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
	}

	public static <T> T convert(Object o, Class<T> cls) {
		return fullMapper.convertValue(o, cls);
	}

	/*
	 * Utils
	 */
	public static String listToJSON(List<String> list) {
		List<String> sorted = sortByTimestamp(list);
		if (sorted == null) {
			sorted = list;
		}

		String result = "[";
		for (String r : sorted) {
			result += r + ",";
		}

		if (result.length() > 1) {
			result = result.substring(0, result.length() - 1);
		}
		result += "]";
		return result;
	}

	private static List<String> sortByTimestamp(List<String> list) {
		List<String> result = new ArrayList<String>();
		List<DomainObject> objects = new ArrayList<DomainObject>();
		Map<DomainObject, String> objectsMap = new HashMap<DomainObject, String>();

		try {
			for (String s : list) {
				DomainObject obj = new DomainObject(s);
				objects.add(obj);
				objectsMap.put(obj, s);
			}
		} catch (Exception e) {
			return null;
		}

		Collections.sort(objects, new Comparator<DomainObject>() {

			@Override
			public int compare(DomainObject o1, DomainObject o2) {
				if (!o1.getContent().containsKey("timestamp") || !o2.getContent().containsKey("timestamp")) {
					return 0;
				}
				long timestamp1 = (Long) o1.getContent().get("timestamp");
				long timestamp2 = (Long) o2.getContent().get("timestamp");

				return (int) (timestamp1 - timestamp2);

			}
		});

		for (DomainObject obj : objects) {
			result.add(objectsMap.get(obj));
		}

		Collections.reverse(result);

		return result;
	}

}
