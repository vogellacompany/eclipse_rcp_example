package com.vogella.osgi.taskconsumer;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.vogella.tasks.model.TaskService;

@Component
public class TaskConsumer {
 
    @Reference
    TaskService taskService;

// The above shows a field reference, you could also use @Reference on a method
//  private void bindTaskService(TaskService taskService) {
//      this.taskService = taskService;
//      System.out.println("Injected " + taskService);
//  }
//
//  @SuppressWarnings("unused")
//  private void unbindTaskService(TaskService taskService) {
//      System.out.println("Removed " + taskService);
//      taskService = null;
//  }

    @Activate
    public void activate() {
        System.out.println("Activate called");
        System.out.println("Number of tasks: " + taskService.getAll().size());
    }
}