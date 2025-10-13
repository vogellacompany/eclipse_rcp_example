package com.vogella.tasks.services.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
    @DisplayName("Ensures that the test service returns tasks")
    void testThatTestServiceReturnsTasks() {
        List<Task> tasks = taskService.getAll();
        // The service returns at least 9 tasks initially, but may have more due to other tests
        assertTrue(tasks.size() >= 9, "Should have at least 9 tasks, got " + tasks.size());
    }

    @Test
    @DisplayName("Test getting a task by ID")
    void testGetTaskById() {
        List<Task> tasks = taskService.getAll();
        assertTrue(tasks.size() > 0, "Should have tasks");
        
        long firstTaskId = tasks.get(0).getId();
        var task = taskService.get(firstTaskId);
        
        assertTrue(task.isPresent(), "Should find task by ID");
        assertEquals(firstTaskId, task.get().getId());
    }

    @Test
    @DisplayName("Test consuming tasks")
    void testConsumeTask() {
        taskService.consume(tasks -> {
            assertNotNull(tasks, "Tasks list should not be null");
            assertTrue(tasks.size() >= 9, "Should have at least 9 tasks");
        });
    }
}
