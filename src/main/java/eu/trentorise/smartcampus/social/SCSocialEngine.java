package eu.trentorise.smartcampus.social;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.aac.AACService;
import eu.trentorise.smartcampus.aac.model.TokenData;
import eu.trentorise.smartcampus.portfolio.controller.rest.PortfolioUser;
import eu.trentorise.smartcampus.portfolio.models.Portfolio;
import eu.trentorise.smartcampus.socialservice.SocialService;
import eu.trentorise.smartcampus.socialservice.SocialServiceException;
import eu.trentorise.smartcampus.socialservice.beans.Entity;
import eu.trentorise.smartcampus.socialservice.beans.EntityType;

public class SCSocialEngine implements SocialEngine {

	private static final Logger logger = Logger.getLogger(SCSocialEngine.class);

	private static SocialService socialService;

	private static String portfolioTypeId = null;

	@Autowired
	@Value("${smartcampus.socialengine.endpoint}")
	private String socialEndpoint;

	@Autowired
	@Value("${smartcampus.appId}")
	private String appId;

	@Autowired
	@Value("${aacURL}")
	private String aacEndpoint;

	@Autowired
	@Value("${smartcampus.clientId}")
	private String scClientId;

	@Autowired
	@Value("${smartcampus.clientSecret}")
	private String scClientSecret;

	private static AACService aacService;

	private TokenData authToken;

	public SocialService getSocialClient() {
		if (socialService == null) {
			socialService = new SocialService(socialEndpoint);
		}

		return socialService;
	}

	public AACService getAACClient() {
		if (aacService == null) {
			aacService = new AACService(aacEndpoint, scClientId, scClientSecret);
		}

		return aacService;
	}

	private String getPortfolioTypeId() {
		if (portfolioTypeId == null) {
			EntityType type = new EntityType("portfolio", null);
			try {
				portfolioTypeId = getSocialClient().createEntityType(
						getAuthToken(), type).getId();
				logger.info("Loaded porfolio entity type");
			} catch (SecurityException e) {
				logger.error(String
						.format("Security exception getting portfolio entity type"));
			} catch (SocialServiceException e) {
				logger.error(String
						.format("General exception getting portfolio entity type"));
			} catch (AACException e) {
				logger.error(String
						.format("Authentication exception getting portfolio entity type"));
			}
		}

		return portfolioTypeId;
	}

	private String getAuthToken() throws AACException {
		if (authToken == null) {
			authToken = getAACClient().generateClientToken();
		} else {
			if (authToken.getExpires_on() < System.currentTimeMillis()) {
				logger.info("Client authToken expired, trying refresh....");
				authToken = getAACClient().refreshToken(
						authToken.getRefresh_token());
				logger.info("Client authToken refreshed!");
			}
		}

		return authToken.getAccess_token();
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

	@Override
	public Entity createEntity(PortfolioUser user, Portfolio portfolio) {
		Entity entity = new Entity();
		entity.setLocalId(portfolio.getId());
		entity.setName(portfolio.getName());
		entity.setOwner(user.getUserId());
		entity.setType(getPortfolioTypeId());
		try {
			return getSocialClient().createOrUpdateUserEntityByApp(
					getAuthToken(), appId, user.getUserId(), entity);
		} catch (Exception e) {
			logger.error(String.format(
					"Exception %s creating entity (msg: %s)", e.getClass()
							.getName(), e.getMessage()));
		}

		return null;
	}

	@Override
	public boolean deleteEntity(PortfolioUser user, String portfolioId) {
		try {
			return getSocialClient().deleteEntityByApp(getAuthToken(), appId,
					portfolioId);
		} catch (Exception e) {
			logger.error(String.format(
					"Exception %s creating entity (msg: %s)", e.getClass()
							.getName(), e.getMessage()));
		}

		return false;
	}
}
