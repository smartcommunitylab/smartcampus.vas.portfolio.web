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

import it.sayservice.platform.client.InvocationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.portfolio.converter.EuropassConverter;
import eu.trentorise.smartcampus.portfolio.converter.JsonConverter;
import eu.trentorise.smartcampus.portfolio.data.ContactData;
import eu.trentorise.smartcampus.portfolio.data.CurriculumEntry;
import eu.trentorise.smartcampus.portfolio.data.LanguageData;
import eu.trentorise.smartcampus.portfolio.data.StudentInfoData;
import eu.trentorise.smartcampus.portfolio.data.cv.Europass;
import eu.trentorise.smartcampus.portfolio.data.cv.Language;
import eu.trentorise.smartcampus.portfolio.data.cv.Training;
import eu.trentorise.smartcampus.portfolio.data.cv.WorkExperience;
import eu.trentorise.smartcampus.portfolio.util.PortfolioUtils;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Component
public class Aggregator {

	private static final Logger logger = Logger.getLogger(Aggregator.class);
	@Autowired
	private PortfolioManager portfolioManager;

	public Europass getEuropass(BasicProfile basicProfile, String portfolioId)
			throws Exception {
		String userId = basicProfile.getUserId();
		Europass europass = new Europass();
		List<String> visibleItems = getVisibleItems(userId, portfolioId);
		europass = populateStudentInfo(userId, europass, visibleItems);
		europass = populateLanguages(userId, europass, visibleItems);
		europass = populateWorkExperiences(userId, europass, visibleItems);
		europass = populateTraining(userId, europass, visibleItems);
		europass = populateContacts(userId, europass, visibleItems);
		europass = populateProfileInfo(basicProfile, europass);
		return europass;
	}

	private Europass populateProfileInfo(BasicProfile basicProfile,
			Europass europass) throws Exception {
		String fullname = basicProfile.getName() + " "
				+ basicProfile.getSurname();
		europass.setFullname(fullname);
		return europass;
	}

	private List<String> getVisibleItems(String userId, String portfolioId)
			throws InvocationException, JSONException {
		List<String> items = new ArrayList<String>();
		// FIXME temp
		String json = ""; // SPortfolioUtils.listToJSON(portfolioManager.getStudentPortfolios(userId));
		JSONArray portfolios = new JSONArray(json);
		for (int i = 0; i < portfolios.length(); i++) {
			if (portfolios.getJSONObject(i).getString("id").equals(portfolioId)) {
				String content = portfolios.getJSONObject(i).getString(
						"content");
				JSONArray values = new JSONArray(new org.json.JSONObject(
						content).getString("showUserGeneratedData"));
				for (int j = 0; j < values.length(); j++) {
					items.add(values.getString(j));
				}
				values = new JSONArray(
						new org.json.JSONObject(content)
								.getString("showStudentInfo"));
				for (int j = 0; j < values.length(); j++) {
					items.add(values.getString(j));
				}
			}
		}
		return items;
	}

	private Europass populateContacts(String userId, Europass europass,
			List<String> visibleItems) throws InvocationException,
			JsonParseException, JsonMappingException, IOException,
			JSONException {
		String json = PortfolioUtils.listToJSON(portfolioManager
				.getUserProducedData(userId, "contact"));
		List<ContactData> contacts = JsonConverter.toClassList(json,
				ContactData.class);
		for (ContactData temp : contacts) {
			if (visibleItems.contains(temp.getId())) {
				if (temp.getName().equalsIgnoreCase("address")) {
					europass.setAddress(temp.getValue());
				} else if (temp.getName().equalsIgnoreCase("email")) {
					europass.setEmail(temp.getValue());
				} else if (temp.getName().equalsIgnoreCase("fax")) {
					europass.setFax(temp.getValue());
				} else if (temp.getName().equalsIgnoreCase("phone")
						|| temp.getName().equalsIgnoreCase("telefono")) {
					europass.setTelephone(temp.getValue());
				} else if (temp.getName().equalsIgnoreCase("mobile")) {
					europass.setMobile(temp.getValue());
				}
			}
		}
		return europass;
	}

	private Europass populateTraining(String userId, Europass europass,
			List<String> visibleItems) throws InvocationException,
			JsonParseException, JsonMappingException, IOException,
			JSONException {
		String json = PortfolioUtils.listToJSON(portfolioManager
				.getUserProducedData(userId, "education"));
		if (europass.getTraining() == null) {
			europass.setTraining(new ArrayList<Training>());
		}

		List<CurriculumEntry> entries = JsonConverter.toClassList(json,
				CurriculumEntry.class);

		for (CurriculumEntry entry : entries) {
			if (visibleItems.contains(entry.getId())) {
				europass.getTraining().add(EuropassConverter.toTraining(entry));
			}
		}
		return europass;
	}

	private Europass populateWorkExperiences(String userId, Europass europass,
			List<String> visibleItems) throws InvocationException,
			JsonParseException, JsonMappingException, IOException,
			JSONException {
		String json = PortfolioUtils.listToJSON(portfolioManager
				.getUserProducedData(userId, "professional"));
		if (europass.getWorkExperience() == null) {
			europass.setWorkExperience(new ArrayList<WorkExperience>());
		}

		List<CurriculumEntry> entries = JsonConverter.toClassList(json,
				CurriculumEntry.class);
		for (CurriculumEntry entry : entries) {
			if (visibleItems.contains(entry.getId())) {
				europass.getWorkExperience().add(
						EuropassConverter.toWorkExperience(entry));
			}
		}
		return europass;
	}

	private Europass populateLanguages(String userId, Europass europass,
			List<String> visibleItems) throws InvocationException,
			JsonParseException, JsonMappingException, IOException,
			JSONException {
		String json = PortfolioUtils.listToJSON(portfolioManager
				.getUserProducedData(userId, "language"));
		List<LanguageData> languages = JsonConverter.toClassList(json,
				LanguageData.class);

		if (europass.getLanguages() == null) {
			europass.setLanguages(new ArrayList<Language>());
		}
		for (LanguageData temp : languages) {
			if (visibleItems.contains(temp.getId())) {
				europass.getLanguages().add(EuropassConverter.toLanguage(temp));
			}
		}

		return europass;
	}

	private Europass populateStudentInfo(String userId, Europass europass,
			List<String> visibleItems) throws InvocationException {
		String json = portfolioManager.getStudentInfo(userId);
		if (json != null && !json.isEmpty()) {
			StudentInfoData student = JsonConverter.toClass(json,
					StudentInfoData.class, null);
			if (visibleItems.contains("address")) {
				europass.setAddress(student.getAddress());
			}
			if (visibleItems.contains("dateOfBirth")) {
				europass.setBirthDate(student.getDateOfBirth());
			}
			if (visibleItems.contains("gender")) {
				europass.setGender(student.getGender());
			}
			if (visibleItems.contains("mobile")) {
				europass.setMobile(student.getMobile());
			}
			if (visibleItems.contains("nation")) {
				europass.setNationality(student.getNation());
			}
			if (visibleItems.contains("phone")) {
				europass.setTelephone(student.getPhone());
			}
		}
		return europass;
	}

	private String capitalLetter(String original) {
		final StringBuilder result = new StringBuilder(original.length());
		String[] words = original.split("\\s");
		for (int i = 0, l = words.length; i < l; ++i) {
			if (i > 0)
				result.append(" ");
			result.append(Character.toUpperCase(words[i].charAt(0))).append(
					words[i].substring(1));
		}
		return result.toString();

	}

}
