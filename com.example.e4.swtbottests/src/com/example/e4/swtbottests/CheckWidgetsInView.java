package com.example.e4.swtbottests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckWidgetsInView {

	private static SWTBot bot;

	@BeforeEach
	public void beforeClass() throws Exception {
		// don't use SWTWorkbenchBot here which relies on Platform 3.x
		bot = new SWTBot();
	}

	@Test
	public void findView() {
		SWTBotText textWithMessage = bot.textWithMessage("Enter text to mark part as dirty");
		assertNotNull(textWithMessage);
		assertTrue(textWithMessage.isEnabled());
	}

	@Test
	public void findTable() {
		SWTBotTable table = bot.table();
		assertNotNull(table);
		assertEquals(5, table.rowCount(), "Table should have 5 entries");
	}

	@AfterEach
	public void sleep() {
		bot.sleep(2000);
	}
}
