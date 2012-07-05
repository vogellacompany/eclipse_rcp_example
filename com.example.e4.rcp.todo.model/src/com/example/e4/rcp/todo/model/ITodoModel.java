package com.example.e4.rcp.todo.model;

import java.util.List;

public interface ITodoModel {
	List<Todo> getTodos();

	boolean saveTodo(Todo todo);

	Todo getTodo(long id);

	boolean deleteTodo(long id);
}
