package com.vogella.tasks.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class RestartHandler {

	@Execute
	public void execute(IWorkbench workbench, EPartService partService) {
		if (partService.saveAll(true)) {
			workbench.restart();
		}
	}
}
