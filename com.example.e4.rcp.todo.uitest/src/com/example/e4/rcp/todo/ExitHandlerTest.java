package com.example.e4.rcp.todo;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class ExitHandlerTest {

	private static SWTBot bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		// Don't use SWTWorkbenchBot here which relies on Platform 3.x
		bot = new SWTBot();
	}
	
	@Test
	public void executeExit() {
		SWTBotMenu fileMenu = bot.menu("File");
		Assert.assertNotNull(fileMenu);
		SWTBotMenu exitMenu = fileMenu.menu("Another Exit");
		Assert.assertNotNull(exitMenu);
		exitMenu.click();
	}

	@AfterClass
	public static void sleep() {
		bot.sleep(2000);
	}
}
