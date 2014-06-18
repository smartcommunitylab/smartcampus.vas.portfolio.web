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
package eu.trentorise.smartcampus.portfolio.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.portfolio.manager.Aggregator;
import eu.trentorise.smartcampus.portfolio.manager.ReportManager;
import eu.trentorise.smartcampus.portfolio.manager.ReportManager.REPORT_OUTPUT;

@Controller
public class ReportController extends SCController {

	@Autowired
	private ReportManager reportManager;

	@Autowired
	private Aggregator aggregatorData;

	private Logger logger = Logger.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET, value = "/rest/generatecv/{portfolioId}/{outputFormat}/{base64}")
	public @ResponseBody
	byte[] generateReport(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("portfolioId") String portfolioId,
			@PathVariable("outputFormat") String outputFormat,
			@PathVariable("base64") boolean base64) throws Exception {

		byte[] reportContent;
		try {
			reportContent = reportManager.produceReportCv("europass.jasper",
					REPORT_OUTPUT.PDF, aggregatorData.getEuropass(
							getBasicProfile(request), portfolioId), request
							.getSession().getServletContext().getRealPath("/")
							+ "img/report");
			logger.info(String.format("Created report %s for portfolio %s",
					outputFormat, portfolioId));
		} catch (Exception e) {
			logger.error(String.format("Exception %s, msg: %s", e.getClass()
					.getSimpleName(), e.getMessage()));
			throw e;
		}

		return (base64) ? Base64.encodeBase64(reportContent) : reportContent;
	}
}
