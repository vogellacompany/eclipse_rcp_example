package com.vogella.tasks.ui.menu;

import java.util.List;
import java.util.Objects;

import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class DynamicSwitchThemeMenu {
    @SuppressWarnings("restriction")
	@AboutToShow
    public void execute(List<MMenuElement> items, EModelService modelService, MApplication app, IThemeEngine themeEngine) {
        List<ITheme> themes = themeEngine.getThemes();
        for (ITheme theme : themes) {
            items.add(createMenuEntry(app, modelService, theme));
        }
    }

    @SuppressWarnings("restriction")
    private MHandledMenuItem createMenuEntry(MApplication app, EModelService modelService, ITheme theme) {
        List<MCommand> commands = app.getCommands();
        MCommand command = null;
        for (MCommand mCommand : commands) {
            if (mCommand.getElementId().equals("com.vogella.tasks.ui.command.theme.switch")) {
                command = mCommand;
                break;
            }
        }
        Objects.requireNonNull(command,
                "Command must exist and have the ID com.vogella.tasks.ui.command.theme.switch");

        MHandledMenuItem menuItem = modelService.createModelElement(MHandledMenuItem.class);
        menuItem.setLabel(theme.getLabel());
        menuItem.setCommand(command);
        menuItem.getPersistedState().put("persistState", "false");

        MParameter parameter = modelService.createModelElement(MParameter.class);
        parameter.setElementId(theme.getId() + "dynamicmenu");
        parameter.setName("themeId");
        parameter.setValue(theme.getId());

        menuItem.getParameters().add(parameter);
        return menuItem;
    }
}
