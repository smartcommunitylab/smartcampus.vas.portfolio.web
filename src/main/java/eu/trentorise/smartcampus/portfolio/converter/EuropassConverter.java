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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.trentorise.smartcampus.portfolio.data.CurriculumEntry;
import eu.trentorise.smartcampus.portfolio.data.LanguageData;
import eu.trentorise.smartcampus.portfolio.data.cv.Language;
import eu.trentorise.smartcampus.portfolio.data.cv.Training;
import eu.trentorise.smartcampus.portfolio.data.cv.WorkExperience;

public class EuropassConverter {

	private static final List<String> europassLangLevels = Arrays.asList("A1",
			"A2", "B1", "B2", "C1", "C2");

	public static List<Training> toTraining(List<CurriculumEntry> entres) {
		List<Training> trainings = new ArrayList<Training>();
		for (CurriculumEntry entry : entres) {
			trainings.add(toTraining(entry));
		}
		return trainings;
	}

	public static Training toTraining(CurriculumEntry entry) {
		Training training = new Training();
		training.setDate(entry.getTimePeriod());
		training.setSkillCovered(entry.getDescription());
		training.setOrganizationInfo(entry.getName());

		return training;
	}

	public static List<WorkExperience> toWorkExperience(
			List<CurriculumEntry> entres) {
		List<WorkExperience> works = new ArrayList<WorkExperience>();
		for (CurriculumEntry entry : entres) {
			works.add(toWorkExperience(entry));
		}
		return works;
	}

	public static WorkExperience toWorkExperience(CurriculumEntry entry) {
		WorkExperience we = new WorkExperience();
		we.setDate(entry.getTimePeriod());
		we.setActivities(entry.getDescription());
		we.setEmployerInfo(entry.getName());
		return we;
	}

	public static List<Language> toLanguage(List<LanguageData> ldList) {
		List<Language> languages = new ArrayList<Language>();
		for (LanguageData temp : ldList) {
			languages.add(toLanguage(temp));
		}

		return languages;
	}

	public static Language toLanguage(LanguageData ld) {
		Language lang = new Language();
		lang.setName(ld.getName());
		if (europassLangLevels.contains(ld.getLevel().toUpperCase())) {
			lang.setListeningLevel(ld.getLevel());
			lang.setReadingLevel(ld.getLevel());
			lang.setSpokenInteractionLevel(ld.getLevel());
			lang.setSpokenProductionLevel(ld.getLevel());
			lang.setWritingLevel(ld.getLevel());
		} else {
			lang.setListeningDescription(ld.getLevel());
			lang.setReadingDescription(ld.getLevel());
			lang.setSpokenInteractionDescr(ld.getLevel());
			lang.setSpokenProductionDescr(ld.getLevel());
			lang.setWritingDescription(ld.getLevel());
		}
		return lang;

	}
}
