package com.example.e4.rcp.todo.lifecycle;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import com.example.e4.rcp.todo.dialogs.PasswordDialog;

public class LifeCycleManager {

	// We add the nodePath in case you move the lifecycle handler to
	// another plug-in later
	@Inject
	@Preference(nodePath = "com.example.e4.rcp.todo", value = "user")
	private String user;

	@PostContextCreate
	public void postContextCreate(@Preference IEclipsePreferences prefs) {
		final Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);
		PasswordDialog dialog = new PasswordDialog(shell);
		if (user != null) {
			dialog.setUser(user);
		}
		if (dialog.open() != Window.OK) {
			// close the application
			System.exit(-1);
		} else {
			// Store the user values in the preferences
			prefs.put("user", dialog.getUser());
			try {
				prefs.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
		}
	}
}