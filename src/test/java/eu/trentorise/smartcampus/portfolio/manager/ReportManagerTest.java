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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;

import junitx.framework.FileAssert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.trentorise.smartcampus.portfolio.data.cv.Europass;
import eu.trentorise.smartcampus.portfolio.data.cv.Language;
import eu.trentorise.smartcampus.portfolio.data.cv.Training;
import eu.trentorise.smartcampus.portfolio.data.cv.WorkExperience;
import eu.trentorise.smartcampus.portfolio.manager.ReportManager.REPORT_OUTPUT;

public class ReportManagerTest {

	private static ReportManager reportManager;

	@BeforeClass
	public static void setup() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"spring/applicationContext.xml");
		reportManager = ctx.getBean(ReportManager.class);
	}

	@Test
	public void reportGeneration() throws MalformedURLException, IOException,
			ReportException {
		byte[] report = reportManager.produceReportCv("europass.jasper",
				REPORT_OUTPUT.PDF, createEuropass(), "imgs/");

		File temp = File.createTempFile("testportfolio", "");
		temp.deleteOnExit();

		FileUtils.writeByteArrayToFile(temp, report);

		byte[] reportOk = IOUtils.toByteArray(getClass().getClassLoader()
				.getResourceAsStream("test_cv.pdf"));

		File tempOk = File.createTempFile("testportfolio", "");
		FileUtils.writeByteArrayToFile(tempOk, reportOk);
		FileAssert.assertBinaryEquals(tempOk, temp);

		temp.delete();
		tempOk.delete();
	}

	private Europass createEuropass() throws MalformedURLException, IOException {
		Europass sample = new Europass();

		sample.setFullname("SC User");
		sample.setAddress("Via Sommarive 18");

		sample.setTelephone("000-0000000");
		sample.setEmail("scuser@trentorise.eu");
		sample.setNationality("italian");
		sample.setBirthDate("01/01/2010");
		sample.setGender("female");

		sample.setPicture(ImageIO
				.read(new URL(
						"http://tux.crystalxp.net/png/darky-knight-fullmetux-alchemist-ed-6554.png")));

		sample.setWorkExperience(Arrays.asList(new WorkExperience(
				"From September 2012", "Developer",
				"Analisys and Implementation", "mario rossi", "Reasearch"),
				new WorkExperience("From September 2012", "Developer",
						"Analisys and Implementation", "mario rossi",
						"Reasearch"), new WorkExperience("From September 2012",
						"Developer", "Analisys and Implementation",
						"mario rossi", "Reasearch"), new WorkExperience(
						"From September 2012", "Developer",
						"Analisys and Implementation", "mario rossi",
						"Reasearch"), new WorkExperience("From September 2012",
						"Developer", "Analisys and Implementation",
						"mario rossi", "Reasearch"), new WorkExperience(
						"From September 2012", "Developer",
						"Analisys and Implementation", "mario rossi",
						"Reasearch"), new WorkExperience("From September 2012",
						"Developer", "Analisys and Implementation",
						"mario rossi", "Reasearch"), new WorkExperience(
						"From September 2012", "Developer",
						"Analisys and Implementation", "mario rossi",
						"Reasearch")));

		sample.setTraining(Arrays
				.asList(new Training(
						"From February 2012",
						"developer",
						"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
						null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null),
						new Training(
								"From February 2012",
								"developer",
								"Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatti con la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga",
								null, null)));

		sample.setLanguages(Arrays.asList(new Language("english", null, "good",
				null, "sufficient", null, null, null, null, null, "good"),
				new Language("german", null, "good", null, "sufficient", null,
						null, null, null, null, null), new Language("spanish",
						null, "good", null, "sufficient", null, null, null,
						null, null, null)));

		sample.setMotherTongue("italian");

		sample.setOtherSkills("Assistenza presso gli uffici amministrativi della società, alla contabilità di cassa e gestione dei contatticon la rete di vendita, gestione delle scadenze fiscali periodiche e delle buste paga");
		sample.setSocialSkills("In behaviourism, social skill is any skill facilitating interaction and communication with others. Social rules and relations are created, communicated, and changed in verbal and nonverbal ways. The process of learning such skills is called socialization. The rationale for this type of an approach to treatment is that people meet a variety of social problems and can reduce the stress and punishment from the encounter as well as increase their reinforcement by having the correct skills.");

		return sample;
	}
	
	public static void main(String[] args) {
		System.err.println(new Date(1354525800627L));
	}

}
