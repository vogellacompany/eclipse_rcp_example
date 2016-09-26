package com.example.e4.rcp.todo.services.internal;

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

import org.eclipse.e4.core.services.events.IEventBroker;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Tag;
import com.example.e4.rcp.todo.model.Todo;

public class MyTodoServiceImpl implements ITodoService {

	private static AtomicInteger current = new AtomicInteger(1);
	private List<Todo> todos;

	// use dependency injection in MyTodoServiceImpl
	@Inject
	private IEventBroker broker;
	private Tag<Tag<Todo>> rootTag;

	public MyTodoServiceImpl() {
		todos = createInitialModel();
		createRootTag(todos);
	}

	@Override
	public void getTodos(Consumer<List<Todo>> todosConsumer) {
		// always pass a new copy of the data
		todosConsumer.accept(todos.stream().map(t -> t.copy()).collect(Collectors.toList()));
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

	@Override
	public Tag<Tag<Todo>> getRootTag() {
		return rootTag;
	}

	@Override
	public List<Tag<Todo>> getTags(long id) {
		List<Tag<Todo>> tags = new ArrayList<>();
	
		Optional<Todo> findById = findById(id);
		findById.ifPresent(todo -> findTags(todo, tags, getRootTag()));
	
		return tags;
	}

	// Example data, change if you like
	private List<Todo> createInitialModel() {
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

	private void createRootTag(List<Todo> todos) {
		Tag<Todo> eclipseTag = new Tag<>("Eclipse", todos);
		List<Tag<Todo>> tagList = new ArrayList<>();
		tagList.add(eclipseTag);
		rootTag = new Tag<>("root", tagList);
	}

	private Todo createTodo(String summary, String description) {
		return new Todo(current.getAndIncrement(), summary, description, false, new Date());
	}

	private Optional<Todo> findById(long id) {
		return getTodosInternal().stream().filter(t -> t.getId() == id).findAny();
	}

	private void findTags(Todo todo, List<Tag<Todo>> todosTags, Tag<?> rootTag) {
		List<?> taggedElements = rootTag.getTaggedElements();
		for (Object taggedElement : taggedElements) {
			if (taggedElement instanceof Tag) {
				findTags(todo, todosTags, (Tag<?>) taggedElement);
			} else if (todo.equals(taggedElement)) {
				@SuppressWarnings("unchecked")
				Tag<Todo> foundTag = (Tag<Todo>) rootTag;
				todosTags.add(foundTag);
			}
		}
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
