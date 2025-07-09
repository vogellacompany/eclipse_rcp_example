package com.vogella.tasks.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.workbench.IWorkbench;

import jakarta.inject.Inject;
import jakarta.inject.Named;

public class ThemeSwitchHandler {
    private static final String DEFAULT_THEME = "com.vogella.eclipse.css.default";
    private static final String RAINBOW_THEME = "com.vogella.eclipse.css.rainbow";
    
    
    @Execute
    public void switchTheme(@Optional  IThemeEngine engine, @Optional @Named("theme") String theme, IWorkbench wb) {
            engine.setTheme(theme , true);
            wb.restart();
    }
}