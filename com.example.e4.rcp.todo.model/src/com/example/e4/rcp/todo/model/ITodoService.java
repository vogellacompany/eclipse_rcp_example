package com.example.e4.rcp.todo.model;

import java.util.List;
import java.util.Optional;

public interface ITodoService {
	
	List<Todo> getTodos();

	boolean saveTodo(Todo todo);

	Optional<Todo> getTodo(long id);

	boolean deleteTodo(long id);

}
