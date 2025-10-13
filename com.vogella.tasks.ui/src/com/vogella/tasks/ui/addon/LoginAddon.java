package com.vogella.tasks.ui.addon;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Lifecycle addon that shows a login dialog on application startup.
 * Can be bypassed by setting the system property "skipLogin" to "true".
 * 
 * This is useful for automated testing where login should be skipped.
 */
public class LoginAddon {
	
	private static final String SKIP_LOGIN_PROPERTY = "skipLogin";
	
	@Execute
	public void execute(Shell shell) {
		// Check if we should skip login (for testing)
		if (shouldSkipLogin()) {
			return;
		}
		
		// Show a simple login notification
		// In a real application, this would show a proper login dialog
		// For demonstration purposes, we just show an info dialog
		// that doesn't block the application
		if (shell != null && !shell.isDisposed()) {
			shell.getDisplay().asyncExec(() -> {
				if (!shell.isDisposed()) {
					MessageDialog.openInformation(shell, "Login", 
						"Welcome! (Set -DskipLogin=true to bypass this dialog in tests)");
				}
			});
		}
	}
	
	private boolean shouldSkipLogin() {
		return "true".equalsIgnoreCase(System.getProperty(SKIP_LOGIN_PROPERTY));
	}
}
