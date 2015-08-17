package com.example.e4.rcp.todo.addons;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.osgi.service.event.Event;

public class ToolbarsVisiblePerPerspectiveAddon {

	@Inject
	@Optional
	EModelService modelService;
	
	@Inject
	@Optional
	public void subscribeTopicSelectedElement(
			 @EventTopic(UIEvents.ElementContainer.TOPIC_SELECTEDELEMENT) Event event) {
		Object element = event.getProperty(EventTags.ELEMENT);
		Object newValue = event.getProperty(EventTags.NEW_VALUE);
		if (!(element instanceof MPerspectiveStack) || !(newValue instanceof MPerspective)) {
			return;
		}
		MPerspective perspective = (MPerspective) newValue;
		if(modelService==null) {
			return;
		}
		MWindow topLevelWindowFor = modelService.getTopLevelWindowFor(perspective);
		List<MToolBar> toolbars = modelService.findElements(topLevelWindowFor, null, MToolBar.class, null);
		for (MToolBar toolbar : toolbars) {
			if (toolbar.getTags().isEmpty()) {
				toolbar.setVisible(true);
				toolbar.setToBeRendered(true);
			}
			if (toolbar.getTags().contains("perspective:"+perspective.getElementId())) {
				toolbar.setVisible(true);
				toolbar.setToBeRendered(true);
			} else {
				toolbar.setVisible(false);
				toolbar.setToBeRendered(false);
			}
		}
	}
}