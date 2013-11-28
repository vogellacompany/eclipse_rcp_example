package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ExitHandler {
	@Execute
	public void execute(EPartService partService,
			IWorkbench workbench, Shell shell) {
		if (!partService.getDirtyParts().isEmpty()) {
			boolean confirm = MessageDialog.openConfirm(shell, "Unsaved",
					"Unsaved data, do you want to save?");
			if (confirm) {
				partService.saveAll(false);
				// Ok we close here directy to avoid 
				// second popup
				workbench.close();
			}
		}

		boolean result = MessageDialog.openConfirm(shell, "Close",
				"Close application?");
		if (result) {
			workbench.close();
		}

	}
}
