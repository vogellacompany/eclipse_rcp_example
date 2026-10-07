package com.vogella.tasks.ui.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.ui.parts.TaskCategory;

public class TaskCategoryTest {

	private static final LocalDate TODAY = LocalDate.of(2026, 10, 7);

	@Test
	public void categorizesByDueDateAndDoneFlag() {
		assertEquals(TaskCategory.OVERDUE, category(false, TODAY.minusDays(1)));
		assertEquals(TaskCategory.DUE_TODAY, category(false, TODAY));
		assertEquals(TaskCategory.UPCOMING, category(false, TODAY.plusDays(1)));
		assertEquals(TaskCategory.DONE, category(true, TODAY.minusDays(1)));
	}

	private static TaskCategory category(boolean done, LocalDate due) {
		return TaskCategory.of(new Task(1, "s", "d", done, due), TODAY);
	}
}
