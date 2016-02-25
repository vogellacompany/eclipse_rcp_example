package com.example.e4.rcp.todo.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface ITodoService {
	
	Tag<Tag<Todo>> getRootTag();
	
	List<Tag<Todo>> getTags(long id);
	
	void getTodos(Consumer<List<Todo>> todosConsumer);

	boolean saveTodo(Todo todo);

	Optional<Todo> getTodo(long id);

	boolean deleteTodo(long id);

}
