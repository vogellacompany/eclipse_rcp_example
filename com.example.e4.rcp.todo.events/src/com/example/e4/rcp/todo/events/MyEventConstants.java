package com.example.e4.rcp.todo.events;

public interface MyEventConstants {

	// this key can only used for event registration, you cannot
	// send out generic events
	String TOPIC_TODO_ALLTOPICS = "TOPIC_TODOS/*";

	// events to be send out
	String TOPIC_TODOS_CHANGED = "TOPIC_TODOS/CHANGED";
	
	String TOPIC_TODO_NEW = "TOPIC_TODOS/TODO/NEW";

	String TOPIC_TODO_DELETE = "TOPIC_TODOS/TODO/DELETED";

	String TOPIC_TODO_UPDATE = "TOPIC_TODOS/TODO/UPDATED";
}
