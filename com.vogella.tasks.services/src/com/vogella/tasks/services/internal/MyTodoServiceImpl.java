package com.vogella.tasks.services.internal;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;

import com.vogella.tasks.model.ITodoService;
import com.vogella.tasks.model.Todo;

@Component
public class MyTodoServiceImpl implements ITodoService {



	private static AtomicInteger current = new AtomicInteger(1);
	private List<Todo> todos;

	public MyTodoServiceImpl() {
		todos = createInitialModel();
	}

	@Override
	public void getTodos(Consumer<List<Todo>> todosConsumer) {
		// always pass a new copy of the data
		todosConsumer.accept(todos.stream().map(Todo::copy).collect(Collectors.toList()));
	}

	protected List<Todo> getTodosInternal() {
		return todos;
	}

	// create or update an existing instance of object
	@Override
	public synchronized boolean saveTodo(Todo newTodo) {
		// hold the Optional object as reference to determine, if the object is
		// newly created or not
		Optional<Todo> todoOptional = findById(newTodo.getId());

		Todo todo = null;
		// get the actual object or create a new one
		if (!todoOptional.isEmpty()) {
			todo = todoOptional.get();
		} else {
			todo = new Todo(current.getAndIncrement());
		}

		todo.setSummary(newTodo.getSummary());
		todo.setDescription(newTodo.getDescription());
		todo.setDone(newTodo.isDone());
		todo.setDueDate(newTodo.getDueDate());

		if (!todoOptional.isPresent()) {
			todos.add(todo);
		}
		JSONUtil.saveAsGson(todos);
		return true;
	}



	@Override
	public Optional<Todo> getTodo(long id) {
		return findById(id).map(Todo::copy);
	}

	@Override
	public boolean deleteTodo(long id) {
		Optional<Todo> deleteTodo = findById(id);
		deleteTodo.ifPresent(todo -> todos.remove(todo));
		return deleteTodo.isPresent();
	}

	// Example data, change if you like
	private List<Todo> createInitialModel() {
		List<Todo> list = JSONUtil.retrieveSavedTodos();
		// just for testing, if we retrieve no objects, we simply generate a few
		if (list.isEmpty()) {
			// Create some initial content
			list.add(createTodo("Application model", "Flexible and extensible"));
			list.add(createTodo("DI", "@Inject as programming mode"));
			list.add(createTodo("OSGi", "Services"));
			list.add(createTodo("SWT", "Widgets"));
			list.add(createTodo("JFace", "Especially Viewers!"));
			list.add(createTodo("CSS Styling", "Style your application"));
			list.add(createTodo("Eclipse services", "Selection, model, Part"));
			list.add(createTodo("Renderer", "Different UI toolkit"));
			list.add(createTodo("Compatibility Layer", "Run Eclipse 3.x"));
			list.add(createTodo("Maven", "Learning Tycho"));
		}

		return list;
	}


	private Todo createTodo(String summary, String description) {
		return new Todo(current.getAndIncrement(), summary, description, false, new Date());
	}

	private Optional<Todo> findById(long id) {
		return getTodosInternal().stream().filter(t -> t.getId() == id).findAny();
	}

}
