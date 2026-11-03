package com.vogella.tasks.ui.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Display;

import jakarta.inject.Named;

/**
 * Applies the selected theme and renders the windows again, since native widget state, such as
 * the dark look of buttons, is only applied when the widgets are created.
 */
public class ThemeSwitchHandler {

    @SuppressWarnings("restriction")
    @Execute
    public void switchTheme(@Optional @Named("themeId") String theme, IThemeEngine themeEngine,
            EPartService partService, MApplication application) {
        ITheme active = themeEngine.getActiveTheme();
        if ((active != null && active.getId().equals(theme)) || !partService.saveAll(true)) {
            return;
        }
        // edits the user chose not to save are gone after rendering again
        List.copyOf(partService.getDirtyParts()).forEach(part -> part.setDirty(false));
        themeEngine.setTheme(theme, true);
        // not while the menu that triggered this handler is still being processed
        Display.getCurrent().asyncExec(() -> renderWindowsAgain(application));
    }

    private void renderWindowsAgain(MApplication application) {
        MWindow selected = application.getSelectedElement();
        for (MWindow window : application.getChildren()) {
            window.setToBeRendered(false);
            window.setToBeRendered(true);
        }
        application.setSelectedElement(selected);
    }
}
