package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;

public class ThemeSwitchHandler {
	private static final String DEFAULT_THEME = "com.example.e4.rcp.todo.default";
	private static final String RAINBOW_THEME = "com.example.e4.rcp.todo.rainbow";
	
	// Remember the state
	static boolean defaulttheme= true;
	@Execute
	public void switchTheme(IThemeEngine engine) {
		// second argument defines that change is 
		//persisted and restored on restart
		if (!engine.getActiveTheme().getId().equals(DEFAULT_THEME)){
			engine.setTheme(DEFAULT_THEME, true);
		} else {
			engine.setTheme(RAINBOW_THEME, true);
		}
	}
}
