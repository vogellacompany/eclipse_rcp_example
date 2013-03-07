package com.example.e4.rcp.todo.events;

public interface MyEventConstants {

	// Only used for event registration, you cannot
	// send out generic events
	String TOPIC_TODO_DATA_UPDATE = "TOPIC_TODO_DATA_UPDATE/*";

	String TOPIC_TODO_DATA_UPDATE_NEW = "TOPIC_TODO_DATA_UPDATE/NEW";

	String TOPIC_TODO_DATA_UPDATE_DELETE = "TOPIC_TODO_DATA_UPDATE/DELETED";

	String TOPIC_TODO_DATA_UPDATE_UPDATED = "TOPIC_TODO_DATA_UPDATE/UPDATED";
}
