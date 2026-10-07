package com.vogella.tasks.ui.addon;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import jakarta.inject.Inject;

/** Minimizes the Playground part stack when the application has started. */
public class MinimizePlaygroundStackAddon {

    private static final String STACK_ID = "com.vogella.tasks.ui.partstack.playground";

    @Inject
    @Optional
    public void minimize(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Object event,
            EModelService modelService, MApplication application) {
        MUIElement stack = modelService.find(STACK_ID, application);
        // adding the tag at runtime, rather than in the model, makes the MinMaxAddon create the trim stack
        if (stack != null && !stack.getTags().contains(IPresentationEngine.MINIMIZED)) {
            stack.getTags().add(IPresentationEngine.MINIMIZED);
        }
    }
}
