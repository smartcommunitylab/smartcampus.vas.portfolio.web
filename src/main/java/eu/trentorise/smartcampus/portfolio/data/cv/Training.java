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

public class Training {
	private String date;
	private String qualificationTitle;
	private String skillCovered;
	private String organizationInfo;
	private String levelClassification;

	public Training() {
		super();
	}

	public Training(String date, String qualificationTitle,
			String skillCovered, String organizationInfo,
			String levelClassification) {
		super();
		this.date = date;
		this.qualificationTitle = qualificationTitle;
		this.skillCovered = skillCovered;
		this.organizationInfo = organizationInfo;
		this.levelClassification = levelClassification;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getQualificationTitle() {
		return qualificationTitle;
	}

	public void setQualificationTitle(String qualificationTitle) {
		this.qualificationTitle = qualificationTitle;
	}

	public String getSkillCovered() {
		return skillCovered;
	}

	public void setSkillCovered(String skillCovered) {
		this.skillCovered = skillCovered;
	}

	public String getOrganizationInfo() {
		return organizationInfo;
	}

	public void setOrganizationInfo(String organizationInfo) {
		this.organizationInfo = organizationInfo;
	}

	public String getLevelClassification() {
		return levelClassification;
	}

	public void setLevelClassification(String levelClassification) {
		this.levelClassification = levelClassification;
	}

}
