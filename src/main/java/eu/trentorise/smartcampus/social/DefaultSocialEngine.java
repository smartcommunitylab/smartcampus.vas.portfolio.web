package eu.trentorise.smartcampus.social;

import eu.trentorise.smartcampus.portfolio.controller.rest.PortfolioUser;
import eu.trentorise.smartcampus.portfolio.models.Portfolio;
import eu.trentorise.smartcampus.socialservice.beans.Entity;

public class DefaultSocialEngine implements SocialEngine {

	@Override
	public boolean checkPermission(PortfolioUser user, String entityId) {
		return true;
	}

	@Override
	public Entity createEntity(PortfolioUser user, Portfolio portfolio) {
		return null;
	}

	@Override
	public boolean deleteEntity(PortfolioUser user, String entityURI) {
		return true;
	}

}
