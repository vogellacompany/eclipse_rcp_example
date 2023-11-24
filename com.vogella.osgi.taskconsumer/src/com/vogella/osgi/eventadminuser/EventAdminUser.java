package com.vogella.osgi.eventadminuser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import com.vogella.tasks.model.TaskService;

@Component
public class EventAdminUser {

	@Reference
	EventAdmin admin;

	@Activate
	public void activate() {

		String payload = "OBJECT to BE SEND";

		// When your program starts up
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		// then, when you want to schedule a task
		Runnable task = new Runnable() {

			@Override
			public void run() {

				Map<String, Object> properties = new HashMap<>();

				properties.put("org.eclipse.e4.data", payload);
				Event event = new Event("YOURKEY", properties);
				admin.sendEvent(event);

			}
		};
		executor.schedule(task, 5, TimeUnit.SECONDS);

		// and finally, when your program wants to exit
		executor.shutdown();

	}
}