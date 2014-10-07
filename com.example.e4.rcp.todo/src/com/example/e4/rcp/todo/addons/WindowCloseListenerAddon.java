package com.example.e4.rcp.todo.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;

// @PostConstruct will not work as workbench gets instantiated after the processing of the add-ons
// hence this approach uses method injection

public class WindowCloseListenerAddon {

	@Inject
	@Optional
	MApplication application;
	
	@Inject
	@Optional
	EPartService partService;
	
	@Inject
	@Optional
	IWorkbench workbench;
	
	
	@Inject
	@Optional
	private void subscribeApplicationCompleted(@UIEventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {
		if (application!=null && partService != null &&workbench !=null ){
			registerCloseHandler(partService, workbench, application);
		}
	}
	@Inject
	@Optional
	private void subscribeTopicChildrenChanged(@UIEventTopic(UIEvents.ElementContainer.TOPIC_CHILDREN) Event event) {
		Object changedObj = event.getProperty(UIEvents.EventTags.ELEMENT);
		System.out.println(changedObj);
		
		if (!(changedObj instanceof MApplication)) {
			return;
		}
		
		MApplication application = (MApplication) changedObj;
		if (application!=null && partService != null && workbench !=null ){
			registerCloseHandler(partService, workbench, application);
		}
		
		// only interested in changes to application
		
		

		
	}

	private void registerCloseHandler(final EPartService partService, final IWorkbench workbench,
			MApplication application) {
		// each window gets its own close handler
		for (MWindow window : application.getChildren()) {

			final Shell shell = (Shell) window.getWidget();
			
			IWindowCloseHandler closeHandler = new IWindowCloseHandler() {

				@Override
				public boolean close(MWindow window) {
					if (partService.getDirtyParts().size() > 0) {
						boolean close = MessageDialog.openConfirm(shell, "Unsaved data", "Really close?");
						if (close) {
							workbench.close();
						}
						return close;
					}
					return true;
				}
			};
			window.getContext().set(IWindowCloseHandler.class, closeHandler);

		}
	}
}
