package com.vogella.tasks.events;
/**
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *
 * Only used for constant definition
 */

public interface TaskEventConstants {

    // topic identifier for all topics
	String TOPIC_TASKS = "TOPIC_TASKS";

    // this key can only be used for event registration, you cannot
    // send out generic events
	String TOPIC_TASKS_ALLTOPICS = "TOPIC_TASKS/*";

	String TOPIC_TASKS_CHANGED = "TOPIC_TASKS/CHANGED";

	String TOPIC_TASKS_NEW = "TOPIC_TASKS/TASKS/NEW";

	String TOPIC_TASKS_DELETE = "TOPIC_TASKS/TASKS/DELETED";

	String TOPIC_TASKS_UPDATE = "TOPIC_TASKS/TASKS/UPDATED";
}