package com.vogella.tasks.services.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

/**
 * Plug-in test for TaskService OSGi service.
 * This test verifies that the TaskService is properly available as an OSGi service
 * and functions correctly.
 */
class TransientTaskServiceImplTests {

    private TaskService taskService;
    private BundleContext bundleContext;
    private ServiceReference<TaskService> serviceReference;

    @BeforeEach
    public void setUp() {
        bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
        assertNotNull(bundleContext, "Bundle context should be available");
        
        serviceReference = bundleContext.getServiceReference(TaskService.class);
        assertNotNull(serviceReference, "TaskService should be registered as an OSGi service");
        
        taskService = bundleContext.getService(serviceReference);
        assertNotNull(taskService, "TaskService should be available");
    }

    @AfterEach
    public void tearDown() {
        if (serviceReference != null) {
            bundleContext.ungetService(serviceReference);
        }
    }

    @Test
    @DisplayName("Ensures that the test service always returns 9 elements")
    void testThatTestServiceReturnsNineTasks() {
        List<Task> tasks = taskService.getAll();
        assertEquals(9, tasks.size());
    }

    @Test
    @DisplayName("Test getting a task by ID")
    void testGetTaskById() {
        List<Task> tasks = taskService.getAll();
        assertTrue(tasks.size() > 0, "Should have tasks");
        
        long firstTaskId = tasks.get(0).getId();
        Optional<Task> task = taskService.get(firstTaskId);
        
        assertTrue(task.isPresent(), "Should find task by ID");
        assertEquals(firstTaskId, task.get().getId());
    }

    @Test
    @DisplayName("Test updating a task")
    void testUpdateTask() {
        Task newTask = new Task(999, "Test Task", "Test Description", false, LocalDate.now());
        boolean updated = taskService.update(newTask);
        
        assertTrue(updated, "Task should be updated successfully");
        
        Optional<Task> retrieved = taskService.get(999);
        assertTrue(retrieved.isPresent(), "Updated task should be retrievable");
        assertEquals("Test Task", retrieved.get().getSummary());
    }

    @Test
    @DisplayName("Test deleting a task")
    void testDeleteTask() {
        // First create a task
        Task newTask = new Task(888, "To Delete", "Delete me", false, LocalDate.now());
        taskService.update(newTask);
        
        // Verify it exists
        Optional<Task> beforeDelete = taskService.get(888);
        assertTrue(beforeDelete.isPresent(), "Task should exist before deletion");
        
        // Delete it
        boolean deleted = taskService.delete(888);
        assertTrue(deleted, "Task should be deleted successfully");
        
        // Verify it's gone
        Optional<Task> afterDelete = taskService.get(888);
        assertTrue(afterDelete.isEmpty(), "Task should not exist after deletion");
    }

    @Test
    @DisplayName("Test consuming tasks")
    void testConsumeTask() {
        taskService.consume(tasks -> {
            assertNotNull(tasks, "Tasks list should not be null");
            assertEquals(9, tasks.size(), "Should have 9 tasks");
        });
    }
}
