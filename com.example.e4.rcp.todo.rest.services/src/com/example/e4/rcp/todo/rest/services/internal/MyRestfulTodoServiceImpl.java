package com.example.e4.rcp.todo.rest.services.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;

public class MyRestfulTodoServiceImpl implements ITodoService {

	private static AtomicInteger current = new AtomicInteger(1);
	private List<Todo> todos;

	// use dependency injection in MyTodoServiceImpl
	@Inject
	private IEventBroker broker;

	@Inject
	private UISynchronize uiSync;
	
	public MyRestfulTodoServiceImpl() {
		todos = createInitialModel();
	}

	@Override
	public void getTodos(Consumer<List<Todo>> todosConsumer) {
		// TODO use Job's create factory method, once we use Eclipse Neon as target
		Job collectTodosJob = new TodoRestCallJob(receivedTodos -> {
			// always pass a new copy of the data
			List<Todo> todosCopy = receivedTodos.stream().map(t -> t.copy()).collect(Collectors.toList());
			// the given consumer could be in the UI thread, therefore it must be synchronized
			uiSync.asyncExec(() -> todosConsumer.accept(todosCopy));
		});
		collectTodosJob.schedule();
	}

	protected List<Todo> getTodosInternal() {
		return todos;
	}

	// create or update an existing instance of Todo
	@Override
	public synchronized boolean saveTodo(Todo newTodo) {
		// hold the Optional object as reference to determine, if the Todo is
		// newly created or not
		Optional<Todo> todoOptional = findById(newTodo.getId());

		// get the actual todo or create a new one
		Todo todo = todoOptional.orElse(new Todo(current.getAndIncrement()));
		todo.setSummary(newTodo.getSummary());
		todo.setDescription(newTodo.getDescription());
		todo.setDone(newTodo.isDone());
		todo.setDueDate(newTodo.getDueDate());

		// send out events
		if (todoOptional.isPresent()) {
			broker.post(MyEventConstants.TOPIC_TODO_UPDATE,
					createEventData(MyEventConstants.TOPIC_TODO_UPDATE, String.valueOf(todo.getId())));
		} else {
			todos.add(todo);
			broker.post(MyEventConstants.TOPIC_TODO_NEW,
					createEventData(MyEventConstants.TOPIC_TODO_NEW, String.valueOf(todo.getId())));
		}
		return true;
	}

	@Override
	public Optional<Todo> getTodo(long id) {
		return findById(id).map(todo -> todo.copy());
	}

	@Override
	public boolean deleteTodo(long id) {
		Optional<Todo> deleteTodo = findById(id);

		deleteTodo.ifPresent(todo -> {
			todos.remove(todo);
			
			// configure the event
			broker.post(MyEventConstants.TOPIC_TODO_DELETE,
					createEventData(MyEventConstants.TOPIC_TODO_DELETE, String.valueOf(todo.getId())));
		});

		return deleteTodo.isPresent();
	}

	// Example data, change if you like
	private List<Todo> createInitialModel() {
		List<Todo> list = new ArrayList<>();
		list.add(createTodo("Restful APIs", "Use Retrofit in OSGi"));
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
		return new Todo(current.getAndIncrement(), summary, description, false, new Date());
	}

	private Optional<Todo> findById(long id) {
		return getTodosInternal().stream().filter(t -> t.getId() == id).findAny();
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
