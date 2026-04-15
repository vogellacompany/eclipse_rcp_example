package com.vogella.tasks.model.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.vogella.tasks.model.Task;

class TaskTest {

    @Test
    void taskIdIsRetained() {
        Task task = new Task(42);
        assertEquals(42, task.getId());
    }

    @Test
    void tasksWithSameIdAreEqual() {
        Task t1 = new Task(1);
        Task t2 = new Task(1);
        assertEquals(t1, t2);
    }

    @Test
    void tasksWithDifferentIdsAreNotEqual() {
        Task t1 = new Task(1);
        Task t2 = new Task(2);
        assertNotEquals(t1, t2);
    }

    @Test
    void newTaskHasEmptySummaryAndIsNotDone() {
        Task task = new Task(1);
        assertEquals("", task.getSummary());
        assertFalse(task.isDone());
    }

    @Test
    void copyProducesEquivalentTask() {
        Task original = new Task(7, "Write tests", "Add unit tests", false, LocalDate.of(2026, 1, 1));
        Task copy = original.copy();
        assertEquals(original.getId(), copy.getId());
        assertEquals(original.getSummary(), copy.getSummary());
        assertTrue(original.equals(copy));
    }
}
