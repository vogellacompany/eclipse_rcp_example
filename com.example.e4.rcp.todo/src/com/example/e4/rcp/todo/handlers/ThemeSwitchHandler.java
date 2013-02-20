package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;

public class ThemeSwitchHandler {
	// Remember the state
	static boolean defaulttheme= true;
	@Execute
	public void switchTheme(IThemeEngine engine) {
		System.out.println("ThemeSwitchHandler called");
		// second argument defines that change is 
		//persisted and restored on restart
		if (!defaulttheme) {
			engine.setTheme("com.vogella.e4.todo.defaulttheme", true);
			
		} else {
			engine.setTheme("com.vogella.e4.todo.darktheme", true);
		}
		defaulttheme= !defaulttheme;
	}
}
