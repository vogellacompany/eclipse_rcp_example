package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;

public class ThemeSwitchHandler {
	static boolean doIt= true;
	@Execute
	public void switchTheme(IThemeEngine engine) {
		System.out.println("ThemeSwitchHandler called");
		// The last argument controls
		// whether the change should be persisted and
		// restored on restart
		if (doIt) {
			engine.setTheme("de.vogella.e4.todo.darktheme", true);
			
		} else {
			engine.setTheme("de.vogella.e4.todo.redtheme", true);
		}
		doIt= !doIt;
		
	}
}
