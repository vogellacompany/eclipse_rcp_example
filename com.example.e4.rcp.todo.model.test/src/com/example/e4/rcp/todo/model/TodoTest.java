package com.example.e4.rcp.todo.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TodoTest {

	private Todo t;

	@Before
	public void setUp() throws Exception {
		System.out.println("TodoTest#setup");
		t = new Todo(-1);
	}

	@Test
	public void testIsDone() {
		t.setDone(true);
		assertTrue(t.isDone());
	}
	
	
}
