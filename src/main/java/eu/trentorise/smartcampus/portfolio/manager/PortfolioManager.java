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

import it.sayservice.platform.client.DomainEngineClient;
import it.sayservice.platform.client.DomainObject;
import it.sayservice.platform.client.InvocationException;
import it.sayservice.platform.core.common.util.ServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.portfolio.controller.rest.PortfolioUser;
import eu.trentorise.smartcampus.portfolio.models.Concept;
import eu.trentorise.smartcampus.portfolio.models.Portfolio;
import eu.trentorise.smartcampus.portfolio.models.UserProducedData;
import eu.trentorise.smartcampus.portfolio.util.PortfolioUtils;
import eu.trentorise.smartcampus.presentation.common.exception.DataException;
import eu.trentorise.smartcampus.presentation.storage.BasicObjectStorage;
import eu.trentorise.smartcampus.social.SocialEngine;

@Component
public class PortfolioManager {

	private static final Logger logger = Logger
			.getLogger(PortfolioManager.class);

	public static String STUDENT_EDU_TITLE = "University of Trento";
	public static String STUDENT_EDU_TYPE = "sys_simple";
	public static String STUDENT_EDU_CATEGORY = "education";

	@Autowired
	private DomainEngineClient domainClient;

	@Autowired
	private BasicObjectStorage porfolioStorage;

	@Autowired
	private SocialEngine socialEngine;

	private static ObjectMapper mapper = new ObjectMapper();

	public boolean createStudent(String userId, String unitnId)
			throws InvocationException, IOException, ClassNotFoundException {
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("newUserId", userId);
		pars.put("newIdAda", unitnId);
		byte[] b = (byte[]) domainClient.invokeDomainOperationSync(
				"createStudent", "smartcampus.services.esse3.StudentFactory",
				"smartcampus.services.esse3.StudentFactory.0", pars,
				"vas_portfolio_subscriber");
		String result = (String) ServiceUtil.deserializeObject(b);
		if (Boolean.parseBoolean(result)) {
			pars.clear();
			pars.put("newUserId", userId);
			pars.put("newCategory", STUDENT_EDU_CATEGORY);
			pars.put("newType", STUDENT_EDU_TYPE);
			pars.put("newTitle", STUDENT_EDU_TITLE);
			pars.put("newSubtitle", "");
			pars.put("newContent", "");
			domainClient.invokeDomainOperation("createUserProducedData",
					"smartcampus.services.esse3.UserProducedDataFactory",
					"smartcampus.services.esse3.UserProducedDataFactory.0",
					pars, userId, "vas_portfolio_subscriber");
			return true;
		}
		return false;
	}

