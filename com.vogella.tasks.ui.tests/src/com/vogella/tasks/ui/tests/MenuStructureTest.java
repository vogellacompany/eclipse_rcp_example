package com.vogella.tasks.ui.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the menu structure of the Tasks UI application.
 */
public class MenuStructureTest {

	private static SWTBot bot;

	@BeforeEach
	public void setUp() throws Exception {
		// don't use SWTWorkbenchBot here which relies on Platform 3.x
		bot = new SWTBot();
	}
	
	@Test
	public void testFileMenuExists() {
		SWTBotMenu fileMenu = bot.menu("File");
		assertNotNull(fileMenu, "File menu should exist");
	}
	
	@Test
	public void testEditMenuExists() {
		SWTBotMenu editMenu = bot.menu("Edit");
		assertNotNull(editMenu, "Edit menu should exist");
	}
	
	@Test
	public void testWindowMenuExists() {
		SWTBotMenu windowMenu = bot.menu("Window");
		assertNotNull(windowMenu, "Window menu should exist");
	}
	
	@Test
	public void testProcessesMenuExists() {
		SWTBotMenu processesMenu = bot.menu("Processes");
		assertNotNull(processesMenu, "Processes menu should exist");
	}
	
	@Test
	public void testFileMenuContainsSave() {
		SWTBotMenu fileMenu = bot.menu("File");
		SWTBotMenu saveMenu = fileMenu.menu("Save");
		assertNotNull(saveMenu, "File menu should contain Save menu item");
	}
	
	@Test
	public void testWindowMenuContainsPerspectives() {
		SWTBotMenu windowMenu = bot.menu("Window");
		SWTBotMenu perspectivesMenu = windowMenu.menu("Perspectives");
		assertNotNull(perspectivesMenu, "Window menu should contain Perspectives submenu");
	}
	
	@Test
	public void testWindowMenuContainsTheme() {
		SWTBotMenu windowMenu = bot.menu("Window");
		SWTBotMenu themeMenu = windowMenu.menu("Theme");
		assertNotNull(themeMenu, "Window menu should contain Theme submenu");
	}
}
