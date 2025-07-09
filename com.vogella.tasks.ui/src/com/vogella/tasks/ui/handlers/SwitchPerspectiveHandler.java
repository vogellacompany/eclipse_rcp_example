
package com.vogella.tasks.ui.handlers;

import org.eclipse.core.runtime.ILog;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import jakarta.inject.Named;

public class SwitchPerspectiveHandler {
	@Execute
	public void execute(EPartService partService, @Optional
			@Named("perspective_parameter") String perspectiveID) {
		if (partService==null) {
			ILog.get().error("No perspective ID received");
		}
		partService.switchPerspective(perspectiveID);
	}

}