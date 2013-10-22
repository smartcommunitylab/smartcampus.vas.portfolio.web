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
package eu.trentorise.smartcampus.portfolio.manager;

import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.trentorise.smartcampus.portfolio.data.cv.Europass;

public class AggregatorTest {

	private static Aggregator aggregator;

	/**
	 * Test data of a temporary user, it could be not present anymore
	 */
	private static final String userId = "20";
	private static final String portfolioId = "5059e1c3975ad2633379ab26";
	private static final String token = "63a3761c4e2c7de717fab95391da846ffad14a14903cd406d1573509653e63fd";
	private static final String serviceUri = "https://vas-dev.sc.trentorise.eu/smartcampus.vas.community-manager.web";

	@BeforeClass
	public static void setup() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		aggregator = ctx.getBean(Aggregator.class);
	}

	/**
	 * test is commented, because it uses temporary data not always present in
	 * the system
	 */

	// @Test
	public void getEuropass() throws Exception {
		// Europass europass = aggregator.getEuropass(userId, portfolioId,
		// token,
		// serviceUri);
	}
}
