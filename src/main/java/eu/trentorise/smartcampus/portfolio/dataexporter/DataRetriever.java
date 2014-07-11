package eu.trentorise.smartcampus.portfolio.dataexporter;

import it.sayservice.platform.client.InvocationException;
import it.sayservice.platform.client.jms.JMSDomainEngineClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.trentorise.smartcampus.portfolio.models.Portfolio;
import eu.trentorise.smartcampus.portfolio.processor.EventProcessorImpl;
import eu.trentorise.smartcampus.presentation.common.exception.DataException;
import eu.trentorise.smartcampus.presentation.common.exception.NotFoundException;
import eu.trentorise.smartcampus.presentation.storage.sync.mongo.BasicObjectSyncMongoStorage;

/**
 * Utility class to extract existing Portfolio and UserProducedData from
 * DomainObject to mongodb
 * 
 * @author mirko perillo
 * 
 */
public class DataRetriever {

	private static final String BROKER_URL = "tcp://localhost:61616";
	private static final String BROKER_CLIENT_ID = "vas_portfolio_subscriber";

	private static final String MONGO_HOST = "localhost";
	private static final int MONGO_PORT = 27017;
	private static final String MONGO_NAME = "portfolio-dev";

	private static final String DB_URL = "jdbc:mysql://localhost/acprovider";
	private static final String DB_USER = "ac";
	private static final String DB_PWD = "ac";

	private static final boolean PRINT_PORTFOLIO = false;
	private static final boolean OVERWRITE_IF_EXIST = false;

	public static void main(String[] args) {
		MongoOperations mongo = null;
		try {
			mongo = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(
					MONGO_HOST, MONGO_PORT), MONGO_NAME));
		} catch (UnknownHostException e) {
			exitMsg("Exception connecting to mongo db " + MONGO_NAME + ": "
					+ e.getMessage());
		} catch (MongoException e) {
			exitMsg("Exception connecting to mongo db " + MONGO_NAME + ": "
					+ e.getMessage());
		}
		BasicObjectSyncMongoStorage storage = new BasicObjectSyncMongoStorage(
				mongo);

		EventProcessorImpl eventProcessor = new EventProcessorImpl();
		eventProcessor.setStorage(storage);
		JMSDomainEngineClient domainClient = null;
		try {
			domainClient = new JMSDomainEngineClient(
					new ActiveMQConnectionFactory(BROKER_URL));
			domainClient.setClientId(BROKER_CLIENT_ID);
			domainClient.setDomainUpdateListener(eventProcessor);
		} catch (InvocationException e) {
			exitMsg("Exception connecting to domainClient " + e.getMessage());
		}

		List<Long> userIds = new ArrayList<Long>();
		int totalPortfolio = 0;
		int convertedPortfolio = 0;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// retrieve all system uses
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER,
					DB_PWD);
			System.out.println("Connected to db " + DB_URL);
			java.sql.Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id FROM user");
			while (rs.next()) {
				userIds.add(rs.getLong("id"));
			}
		} catch (InstantiationException e) {
			exitMsg("Exception connecting ac db " + e.getMessage());
		} catch (IllegalAccessException e) {
			exitMsg("Exception connecting ac db " + e.getMessage());
		} catch (ClassNotFoundException e) {
			exitMsg("Exception connecting ac db " + e.getMessage());
		} catch (SQLException e) {
			exitMsg("Exception connecting ac db " + e.getMessage());
		}
		System.out.println(String.format("Founded %s users", userIds.size()));
		if (userIds.size() == 0) {
			System.exit(0);
		} else {
			for (Long uid : userIds) {
				// retrieve all portfolio of a user
				Map<String, Object> pars = new TreeMap<String, Object>();
				pars.put("userId", uid.toString());
				pars.put("deleted", false);
				try {
					List<String> res = domainClient.searchDomainObjects(
							"smartcampus.services.esse3.Portfolio", pars,
							"vas_portfolio_subscriber");
					totalPortfolio += res.size();

					for (String p : res) {
						JSONObject jsonObj;
						try {
							jsonObj = new JSONObject(p);
							String portfolioContent = jsonObj
									.getString("content");

							Portfolio portfolio = mapper.readValue(
									portfolioContent, Portfolio.class);
							portfolio.setId(jsonObj.getString("id"));

							try {
								if (!OVERWRITE_IF_EXIST
										&& storage.getObjectById(portfolio
												.getId()) != null) {
									System.out
											.println(String
													.format("Portfolio %s already stored in db",
															portfolio.getId()));
								} else {
									try {
										storage.storeObject(portfolio);
										System.out
												.println(String
														.format("Stored on mongo portfolio %s of user %s",
																portfolio
																		.getId(),
																uid));
										convertedPortfolio++;
									} catch (DataException e) {
										System.out
												.println(String
														.format("Exception storing in mongo portfolio %s",
																portfolio
																		.getId()));
									}
								}
							} catch (NotFoundException e) {
								System.out
										.println(String
												.format("Exception searching in mongo portfolio %s",
														portfolio.getId()));
								continue;
							} catch (DataException e) {
								System.out
										.println(String
												.format("Exception searching in mongo portfolio %s",
														portfolio.getId()));
								continue;
							}
						} catch (JSONException e) {
							exitMsg(String
									.format("Exception extracting portfolio content: %s (%s)",
											p, e.getMessage()));
						} catch (JsonParseException e) {
							exitMsg(String
									.format("Exception extracting portfolio content: %s (%s)",
											p, e.getMessage()));
						} catch (JsonMappingException e) {
							exitMsg(String
									.format("Exception extracting portfolio content: %s (%s)",
											p, e.getMessage()));
						} catch (IOException e) {
							exitMsg(String
									.format("Exception extracting portfolio content: %s (%s)",
											p, e.getMessage()));
						}
					}
					if (PRINT_PORTFOLIO) {
						for (String r : res) {
							System.out.println(r);
						}
					}
				} catch (InvocationException e) {
					exitMsg("Exception searching domain object portfolios of user "
							+ uid + " " + e.getMessage());
				}
			}
		}
		System.out.println(String.format("Founded %s portfolio, converted %s",
				totalPortfolio, convertedPortfolio));
		System.out.println("DONE!");
		System.exit(0);
	}

	private static void exitMsg(String msg) {
		System.err.println(msg);
		System.exit(-1);
	}
}
