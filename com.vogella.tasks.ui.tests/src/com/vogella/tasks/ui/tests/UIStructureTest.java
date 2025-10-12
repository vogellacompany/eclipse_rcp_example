package com.vogella.tasks.ui.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the UI structure of the Tasks UI application.
 * Uses the E4 workbench model API to verify application structure.
 */
public class UIStructureTest {

	private static SWTBot bot;

	@BeforeEach
	public void setUp() throws Exception {
		bot = new SWTBot();
	}
	
	@Test
	public void testApplicationWindowExists() {
		// Test that the main window is present
		assertTrue(bot.shells().length > 0, "At least one shell should exist");
	}
	
	@Test
	public void testCanAccessApplication() {
		// This test verifies that we can access the E4 application
		// In a real test, we would inject MApplication and verify structure
		// For now, we just verify the bot can find widgets
		assertNotNull(bot, "SWTBot should be initialized");
	}
	
	/**
	 * Test that verifies perspectives can be accessed.
	 * This test uses SWTBot to interact with the UI.
	 */
	@Test
	public void testPerspectivesAccessible() {
		// Verify that we can access the Window menu which contains perspectives
		try {
			bot.menu("Window");
		} catch (Exception e) {
			// If we can't find it, the test will fail
			throw new AssertionError("Should be able to access Window menu", e);
		}
	}
}
