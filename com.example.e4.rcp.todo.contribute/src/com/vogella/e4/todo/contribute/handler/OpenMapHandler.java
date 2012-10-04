package com.vogella.e4.todo.contribute.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class OpenMapHandler {
	@Execute
	public void run(Shell shell) {
		MessageDialog.openInformation(shell, "Just testing", "Test");
	}
}
