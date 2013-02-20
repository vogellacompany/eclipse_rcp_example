package com.example.e4.rcp.todo.model;

import java.util.List;

import org.eclipse.e4.core.services.events.IEventBroker;

public interface ITodoModel {
	
	List<Todo> getTodos();

	boolean saveTodo(Todo todo);

	Todo getTodo(long id);

	boolean deleteTodo(long id);

    void setEventBroker(IEventBroker broker);
}
