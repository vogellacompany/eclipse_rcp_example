package com.vogella.tasks.ui.toolcontrols;

import java.net.URI;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.osgi.service.event.Event;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

/**
 * Vertical tool bar with one radio button per perspective of the window, kept in sync with the
 * model since the application model has no dynamic contribution for tool bars.
 */
public class PerspectiveSwitcherToolControl {

	@Inject
	private MWindow window;

	@Inject
	private EModelService modelService;

	@Inject
	private EPartService partService;

	private ToolBar toolBar;
	private LocalResourceManager resources;

	@PostConstruct
	public void createGui(Composite parent) {
		toolBar = new ToolBar(parent, SWT.FLAT | SWT.VERTICAL);
		resources = new LocalResourceManager(JFaceResources.getResources(), toolBar);
		refresh();
	}

	@Inject
	@Optional
	public void selectionChanged(@UIEventTopic(UIEvents.ElementContainer.TOPIC_SELECTEDELEMENT) Event event) {
		if (event.getProperty(UIEvents.EventTags.ELEMENT) instanceof MPerspectiveStack) {
			refresh();
		}
	}

	@Inject
	@Optional
	public void childrenChanged(@UIEventTopic(UIEvents.ElementContainer.TOPIC_CHILDREN) Event event) {
		if (event.getProperty(UIEvents.EventTags.ELEMENT) instanceof MPerspectiveStack) {
			refresh();
		}
	}

	@Inject
	@Optional
	public void labelChanged(@UIEventTopic(UIEvents.UILabel.TOPIC_ALL) Event event) {
		if (event.getProperty(UIEvents.EventTags.ELEMENT) instanceof MPerspective) {
			refresh();
		}
	}

	private void refresh() {
		if (toolBar == null || toolBar.isDisposed()) {
			return;
		}
		for (ToolItem item : toolBar.getItems()) {
			item.dispose();
		}
		List<MPerspective> perspectives = modelService.findElements(window, null, MPerspective.class);
		for (MPerspective perspective : perspectives) {
			if (!perspective.isToBeRendered()) {
				continue;
			}
			ToolItem item = new ToolItem(toolBar, SWT.RADIO);
			item.setToolTipText(perspective.getLabel());
			if (perspective.getIconURI() != null) {
				item.setImage(resources.create(createDescriptor(perspective.getIconURI())));
			} else {
				item.setText(perspective.getLabel());
			}
			item.setSelection(isActive(perspective));
			item.addListener(SWT.Selection, _ -> {
				if (item.getSelection()) {
					partService.switchPerspective(perspective);
				}
			});
		}
		toolBar.getParent().layout(true, true);
	}

	private static boolean isActive(MPerspective perspective) {
		MUIElement parent = perspective.getParent();
		return parent instanceof MPerspectiveStack stack && stack.getSelectedElement() == perspective;
	}

	private static ImageDescriptor createDescriptor(String iconURI) {
		try {
			return ImageDescriptor.createFromURL(URI.create(iconURI).toURL());
		} catch (Exception _) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
}
