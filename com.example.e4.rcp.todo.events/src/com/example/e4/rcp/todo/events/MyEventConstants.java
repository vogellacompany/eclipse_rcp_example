package com.example.e4.rcp.todo.events;
/**
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * Only used for constant definition
 */

public interface MyEventConstants {

	// topic identifier for all todo topics
	String TOPIC_TODO = "TOPIC_TODOS";
	
	// this key can only be used for event registration, you cannot
	// send out generic events
	String TOPIC_TODO_ALLTOPICS = "TOPIC_TODOS/*";

	String TOPIC_TODOS_CHANGED = "TOPIC_TODOS/CHANGED";
	
	String TOPIC_TODO_NEW = "TOPIC_TODOS/TODO/NEW";

	String TOPIC_TODO_DELETE = "TOPIC_TODOS/TODO/DELETED";

	String TOPIC_TODO_UPDATE = "TOPIC_TODOS/TODO/UPDATED";
}
