package com.vogella.tasks.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;

import jakarta.inject.Named;

public class ThemeSwitchHandler {
    
    @SuppressWarnings("restriction")
	@Execute
    public void switchTheme(@Optional @Named("themeId") String theme, IThemeEngine themeEngine) {
    	themeEngine.setTheme(theme, true);
    }
}