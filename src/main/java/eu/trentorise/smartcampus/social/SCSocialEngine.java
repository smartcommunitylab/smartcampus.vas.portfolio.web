package eu.trentorise.smartcampus.social;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.trentorise.smartcampus.portfolio.controller.rest.PortfolioUser;
import eu.trentorise.smartcampus.socialservice.SocialService;
import eu.trentorise.smartcampus.socialservice.SocialServiceException;

public class SCSocialEngine implements SocialEngine {

	private static final Logger logger = Logger.getLogger(SCSocialEngine.class);

	private static SocialService socialService;

	@Autowired
	@Value("${smartcampus.socialengine.endpoint}")
	private String socialEndpoint;

	public SocialService getSocialClient() {
		if (socialService == null) {
			socialService = new SocialService(socialEndpoint);
		}

		return socialService;
	}

	@Override
	public boolean checkPermission(PortfolioUser user, String entityId) {

		try {
			return getSocialClient().getEntitySharedWithUser(
					user.getUserToken(), entityId) != null;
		} catch (SecurityException e) {
			logger.error(String.format(
					"Security exception getting resource owner: %s",
					e.getMessage()));
		} catch (SocialServiceException e) {
			logger.error(String.format(
					"General exception getting resource owner: %s",
					e.getMessage()));
		}

		return false;
	}
}
