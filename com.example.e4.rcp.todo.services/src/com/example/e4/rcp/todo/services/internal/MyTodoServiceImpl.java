package com.example.e4.rcp.todo.services.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;

public class MyTodoServiceImpl implements ITodoService {

	private static int current = 1;
	private List<Todo> todos;

	// use dependency injection in MyTodoServiceImpl
	@Inject
	private IEventBroker broker;

	public MyTodoServiceImpl() {
		todos = createInitialModel();
	}

	// always return a new copy of the data
	@Override
	public List<Todo> getTodos() {
		List<Todo> list = new ArrayList<>();
		for (Todo todo : todos) {
			list.add(todo.copy());
		}
		return list;
	}

	// create or update an existing instance of Todo
	@Override
	public synchronized boolean saveTodo(Todo newTodo) {
		boolean created = false;
		Optional<Todo> updateTodo = findById(newTodo.getId());
		if (!updateTodo.isPresent()) {
			created = true;
			updateTodo = Optional.of(new Todo(current++));
			todos.add(updateTodo.get());
		}
		updateTodo.get().setSummary(newTodo.getSummary());
		updateTodo.get().setDescription(newTodo.getDescription());
		updateTodo.get().setDone(newTodo.isDone());
		updateTodo.get().setDueDate(newTodo.getDueDate());

		// configure the event

		// send out events
		if (created) {
			broker.post(MyEventConstants.TOPIC_TODO_NEW,
					createEventData(MyEventConstants.TOPIC_TODO_NEW, 
							String.valueOf(updateTodo.get().getId())));
		} else {
			broker.
			post(MyEventConstants.TOPIC_TODO_UPDATE,
			 createEventData(MyEventConstants.TOPIC_TODO_UPDATE, 
					 String.valueOf(updateTodo.get().getId())));
		}
		return true;
	}

	@Override
	public Optional<Todo> getTodo(long id) {
		Optional<Todo> todo = findById(id);

		if (todo.isPresent()) {
			return Optional.of(todo.get().copy());
		}
		return Optional.empty();
	}

	@Override
	public boolean deleteTodo(long id) {
		Optional<Todo> deleteTodo = findById(id);

		if (deleteTodo.isPresent()) {
			todos.remove(deleteTodo.get());
			// configure the event
			broker.
				post(MyEventConstants.TOPIC_TODO_DELETE,
				 createEventData(MyEventConstants.TOPIC_TODO_DELETE, 
						 String.valueOf(deleteTodo.get().getId())));
			return true;
		}
		return false;
	}

	// Example data, change if you like
	private List<Todo> createInitialModel()         {
		List<Todo> list = new ArrayList<>();
		list.add(createTodo("Application model", "Flexible and extensible"));
		list.add(createTodo("DI", "@Inject as programming mode"));		
		list.add(createTodo("OSGi", "Services"));
		list.add(createTodo("SWT", "Widgets"));
		list.add(createTodo("JFace", "Especially Viewers!"));
		list.add(createTodo("CSS Styling", "Style your application"));
		list.add(createTodo("Eclipse services", "Selection, model, Part"));
		list.add(createTodo("Renderer", "Different UI toolkit"));
		list.add(createTodo("Compatibility Layer", "Run Eclipse 3.x"));
		return list;
	}

	private Todo createTodo(String summary, String description) {
		return new Todo(current++, summary, description, false, new Date());
	}

	private Optional<Todo> findById(long id) {
		for (Todo todo : getTodos()) {
			if (id == todo.getId()) {
				return Optional.of(todo);
			}
		}
		return Optional.empty();
	}

	private Map<String, String> createEventData(String topic, String todoId) {
		Map<String, String> map = new HashMap<>();
		// in case the receiver wants to check the topic
		map.put(MyEventConstants.TOPIC_TODO, topic);
		// which todo has changed
		map.put(Todo.FIELD_ID, todoId);
		return map;
	}
}
