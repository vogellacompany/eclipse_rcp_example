
package com.vogella.tasks.ui.handlers;

import jakarta.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class SwitchPerspectiveHandler {
	@Execute
	public void execute(EPartService partService,
			@Named("com.vogella.tasks.ui.commandparameter.perspectiveid") String perspectiveID) {
		partService.switchPerspective(perspectiveID);
	}

}