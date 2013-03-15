package com.example.e4.rcp.todo.lifecycle;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.example.e4.rcp.todo.dialogs.PasswordDialog;
import com.example.e4.rcp.todo.model.ITodoModel;

// For a extended example see
// https://bugs.eclipse.org/382224
public class LifeCycleManager {
	@PostContextCreate
	void postContextCreate() {
		// Inject the EventBroker into the ITodoModel
		final Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);
		PasswordDialog dialog = new PasswordDialog(shell);
		if (dialog.open()!=Window.OK){
			// close the application
			System.exit(-1);
		};
		
		// Will close the shell once a part gets activated
		// Should be easier
		// See the following bug reports
		// https://bugs.eclipse.org/376821
//		broker.subscribe(UIEvents.UILifeCycle.ACTIVATE,
//				new EventHandler() {
//					@Override
//					public void handleEvent(Event event) {
//						shell.close();
//						shell.dispose();
//						broker.unsubscribe(this);
//					}
//				});
	}

}
