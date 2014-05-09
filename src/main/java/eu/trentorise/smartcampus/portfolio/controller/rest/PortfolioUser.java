package eu.trentorise.smartcampus.portfolio.controller.rest;

import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

public class PortfolioUser extends BasicProfile {

	private String clientToken;
	private String userToken;

	public PortfolioUser(BasicProfile profile) {
		this.setName(profile.getName());
		this.setSurname(profile.getSurname());
		this.setSocialId(profile.getSocialId());
		this.setUserId(profile.getUserId());
	}

	public String getClientToken() {
		return clientToken;
	}

	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

}
