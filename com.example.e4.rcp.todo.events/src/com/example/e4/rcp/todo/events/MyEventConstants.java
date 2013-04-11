package com.example.e4.rcp.todo.events;

public interface MyEventConstants {

	// Only used for event registration, you cannot
	// send out generic events
	String TOPIC_TODO_ALLTOPICS = "TOPIC_TODO/*";

	String TOPIC_TODO_NEW = "TOPIC_TODO/NEW";

	String TOPIC_TODO_DELETE = "TOPIC_TODO/DELETED";

	String TOPIC_TODO_UPDATE = "TOPIC_TODO/UPDATED";
}
