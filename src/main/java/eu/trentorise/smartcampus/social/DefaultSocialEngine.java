package eu.trentorise.smartcampus.social;

import eu.trentorise.smartcampus.portfolio.controller.rest.PortfolioUser;

public class DefaultSocialEngine implements SocialEngine {

	@Override
	public boolean checkPermission(PortfolioUser user, String entityId) {
		return true;
	}

}
