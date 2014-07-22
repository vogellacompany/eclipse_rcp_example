package com.example.e4.rcp.todo.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ShowErrorDialogHandler {
	static String lineSeparator = System.getProperty("line.separator");

	@Execute
	public void execute(final Shell shell, MWindow window) {

		// create exception on purpose to demonstrate ErrorDialog
		try {
			String s = null;
			System.out.println(s.length());
		} catch (NullPointerException e) {
			MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
			ErrorDialog.openError(shell, "Error", "This is an error", status);
		}
	}

	// helper method to show the stack trace in the detailed message
	private static MultiStatus createMultiStatus(String msg, Throwable t) {

		List<Status> childStatuses = new ArrayList<Status>();

		for (String line : t.getStackTrace().toString().split(lineSeparator)) {
			Status status = new Status(IStatus.ERROR,
					"com.example.e4.rcp.todo", line);
			childStatuses.add(status);
		}

		MultiStatus ms = new MultiStatus("com.example.e4.rcp.todo",
				IStatus.ERROR, childStatuses.toArray(new Status[] {}),
				t.getLocalizedMessage(), t);
		return ms;
	}
}
