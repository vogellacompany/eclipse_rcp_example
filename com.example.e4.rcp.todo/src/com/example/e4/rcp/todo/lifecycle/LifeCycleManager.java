package com.example.e4.rcp.todo.lifecycle;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

// For a extended example see
// https://bugs.eclipse.org/382224
public class LifeCycleManager {
	@PostContextCreate
	void postContextCreate(final IEventBroker eventBroker) {
		final Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);
		// Will close the shell once a part gets activated
		// Should be easier
		// See the following bug reports
		// https://bugs.eclipse.org/376821
		eventBroker.subscribe(UIEvents.UILifeCycle.ACTIVATE,
				new EventHandler() {
					@Override
					public void handleEvent(Event event) {
						shell.close();
						shell.dispose();
						System.out.println("Closed");
						eventBroker.unsubscribe(this);
					}
				});
		shell.open();
	}
}
