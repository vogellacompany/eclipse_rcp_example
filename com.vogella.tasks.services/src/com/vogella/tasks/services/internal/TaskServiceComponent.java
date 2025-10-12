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

/**
 * OSGi Declarative Services component that provides TaskService.
 * This is a simplified version of TransientTaskServiceImpl without Eclipse DI dependencies,
 * suitable for OSGi testing.
 */
@Component(service = TaskService.class)
public class TaskServiceComponent implements TaskService {

    private static AtomicInteger current = new AtomicInteger(1);
    private List<Task> tasks;

    public TaskServiceComponent() {
        tasks = createTestData();
    }

    @Override
    public void consume(Consumer<List<Task>> taskConsumer) {
        taskConsumer.accept(tasks.stream().map(Task::copy).collect(Collectors.toList()));
    }

    @Override
    public synchronized boolean update(Task newTask) {
        Optional<Task> taskOptional = findById(newTask.getId());
        Task task = taskOptional.orElse(new Task(current.getAndIncrement()));
        task.setSummary(newTask.getSummary());
        task.setDescription(newTask.getDescription());
        task.setDone(newTask.isDone());
        task.setDueDate(newTask.getDueDate());

        if (!taskOptional.isPresent()) {
            tasks.add(task);
        }
        
        JSONUtil.saveAsGson(tasks);
        return true;
    }

    @Override
    public Optional<Task> get(long id) {
        return findById(id).map(Task::copy);
    }

    @Override
    public boolean delete(long id) {
        Optional<Task> deletedTask = findById(id);
        deletedTask.ifPresent(tasks::remove);
        return deletedTask.isPresent();
    }

    private List<Task> createTestData() {
        List<Task> list = JSONUtil.retrieveSavedData();
        if (list.isEmpty()) {
            list = List.of(
                create("Application model", "Flexible and extensible"),
                create("DI", "@Inject as programming mode"),
                create("OSGi", "Services"),
                create("SWT", "Widgets"),
                create("JFace", "Especially Viewers!"),
                create("CSS Styling", "Style your application"),
                create("Eclipse services", "Selection, model, Part"),
                create("Renderer", "Different UI toolkit"),
                create("Compatibility Layer", "Run Eclipse 3.x")
            );
        }
        return new ArrayList<>(list);
    }

    private Task create(String summary, String description) {
        return new Task(current.getAndIncrement(), summary, description, false, LocalDate.now());
    }

    private Optional<Task> findById(long id) {
        return tasks.stream().filter(t -> t.getId() == id).findAny();
    }

    @Override
    public List<Task> getAll() {
        return tasks.stream().map(Task::copy).collect(Collectors.toList());
    }
}
