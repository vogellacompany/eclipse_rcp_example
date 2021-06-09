package com.vogella.tasks.services.internal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

@Component
public class TransientTaskServiceImpl implements TaskService {


	private static AtomicInteger current = new AtomicInteger(91);
	private List<Task> tasks;

    public TransientTaskServiceImpl() {
        tasks = createTestData();
    }

	@Override
    public List<Task> getAll() {
        return tasks.stream().map(Task::copy).collect(Collectors.toList());
    }
 
    @Override
    public void consume(Consumer<List<Task>> taskConsumer) {
        // always pass a new copy of the data
		List<Task> collect = tasks.stream().map(Task::copy).collect(Collectors.toList());
		taskConsumer.accept(collect);
    }


    // create or update an existing instance of object
    @Override
    public synchronized boolean update(Task newTask) {
        // hold the Optional object as reference to determine, if the object is
        // newly created or not
        Optional<Task> taskOptional = findById(newTask.getId());

        // get the actual object or create a new one
        Task task = taskOptional.orElse(new Task(current.getAndIncrement()));
        task.setSummary(newTask.getSummary());
        task.setDescription(newTask.getDescription());
        task.setDone(newTask.isDone());
        task.setDueDate(newTask.getDueDate());

        if (!taskOptional.isPresent()) {
            tasks.add(task);
        }
        return true;
    }

    @Override
    public Optional<Task> get(long id) {
        return findById(id).map(Task::copy);
    }

    @Override
    public boolean delete(long id) {
        Optional<Task> deletedTask = findById(id);
        deletedTask.ifPresent(t -> tasks.remove(t));
        return deletedTask.isPresent();
    }

    // Example data, change if you like
    private List<Task> createTestData() {
        List<Task> list = List.of(create("Application model", "Flexible and extensible"),
                create("DI", "@Inject as programming mode"), create("OSGi", "Services"),
                create("SWT", "Widgets"), create("JFace", "Especially Viewers!"),
                create("CSS Styling", "Style your application"),
				create("CSS Styling", "Styling for custom widget"),
                create("Eclipse services", "Selection, model, Part"),
                create("Renderer", "Different UI toolkit"), create("Compatibility Layer", "Run Eclipse 3.x"));
        return new ArrayList<>(list);
    }

    private Task create(String summary, String description) {
        return new Task(current.getAndIncrement(), summary, description, false, LocalDate.now());
    }

    private Optional<Task> findById(long id) {
        return tasks.stream().filter(t -> t.getId() == id).findAny();
    }

}