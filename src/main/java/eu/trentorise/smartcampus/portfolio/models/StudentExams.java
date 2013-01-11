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

import java.util.List;

import eu.trentorise.smartcampus.presentation.data.BasicObject;

public class StudentExams extends BasicObject {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String unitnId;
	private List<ExamData> examData;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUnitnId() {
		return unitnId;
	}

	public void setUnitnId(String unitnId) {
		this.unitnId = unitnId;
	}

	public List<ExamData> getExamData() {
		return examData;
	}

	public void setExamData(List<ExamData> examData) {
		this.examData = examData;
	}

}
