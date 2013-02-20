package com.example.e4.rcp.todo.lifecycle;

import java.nio.channels.SeekableByteChannel;

import javax.management.modelmbean.InvalidTargetObjectTypeException;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.example.e4.rcp.todo.model.ITodoModel;

// For a extended example see
// https://bugs.eclipse.org/382224
public class LifeCycleManager {
	@PostContextCreate
	void postContextCreate(final IEventBroker broker, ITodoModel model) {
		// Inject the EventBroker into the ITodoModel
		model.setEventBroker(broker);
		
		final Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);
		final Display display = Display.getDefault();
		//center the dialog screen to the monitor
		Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);
		shell.setLocation(200, 200);
	
		// Add progressbar
		final ProgressBar bar = new ProgressBar (shell, SWT.SMOOTH);
		Rectangle clientArea = shell.getClientArea ();
		bar.setBounds (clientArea.x, clientArea.y, 200, 32);
		
		// Simulate some long running operation
		bar.setMaximum(4);
		sleep();
		bar.setSelection(1);
		sleep();
		bar.setSelection(3);
		
		// Will close the shell once a part gets activated
		// Should be easier
		// See the following bug reports
		// https://bugs.eclipse.org/376821
		broker.subscribe(UIEvents.UILifeCycle.ACTIVATE,
				new EventHandler() {
					@Override
					public void handleEvent(Event event) {
						shell.close();
						shell.dispose();
						System.out.println("Closed");
						broker.unsubscribe(this);
					}
				});
		shell.open();
	}

	private void sleep() {
		// TODO Auto-generated method stub
		
	}
}
