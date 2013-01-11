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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.portfolio.data.cv.Europass;

@Component
public class ReportManager {

	private static final Logger logger = Logger.getLogger(ReportManager.class);

	public static enum REPORT_OUTPUT {
		PDF
	}

	public byte[] produceReportCv(String templateName, REPORT_OUTPUT output,
			Europass data, String imageBaseDirectory) throws ReportException {

		if (imageBaseDirectory != null) {
			if (!imageBaseDirectory.endsWith("/")
					&& !imageBaseDirectory.endsWith("\\")) {
				imageBaseDirectory = imageBaseDirectory.concat("/");
			}
		}
		byte[] report = null;
		InputStream templateStream = this.getClass().getClassLoader()
				.getResourceAsStream(templateName);
		switch (output) {
		case PDF:
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("imagesDir", imageBaseDirectory);
			params.put("portfolio", data);
			try {
				report = JasperRunManager.runReportToPdf(templateStream,
						params, new JREmptyDataSource());
			} catch (JRException e) {
				logger.error("Exception creating report in pdf format", e);
				throw new ReportException();
			}
			break;

		default:
			throw new IllegalArgumentException("output format not supported");
		}
		return report;
	}
}
