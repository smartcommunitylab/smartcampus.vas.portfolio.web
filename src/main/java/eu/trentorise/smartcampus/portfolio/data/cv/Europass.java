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

import java.awt.Image;
import java.util.List;

public class Europass {
	private String fullname;
	private String address;
	private String telephone;
	private String mobile;
	private String fax;
	private String email;
	private String nationality;
	private String birthDate;
	private String gender;
	private Image picture;

	private List<WorkExperience> workExperience;

	private List<Training> training;
	private String motherTongue;
	private List<Language> languages;

	private String socialSkills;
	private String organizationalSkills;
	private String technicalSkills;
	private String computerSkills;
	private String artisticSkills;
	private String otherSkills;
	private String drivingLicence;
	private String desiredEmployment;

	public Europass() {

	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Image getPicture() {
		return picture;
	}

	public void setPicture(Image picture) {
		this.picture = picture;
	}

	public List<WorkExperience> getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(List<WorkExperience> workExperience) {
		this.workExperience = workExperience;
	}

	public List<Training> getTraining() {
		return training;
	}

	public void setTraining(List<Training> training) {
		this.training = training;
	}

	public String getMotherTongue() {
		return motherTongue;
	}

	public void setMotherTongue(String motherTongue) {
		this.motherTongue = motherTongue;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	public String getSocialSkills() {
		return socialSkills;
	}

	public void setSocialSkills(String socialSkills) {
		this.socialSkills = socialSkills;
	}

	public String getOrganizationalSkills() {
		return organizationalSkills;
	}

	public void setOrganizationalSkills(String organizationalSkills) {
		this.organizationalSkills = organizationalSkills;
	}

	public String getTechnicalSkills() {
		return technicalSkills;
	}

	public void setTechnicalSkills(String technicalSkills) {
		this.technicalSkills = technicalSkills;
	}

	public String getComputerSkills() {
		return computerSkills;
	}

	public void setComputerSkills(String computerSkills) {
		this.computerSkills = computerSkills;
	}

	public String getArtisticSkills() {
		return artisticSkills;
	}

	public void setArtisticSkills(String artisticSkills) {
		this.artisticSkills = artisticSkills;
	}

	public String getOtherSkills() {
		return otherSkills;
	}

	public void setOtherSkills(String otherSkills) {
		this.otherSkills = otherSkills;
	}

	public String getDrivingLicence() {
		return drivingLicence;
	}

	public void setDrivingLicence(String drivingLicence) {
		this.drivingLicence = drivingLicence;
	}

	public String getDesiredEmployment() {
		return desiredEmployment;
	}

	public void setDesiredEmployment(String desiredEmployment) {
		this.desiredEmployment = desiredEmployment;
	}

}
