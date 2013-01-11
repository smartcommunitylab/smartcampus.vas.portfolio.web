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
package eu.trentorise.smartcampus.portfolio.controller.rest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.portfolio.util.HTTPConnector;

@Controller
public class TokenController {

	@Value("${smartcampus.tokenvalidation.url}")
	private String acUrl;	

	@RequestMapping(method = RequestMethod.POST, value = "/validatetoken/{code}")
	public @ResponseBody String getToken(HttpServletResponse res, @PathVariable String code) {
		try {
			String body = HTTPConnector.doPost(acUrl+"/"+code, null, null, null, null);
			return body.trim();
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}
}