	public String getStudentInfo(String userId) throws InvocationException {
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("userId", userId);
		List<String> result = domainClient.searchDomainObjects(
				"smartcampus.services.esse3.StudentInfo", pars,
				"vas_portfolio_subscriber");
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return "";
		}
	}

	public String getStudentExams(String userId) throws InvocationException {
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("userId", userId);
		List<String> result = domainClient.searchDomainObjects(
				"smartcampus.services.esse3.StudentExams", pars,
				"vas_portfolio_subscriber");
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return "";
		}
	}

	public List<Portfolio> getStudentPortfolios(String userId)
			throws InvocationException {
		// Map<String, Object> pars = new TreeMap<String, Object>();
		// pars.put("userId", userId);
		// pars.put("deleted", false);
		// List<String> res = domainClient.searchDomainObjects(
		// "smartcampus.services.esse3.Portfolio", pars,
		// "vas_portfolio_subscriber");
		//
		// return res;

		Map<String, Object> crit = new HashMap<String, Object>();
		crit.put("userId", userId);
		List<Portfolio> result;
		try {
			result = porfolioStorage.searchObjects(Portfolio.class, crit);
			return result;
			// return PortfolioUtils.toJSONList(result, Portfolio.class);
		} catch (DataException e) {
			logger.error(String.format(
					"Exception %s getting portfolio of user %s (msg: %s)", e
							.getClass().getSimpleName(), userId, e.getMessage()));
			return null;
		}
	}

	public List<Portfolio> getStudentPortfolio(String userId, String name)
			throws InvocationException {
		// Map<String, Object> pars = new TreeMap<String, Object>();
		// pars.put("userId", userId);
		// pars.put("name", name);
		// List<String> res = domainClient.searchDomainObjects(
		// "smartcampus.services.esse3.Portfolio", pars,
		// "vas_portfolio_subscriber");
		//
		// return res;

		Map<String, Object> crit = new HashMap<String, Object>();
		crit.put("name", name);
		crit.put("userId", userId);
		List<Portfolio> result;
		try {
			result = porfolioStorage.searchObjects(Portfolio.class, crit);
			return result;
			// return PortfolioUtils.toJSONList(result, Portfolio.class);
		} catch (DataException e) {
			logger.error(String.format(
					"Exception %s getting portfolio named %s (msg: %s)", e
							.getClass().getSimpleName(), name, e.getMessage()));
			return null;
		}
	}

	public String getStudentPortfolio(String portfolioId)
			throws InvocationException {
		// return domainClient.searchDomainObject(
		// "smartcampus.services.esse3.Portfolio", portfolioId,
		// "vas_portfolio_subscriber");
		Map<String, Object> crit = new HashMap<String, Object>();
		crit.put("id", portfolioId);
		List<Portfolio> result;
		try {
			result = porfolioStorage.searchObjects(Portfolio.class, crit);
			return result != null ? PortfolioUtils.toJSON(result.get(0)) : null;
		} catch (DataException e) {
			logger.error(String.format(
					"Exception %s getting portfolio id %s (msg: %s)", e
							.getClass().getSimpleName(), portfolioId, e
							.getMessage()));
			return null;
		}
	}

	public String getPortfolioByEntityId(String id) throws InvocationException {
		// Map<String, Object> pars = new TreeMap<String, Object>();
		// pars.put("entityId", id);
		// List<String> res = domainClient.searchDomainObjects(
		// "smartcampus.services.esse3.Portfolio", pars,
		// "vas_portfolio_subscriber");
		// if (res != null && !res.isEmpty())
		// return res.get(0);
		// return null;
		Map<String, Object> crit = new HashMap<String, Object>();
		crit.put("entityId", id);
		List<Portfolio> result;
		try {
			result = porfolioStorage.searchObjects(Portfolio.class, crit);
			return result != null ? PortfolioUtils.toJSON(result.get(0)) : null;
		} catch (DataException e) {
			logger.error(String.format(
					"Exception %s getting portfolio entityId %s (msg: %s)", e
							.getClass().getSimpleName(), id, e.getMessage()));
			return null;
		}
	}

	// public String createPortfolio(Map<String, Object> json, String userId,
	// long socialUserId)
	// throws InvocationException {
	// if (json != null) {
	// Map<String, Object> pars = new TreeMap<String, Object>();
	// pars.put("newUserId", userId);
	// pars.put("newName", json.get("name"));
	// pars.put("newUserSocialId", socialUserId);
	//
	// if (!pars.containsValue(null)) {
	// domainClient.invokeDomainOperation("createPortfolio",
	// "smartcampus.services.esse3.PortfolioFactory",
	// "smartcampus.services.esse3.PortfolioFactory.0", pars, userId,
	// "vas_portfolio_subscriber");
	// }
	// pars.clear();
	// pars.put("userId", userId);
	// pars.put("name", json.get("name"));
	// List<String> res =
	// domainClient.searchDomainObjects("smartcampus.services.esse3.Portfolio",
	// pars,
	// "vas_portfolio_subscriber");
	// if (res != null && !res.isEmpty()) {
	// return res.get(0);
	// }
	// }
	// return null;
	// }

	public String createPortfolio(Portfolio portfolio, PortfolioUser user,
			String userId, long socialUserId) throws Exception {
		if (portfolio != null && !portfolio.getName().isEmpty()) {
			Map<String, Object> pars = new TreeMap<String, Object>();
			pars.put("newUserId", userId);
			pars.put("newName", portfolio.getName());
			pars.put("newUserSocialId", socialUserId);

			boolean nameAlreadyExists = getStudentPortfolio(userId,
					portfolio.getName()).size() > 0 ? true : false;

			if (!nameAlreadyExists && !pars.containsValue(null)) {

				if (StringUtils.isBlank(portfolio.getId())) {
					portfolio.setId(new ObjectId().toString());
				}
				portfolio.setUser(userId);
				portfolio.setUserId(userId);
				String entityId = socialEngine.createEntity(user, portfolio)
						.getUri();
				logger.info(String.format(
						"Created social entity for portfolio %s",
						portfolio.getId()));
				portfolio.setEntityId(entityId);

				porfolioStorage.storeObject(portfolio);
				logger.info(String.format("Stored portfolio %s",
						portfolio.getId()));

				Map<String, Object> crit = new HashMap<String, Object>();
				crit.put("name", portfolio.getName());
				List<Portfolio> result = porfolioStorage.searchObjects(
						Portfolio.class, crit);
				return result != null ? PortfolioUtils.toJSON(result.get(0))
						: null;
				// domainClient.invokeDomainOperation("createPortfolio",
				// "smartcampus.services.esse3.PortfolioFactory",
				// "smartcampus.services.esse3.PortfolioFactory.0", pars,
				// userId, "vas_portfolio_subscriber");

				// List<String> res = getStudentPortfolio(userId,
				// portfolio.getName());
				// if (res != null && !res.isEmpty()) {
				// // create social entity for portfolio
				// DomainObject obj;
				// obj = new DomainObject(res.get(0));
				// Portfolio result = PortfolioUtils.convert(obj.getContent(),
				// Portfolio.class);
				// Portfolio param = (Portfolio) BeanUtils
				// .cloneBean(portfolio);
				// param.setId(obj.getId());
				// String entityId = socialEngine.createEntity(user, param)
				// .getUri();
				// result.setEntityId(entityId);
				// result.setId(obj.getId());
				// updatePortfolio(result, obj.getId(), userId);
				//
				// // set entityId in portfolio JSON for quick response
				// String response = res.get(0).replace("\"entityId\":1",
				// "\"entityId:\"" + "\"" + entityId + "\"");
				// logger.info(String.format(
				// "Created social entity for portfolio %s",
				// portfolio.getId()));
				// return response;
				// }
			}
		}
		return null;
	}

	// public void updatePortfolio(Map<String, Object> json, String portfolioId,
	// String userId) throws Exception {
	// if (json != null) {
	// String s =
	// domainClient.searchDomainObject("smartcampus.services.esse3.Portfolio",
	// portfolioId,
	// "vas_portfolio_subscriber");
	// if (s != null) {
	// DomainObject o = new DomainObject(s);
	// if (!userId.equals(o.getContent().get("userId"))) {
	// throw new SecurityException("Incorrect user");
	// }
	// }
	//
	// Map<String, Object> pars = new TreeMap<String, Object>();
	// pars.put("newName", json.get("name"));
	//
	// // IMPORTANT: these arrays CANNOT BE NULL
	// ArrayList<String> showPart = (ArrayList<String>)
	// json.get("showUserGeneratedData");
	//
	// Object[] showPartArray = showPart.toArray();
	// pars.put("newShowUserGeneratedData", showPartArray);
	//
	// showPart = (ArrayList<String>) json.get("highlightUserGeneratedData");
	// showPartArray = showPart.toArray();
	// pars.put("newHighlightUserGeneratedData", showPartArray);
	//
	// showPart = (ArrayList<String>) json.get("showStudentInfo");
	// showPartArray = showPart.toArray();
	// pars.put("newShowStudentInfo", showPartArray);
	//
	// if (json.get("tags") != null) {
	// pars.put("newTags", json.get("tags"));
	// }
	//
	// if (!pars.containsValue(null)) {
	// domainClient.invokeDomainOperation("updatePortfolio",
	// "smartcampus.services.esse3.Portfolio",
	// portfolioId, pars, portfolioId, "vas_portfolio_subscriber");
	// }
	// }
	// }

	@SuppressWarnings("unchecked")
	public void updatePortfolio(Portfolio portfolio, String portfolioId,
			String userId) throws Exception {
		if (portfolio != null) {
			String s = domainClient.searchDomainObject(
					"smartcampus.services.esse3.Portfolio", portfolioId,
					"vas_portfolio_subscriber");
			if (s != null) {
				DomainObject o = new DomainObject(s);
				if (!userId.equals(o.getContent().get("userId"))) {
					throw new SecurityException("Incorrect user");
				}
			}

			Map<String, Object> pars = new TreeMap<String, Object>();
			pars.put("newName", portfolio.getName());

			// IMPORTANT: these arrays CANNOT BE NULL
			ArrayList<String> showPart = (ArrayList<String>) portfolio
					.getShowUserGeneratedData();

			Object[] showPartArray = showPart.toArray();
			pars.put("newShowUserGeneratedData", showPartArray);

			showPart = (ArrayList<String>) portfolio
					.getHighlightUserGeneratedData();
			showPartArray = showPart.toArray();
			pars.put("newHighlightUserGeneratedData", showPartArray);

			showPart = (ArrayList<String>) portfolio.getShowStudentInfo();
			showPartArray = showPart.toArray();
			pars.put("newShowStudentInfo", showPartArray);

			if (portfolio.getTags() != null) {
				List<Map<String, Object>> tagMapList = new ArrayList<Map<String, Object>>();
				for (Concept c : portfolio.getTags()) {
					tagMapList.add(mapper.convertValue(c, Map.class));
				}
				pars.put("newTags", tagMapList);
			}

			if (!pars.containsValue(null)) {
				domainClient.invokeDomainOperation("updatePortfolio",
						"smartcampus.services.esse3.Portfolio", portfolioId,
						pars, portfolioId, "vas_portfolio_subscriber");
			}
		}
	}

	public void deletePortfolio(String portfolioId, PortfolioUser user)
			throws Exception {
		// Map<String, Object> pars = new TreeMap<String, Object>();
		// domainClient.invokeDomainOperation("deletePortfolio",
		// "smartcampus.services.esse3.Portfolio", id, pars, id,
		// "vas_portfolio_subscriber");

		porfolioStorage.deleteObjectById(portfolioId);
		// String s = domainClient.searchDomainObject(
		// "smartcampus.services.esse3.Portfolio", portfolioId,
		// "vas_portfolio_subscriber");
		// if (s != null) {
		// DomainObject o = new DomainObject(s);
		// if (!user.getUserId().equals(o.getContent().get("userId"))) {
		// throw new SecurityException("Incorrect user");
		// }
		// domainClient.invokeDomainOperation("deletePortfolio",
		// "smartcampus.services.esse3.Portfolio", portfolioId, pars,
		// portfolioId, "vas_portfolio_subscriber");
		//
		// // delete social entity
		// socialEngine.deleteEntity(user, portfolioId);
		logger.info(String.format("Deleted social entity for portfolio %s",
				portfolioId));
		// }
	}

	public List<String> getUserProducedData(String userId, String category)
			throws InvocationException {
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("userId", userId);
		if (category != null) {
			pars.put("category", category);
		}
		List<String> res = domainClient.searchDomainObjects(
				"smartcampus.services.esse3.UserProducedData", pars,
				"vas_portfolio_subscriber");
		return res;
	}

	public String getUserUNITNData(String userId) throws InvocationException {
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("userId", userId);
		pars.put("category", STUDENT_EDU_CATEGORY);
		pars.put("type", STUDENT_EDU_TYPE);

		List<String> res = domainClient.searchDomainObjects(
				"smartcampus.services.esse3.UserProducedData", pars,
				"vas_portfolio_subscriber");
		if (res != null && !res.isEmpty())
			return res.get(0);
		return null;
	}

	// public void createUserProducedData(Map<String, Object> json, String
	// userId) throws InvocationException {
	// if (json != null) {
	// Map<String, Object> pars = new TreeMap<String, Object>();
	// pars.put("newUserId", userId);
	// pars.put("newCategory", json.get("category"));
	// pars.put("newType", json.get("type"));
	// pars.put("newTitle", json.get("title"));
	// pars.put("newSubtitle", json.get("subtitle"));
	// pars.put("newContent", json.get("content"));
	// if (!pars.containsValue(null)) {
	// domainClient.invokeDomainOperation("createUserProducedData",
	// "smartcampus.services.esse3.UserProducedDataFactory",
	// "smartcampus.services.esse3.UserProducedDataFactory.0", pars, userId,
	// "vas_portfolio_subscriber");
	// }
	// }
	// }

	public void createUserProducedData(UserProducedData upd, String userId)
			throws InvocationException {
		if (upd != null) {
			Map<String, Object> pars = new TreeMap<String, Object>();
			pars.put("newUserId", userId);
			pars.put("newCategory", upd.getCategory());
			pars.put("newType", upd.getType());
			pars.put("newTitle", upd.getTitle());
			pars.put("newSubtitle", upd.getSubtitle());
			pars.put("newContent", upd.getContent());
			if (!pars.containsValue(null)
					&& (!upd.getTitle().isEmpty()
							|| !upd.getSubtitle().isEmpty() || !upd
							.getContent().isEmpty())) {
				domainClient.invokeDomainOperation("createUserProducedData",
						"smartcampus.services.esse3.UserProducedDataFactory",
						"smartcampus.services.esse3.UserProducedDataFactory.0",
						pars, userId, "vas_portfolio_subscriber");
			}
		}
	}

	public void updateUserProducedData(UserProducedData upd, String userDataId,
			String userId) throws Exception {
		if (upd != null) {
			String s = domainClient.searchDomainObject(
					"smartcampus.services.esse3.UserProducedData", userDataId,
					"vas_portfolio_subscriber");
			if (s != null) {
				DomainObject o = new DomainObject(s);
				if (!userId.equals(o.getContent().get("userId"))) {
					throw new SecurityException("Incorrect user");
				}
			}

			Map<String, Object> pars = new TreeMap<String, Object>();
			pars.put("newType", upd.getType());
			pars.put("newTitle", upd.getTitle());
			pars.put("newSubtitle", upd.getSubtitle());
			pars.put("newContent", upd.getContent());
			if (!pars.containsValue(null)
					&& (!upd.getTitle().isEmpty()
							|| !upd.getSubtitle().isEmpty() || !upd
							.getContent().isEmpty())) {
				domainClient.invokeDomainOperation("updateUserProducedData",
						"smartcampus.services.esse3.UserProducedData",
						userDataId, pars, userDataId,
						"vas_portfolio_subscriber");
			}
		}
	}

	public void deleteUserProducedData(String userDataId, String userId)
			throws Exception {
		String s = domainClient.searchDomainObject(
				"smartcampus.services.esse3.UserProducedData", userDataId,
				"vas_portfolio_subscriber");
		if (s != null) {
			DomainObject o = new DomainObject(s);
			if (!userId.equals(o.getContent().get("userId"))) {
				throw new SecurityException("Incorrect user");
			}
		}

		Map<String, Object> pars = new TreeMap<String, Object>();
		domainClient.invokeDomainOperation("deleteUserProducedData",
				"smartcampus.services.esse3.UserProducedData", userDataId,
				pars, userDataId, "vas_portfolio_subscriber");
	}

	public String getDomainObject(String type, String id)
			throws InvocationException {
		return domainClient.searchDomainObject(type, id,
				"vas_portfolio_subscriber");
	}

	public String getUserProducedData(String id) throws InvocationException {
		return domainClient.searchDomainObject(
				"smartcampus.services.esse3.UserProducedData", id,
				"vas_portfolio_subscriber");
	}

	public List<String> getAllUserProducedData(String userId)
			throws InvocationException {
		Map<String, Object> pars = new TreeMap<String, Object>();
		pars.put("userId", userId);
		return domainClient.searchDomainObjects(
				"smartcampus.services.esse3.UserProducedData", pars,
				"vas_portfolio_subscriber");
	}
}
