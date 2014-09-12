package com.example.e4.rcp.todo.events;

public final class MyEventConstants {
	
	private MyEventConstants() {
		// prevent instantiation
	}
	
	// this key can only used for event registration, you cannot
	// send out generic events
	public static final String TOPIC_TODO_ALLTOPICS = "TOPIC_TODOS/*";

	// events to be send out
	public static final String TOPIC_TODOS_CHANGED = "TOPIC_TODOS/CHANGED";
	
	public static final String TOPIC_TODO_NEW = "TOPIC_TODOS/TODO/NEW";

	public static final String TOPIC_TODO_DELETE = "TOPIC_TODOS/TODO/DELETED";

	public static final String TOPIC_TODO_UPDATE = "TOPIC_TODOS/TODO/UPDATED";
}
