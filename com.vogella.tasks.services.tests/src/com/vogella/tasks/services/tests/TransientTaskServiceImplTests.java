package com.vogella.tasks.services.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.services.internal.TransientTaskServiceImpl;

class TransientTaskServiceImplTests {

	@Test
	@DisplayName("Ensures that the test service always returns 9 elements")
	void testThatTestServiceReturnsNineTasks() {
		TransientTaskServiceImpl taskService = new TransientTaskServiceImpl();
		List<Task> tasks = taskService.getAll();
		assertEquals(9, tasks.size());
	}

}
