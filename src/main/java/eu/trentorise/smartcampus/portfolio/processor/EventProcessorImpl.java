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
package eu.trentorise.smartcampus.portfolio.processor;

import it.sayservice.platform.client.DomainObject;
import it.sayservice.platform.client.DomainUpdateListener;
import it.sayservice.platform.core.message.Core.DomainEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.trentorise.smartcampus.portfolio.data.ExamData;
import eu.trentorise.smartcampus.presentation.common.exception.DataException;
import eu.trentorise.smartcampus.presentation.data.BasicObject;
import eu.trentorise.smartcampus.presentation.data.NotificationObject;
import eu.trentorise.smartcampus.presentation.storage.BasicObjectStorage;
import eu.trentorise.smartcampus.presentation.storage.NotificationStorage;

public class EventProcessorImpl implements DomainUpdateListener {

	@Autowired
	private BasicObjectStorage storage;

	@Autowired
	private NotificationStorage notificationStorage;

	private static final String STUDENT_EXAMS = "smartcampus.services.esse3.StudentExams";

	private static Log logger = LogFactory.getLog(EventProcessorImpl.class);

	@Override
	public void onDomainEvents(String subscriptionId, List<DomainEvent> events) {
//		System.out.println("SUBID: " + subscriptionId);
//		System.out.println("EVENTS: " + events.size() + " -> " + events);

		List<BasicObject> created = new ArrayList<BasicObject>();
		List<BasicObject> deleted = new ArrayList<BasicObject>();
		List<BasicObject> updated = new ArrayList<BasicObject>();
		List<NotificationObject> notifications = new ArrayList<NotificationObject>();

		for (DomainEvent e : events) {
			if (STUDENT_EXAMS.equals(e.getDoType())) {
				try {
					processStudentExamsEvent(e, created, updated, deleted, notifications);
				} catch (DataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	// TODO: update to not use fiscalCode
	private void processStudentExamsEvent(DomainEvent e, List<BasicObject> created, List<BasicObject> updated, List<BasicObject> deleted, List<NotificationObject> notifications) throws DataException {
		DomainObject obj = readDOFromEvent(e);

		if (obj == null) {
			return;
		}

		Map<String, Object> content = obj.getContent();
		String userId = (String) content.get("fiscalCode");
		Object examData = content.get("examData");

		if (examData != null) {
			// loop on exams
			ExamData exam = new ExamData();
			Map<String, Object> criteria = new HashMap<String, Object>();
			criteria.put("id", exam.getId());
			List<ExamData> storedExamList = storage.searchObjects(ExamData.class, criteria, userId);
			if (storedExamList == null || storedExamList.size() == 0) {
				exam.setUpdateTime(System.currentTimeMillis());
				exam.setUser(userId);

				created.add(exam);

				NotificationObject notification = createNotification(e, userId);
				notification.getContent().put("event", "ADDED_EXAM");
				String title = exam.getName();
				notification.getContent().put("text", String.format("Exam %s has been registered.", title));
				notification.getContent().put("title", "Exam registered.");
				notification.getContent().put("id", obj.getId());
				notification.getContent().put("type", obj.getType());
				notification.getContent().put("userId", userId);
				notifications.add(notification);

				notificationStorage.storeNotification(notification);

			}
			// end loop

			storage.storeAllObjects(created);
		}
	}

	private DomainObject readDOFromEvent(DomainEvent e) {
		try {
			return new DomainObject(e.getPayload());
		} catch (Exception e1) {
			logger.error("error reading event: " + e1.getMessage());
			return null;
		}
	}

	private NotificationObject createNotification(DomainEvent e, String user) {
		NotificationObject notification = new NotificationObject();
		notification.setId(e.getEventId());
		notification.setTimestamp(e.getTimestamp());
		notification.setType(e.getDoType());
		notification.setContent(new HashMap<String, Object>());
		notification.setUser(user);
		return notification;
	}

	public BasicObjectStorage getStorage() {
		return storage;
	}

	public void setStorage(BasicObjectStorage storage) {
		this.storage = storage;
	}

	public NotificationStorage getNotificationStorage() {
		return notificationStorage;
	}

	public void setNotificationStorage(NotificationStorage notificationStorage) {
		this.notificationStorage = notificationStorage;
	}
}
