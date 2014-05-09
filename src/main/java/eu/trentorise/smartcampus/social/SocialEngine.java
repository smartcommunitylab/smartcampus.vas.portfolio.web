package eu.trentorise.smartcampus.social;

import eu.trentorise.smartcampus.portfolio.controller.rest.PortfolioUser;

public interface SocialEngine {

	/**
	 * checks if an entity is shared with a user
	 * 
	 * @param user
	 *            user
	 * @param entityId
	 *            id of the entity
	 * @return true if entity is shared with the user, false otherwise
	 * @throws WebApiException
	 *             exception thrown by social engine
	 */
	public boolean checkPermission(PortfolioUser user, String entityId);

}