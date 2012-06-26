package com.example.e4.rcp.todo.addons;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;

public class MyModelAddon {
	public static final String TOPIC_TODO_DATA_UPDATE 
		= "TOPIC_TODO_DATA_UPDATE";

	@PostConstruct
	public void init(IEclipseContext context) {
		context.set("test1", "Hello");
	}
}
