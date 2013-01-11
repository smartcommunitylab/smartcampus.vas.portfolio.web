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

import java.util.ArrayList;
import java.util.List;

import eu.trentorise.smartcampus.presentation.data.BasicObject;

public class Portfolio extends BasicObject {

	private static final long serialVersionUID = -5222645739554096556L;
	
	private String userId;
	private long timestamp;
	private String name;
	private List<Concept> tags;
	private Long entityId;
	private List<String> showUserGeneratedData = new ArrayList<String>();
	private List<String> highlightUserGeneratedData = new ArrayList<String>();
	private List<String> showStudentInfo = new ArrayList<String>();
	private String userSocialId;
	private boolean deleted;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Concept> getTags() {
		return tags;
	}

	public void setTags(List<Concept> tags) {
		this.tags = tags;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public List<String> getShowUserGeneratedData() {
		return showUserGeneratedData;
	}

	public void setShowUserGeneratedData(List<String> showUserGeneratedData) {
		this.showUserGeneratedData = showUserGeneratedData;
	}

	public List<String> getHighlightUserGeneratedData() {
		return highlightUserGeneratedData;
	}

	public void setHighlightUserGeneratedData(List<String> highlightUserGeneratedData) {
		this.highlightUserGeneratedData = highlightUserGeneratedData;
	}

	public List<String> getShowStudentInfo() {
		return showStudentInfo;
	}

	public void setShowStudentInfo(List<String> showStudentInfo) {
		this.showStudentInfo = showStudentInfo;
	}

	public String getUserSocialId() {
		return userSocialId;
	}

	public void setUserSocialId(String userSocialId) {
		this.userSocialId = userSocialId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
