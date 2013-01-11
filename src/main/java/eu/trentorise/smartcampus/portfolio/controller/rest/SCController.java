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

import it.unitn.disi.sweb.webapi.client.WebApiException;
import it.unitn.disi.sweb.webapi.client.smartcampus.SCWebApiClient;
import it.unitn.disi.sweb.webapi.model.smartcampus.ac.Operation;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.AcServiceException;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.ac.provider.model.Attribute;
import eu.trentorise.smartcampus.ac.provider.model.User;

@Controller
public class SCController {

	@Autowired
	private AcService acService;

	@Autowired
	@Value("${smartcampus.vas.web.socialengine.host}")
	private String socialEngineHost;	
	@Autowired
	@Value("${smartcampus.vas.web.socialengine.port}")
	private int socialEnginePort;	

	private static final String ID_ADA = "idada";

	private SCWebApiClient client = null;
	
	protected SCWebApiClient getSCClient() {
		if (client == null) {
			client = SCWebApiClient.getInstance(Locale.ENGLISH, socialEngineHost, socialEnginePort);
		}
		return client;
	}
	
	protected String getUserToken(HttpServletRequest request) {
		return request.getHeader(AcProviderFilter.TOKEN_HEADER);
	}

	protected User getUser(HttpServletRequest request)
			throws AcServiceException {
		String token = getUserToken(request);
		User user = acService.getUserByToken(token);
		return user;
	}

	protected User getUser(String token) throws AcServiceException {
		User user = acService.getUserByToken(token);
		return user;
	}

	protected String getUserId(User user) {
		return (user != null) ? user.getId().toString() : null;
	}

	protected String getIdAda(User user) {
		String idAda = null;
		for (Attribute attr : user.getAttributes()) {
			if (attr.getKey().equals(ID_ADA)) {
				idAda = attr.getValue();
			}
		}
		return idAda;
	}
	
	protected boolean canRead(Long socialActorId, Long entityId) throws WebApiException {
		return getSCClient().readPermission(socialActorId, entityId, Operation.READ);
	}
}
