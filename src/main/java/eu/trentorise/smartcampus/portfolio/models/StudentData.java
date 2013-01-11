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
package eu.trentorise.smartcampus.portfolio.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentData {

	private String fiscalCode;
	private String name;
	private String surname;
	private String enrollmentYear;
	private String nation;
	private String academicYear;
	private String supplementaryYears;
	private String cfu;
	private String cfuTotal;
	private String marksNumber;
	private String marksAverage;
	private String gender;
	private String dateOfBirth;
	private String phone;
	private String mobile;
	private String address;
	private String cds;

	public void filterData(List<String> allowed) {
		Set<String> set = new HashSet<String>();
		if (allowed != null) {
			set.addAll(allowed);
		} 
		if (!set.contains("fiscalCode")) {
			fiscalCode = null;
		}
		if (!set.contains("enrollmentYear")) {
			enrollmentYear = null;
		}
		if (!set.contains("nation")) {
			nation = null;
		}
		if (!set.contains("academicYear")) {
			academicYear = null;
		}
		if (!set.contains("supplementaryYears")) {
			supplementaryYears = null;
		}
		if (!set.contains("cfu_cfuTotal")) {
			cfu = null;
			cfuTotal = null;
		}
		if (!set.contains("marksNumber")) {
			marksNumber = null;
		}
		if (!set.contains("marksAverage")) {
			marksAverage = null;
		}
		if (!set.contains("gender")) {
			gender = null;
		}
		if (!set.contains("dateOfBirth")) {
			dateOfBirth = null;
		}
		if (!set.contains("phone")) {
			phone = null;
		}
		if (!set.contains("mobile")) {
			mobile = null;
		}
		if (!set.contains("address")) {
			address = null;
		}
		if (!set.contains("cds")) {
			cds = null;
		}
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(String enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getSupplementaryYears() {
		return supplementaryYears;
	}

	public void setSupplementaryYears(String supplementaryYears) {
		this.supplementaryYears = supplementaryYears;
	}

	public String getCfu() {
		return cfu;
	}

	public void setCfu(String cfu) {
		this.cfu = cfu;
	}

	public String getCfuTotal() {
		return cfuTotal;
	}

	public void setCfuTotal(String cfuTotal) {
		this.cfuTotal = cfuTotal;
	}

	public String getMarksNumber() {
		return marksNumber;
	}

	public void setMarksNumber(String marksNumber) {
		this.marksNumber = marksNumber;
	}

	public String getMarksAverage() {
		return marksAverage;
	}

	public void setMarksAverage(String marksAverage) {
		this.marksAverage = marksAverage;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCds() {
		return cds;
	}

	public void setCds(String cds) {
		this.cds = cds;
	}

	
}
