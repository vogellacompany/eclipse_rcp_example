package com.vogella.tasks.ui.menu;

import java.util.List;
import java.util.Objects;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class DynamicSwitchPerspectiveMenu {
    @AboutToShow
    public void execute(List<MMenuElement> items, EModelService modelService, MApplication app) {

        List<MPerspective> perspectives = modelService.findElements(app, null, MPerspective.class, null);

        for (MPerspective mPerspective : perspectives) {
            items.add(createMenuEntry(app, modelService, mPerspective));
        }

    }

    private MHandledMenuItem createMenuEntry(MApplication app, EModelService modelService, MPerspective mPerspective) {
        // model services currently does not allow to search for command
        List<MCommand> commands = app.getCommands();
        MCommand command = null;
        for (MCommand mCommand : commands) {
            if (mCommand.getElementId().equals("com.vogella.tasks.ui.command.switchperspective")) {
                command = mCommand;
                break;
            }
        }
        Objects.requireNonNull(command,
                "Command must exist and have the ID com.vogella.tasks.ui.command.switchperspective");

        MHandledMenuItem menuItem = modelService.createModelElement(MHandledMenuItem.class);
        menuItem.setLabel(mPerspective.getLabel());
        menuItem.setCommand(command);

        // ensure that this generated element is not persisted during shutdown
        // otherwise these entries should be re-added with every application start
        // which does not use -clearPersistedState
        menuItem.getPersistedState().put("persistState", "false");

        MParameter parameter = modelService.createModelElement(MParameter.class);
        parameter.setElementId(mPerspective.getElementId() + "dynamicmenu");
        // ensure that perspective_parameter is the parameter you are using in
        // your application model for the command parameter
        parameter.setName("perspective_parameter");
        parameter.setValue(mPerspective.getElementId());

        List<MParameter> parameters = menuItem.getParameters();
        parameters.add(parameter);
        return menuItem;

    }

}