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
package eu.trentorise.smartcampus.portfolio.data.cv;

public class Language {
	private String name;
	private String listeningLevel;
	private String listeningDescription;
	private String readingLevel;
	private String readingDescription;
	private String spokenInteractionLevel;
	private String spokenInteractionDescr;
	private String spokenProductionLevel;
	private String spokenProductionDescr;
	private String writingLevel;
	private String writingDescription;

	public Language() {
	}

	public Language(String name, String listeningLevel,
			String listeningDescription, String readingLevel,
			String readingDescription, String spokenInteractionLevel,
			String spokenInteractionDescr, String spokenProductionLevel,
			String spokenProductionDescr, String writingLevel,
			String writingDescription) {
		super();
		this.name = name;
		this.listeningLevel = listeningLevel;
		this.listeningDescription = listeningDescription;
		this.readingLevel = readingLevel;
		this.readingDescription = readingDescription;
		this.spokenInteractionLevel = spokenInteractionLevel;
		this.spokenInteractionDescr = spokenInteractionDescr;
		this.spokenProductionLevel = spokenProductionLevel;
		this.spokenProductionDescr = spokenProductionDescr;
		this.writingLevel = writingLevel;
		this.writingDescription = writingDescription;
	}

	public Language(String name, String listeningLevel, String readingLevel,
			String spokenInteractionLevel, String spokenProductionLevel,
			String writingLevel) {
		super();
		this.name = name;
		this.listeningLevel = listeningLevel;
		this.readingLevel = readingLevel;
		this.spokenInteractionLevel = spokenInteractionLevel;
		this.spokenProductionLevel = spokenProductionLevel;
		this.writingLevel = writingLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getListeningLevel() {
		return listeningLevel;
	}

	public void setListeningLevel(String listeningLevel) {
		this.listeningLevel = listeningLevel;
	}

	public String getReadingLevel() {
		return readingLevel;
	}

	public void setReadingLevel(String readingLevel) {
		this.readingLevel = readingLevel;
	}

	public String getSpokenInteractionLevel() {
		return spokenInteractionLevel;
	}

	public void setSpokenInteractionLevel(String spokenInteractionLevel) {
		this.spokenInteractionLevel = spokenInteractionLevel;
	}

	public String getSpokenProductionLevel() {
		return spokenProductionLevel;
	}

	public void setSpokenProductionLevel(String spokenProductionLevel) {
		this.spokenProductionLevel = spokenProductionLevel;
	}

	public String getWritingLevel() {
		return writingLevel;
	}

	public void setWritingLevel(String writingLevel) {
		this.writingLevel = writingLevel;
	}

	public String getListeningDescription() {
		return listeningDescription;
	}

	public void setListeningDescription(String listeningDescription) {
		this.listeningDescription = listeningDescription;
	}

	public String getReadingDescription() {
		return readingDescription;
	}

	public void setReadingDescription(String readingDescription) {
		this.readingDescription = readingDescription;
	}

	public String getSpokenInteractionDescr() {
		return spokenInteractionDescr;
	}

	public void setSpokenInteractionDescr(String spokenInteractionDescr) {
		this.spokenInteractionDescr = spokenInteractionDescr;
	}

	public String getSpokenProductionDescr() {
		return spokenProductionDescr;
	}

	public void setSpokenProductionDescr(String spokenProductionDescr) {
		this.spokenProductionDescr = spokenProductionDescr;
	}

	public String getWritingDescription() {
		return writingDescription;
	}

	public void setWritingDescription(String writingDescription) {
		this.writingDescription = writingDescription;
	}

}
