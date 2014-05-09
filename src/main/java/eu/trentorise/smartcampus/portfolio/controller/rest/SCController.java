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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import eu.trentorise.smartcampus.aac.AACService;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.social.SocialEngine;

@Controller
public class SCController {

	private static final Logger logger = Logger.getLogger(SCController.class);

	@Autowired
	private SocialEngine socialEngine;

	@Autowired
	@Value("${aacURL}")
	private String aacURL;

	@Autowired
	@Value("${aacExtURL}")
	private String aacExtURL;

	@Autowired
	@Value("${smartcampus.client.id}")
	private String client_id;

	@Autowired
	@Value("${smartcampus.client.secret}")
	private String client_secret;

	protected AACService aacService;
	protected BasicProfileService profileService;

	@PostConstruct
	public void init() {
		aacService = new AACService(aacExtURL, client_id, client_secret);
		profileService = new BasicProfileService(aacURL);
	}

	/**
	 * Here we assume that the access token is placed in the current security
	 * context by the PRE_AUTH_FILTER
	 * 
	 * @return
	 */
	protected String getToken(HttpServletRequest request) {
		String fromCtx = (String) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		// logger.info("portfolio.web usertoken: " + fromCtx);
		return fromCtx;
	}

	/*
	 * Getters and Setters
	 */
	protected BasicProfile getBasicProfile(HttpServletRequest request)
			throws SecurityException, ProfileServiceException {
		BasicProfile bp = profileService.getBasicProfile(getToken(request));
		return bp;
	}

	protected BasicProfile getBasicProfile(HttpServletRequest request,
			String userId) throws SecurityException, ProfileServiceException {
		BasicProfile bp = profileService.getBasicProfile(userId,
				getToken(request));
		return bp;
	}

	protected AccountProfile getAccountProfile(HttpServletRequest request)
			throws SecurityException, ProfileServiceException {
		return profileService.getAccountProfile(getToken(request));
	}

	protected boolean canRead(PortfolioUser user, String entityId) {
		return socialEngine.checkPermission(user, entityId);
	}

}
