package com.vogella.e4.selectionservice.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class SwitchPerspectiveHandler {
	@Execute
	public void execute(MWindow window, EPartService partService,
			EModelService modelService) {
		MPerspective first = (MPerspective) modelService.find(
				"com.vogella.e4.selectionservice.perspective.first", window);
		// now switch perspective
		MPerspective second = (MPerspective) modelService.find(
				"com.vogella.e4.selectionservice.perspective.second", window);
		// now switch perspective
		MPerspective activePerspective = modelService.getActivePerspective(window);
		if (activePerspective.equals(second)){
			partService.switchPerspective(first);
		} else {
			partService.switchPerspective(second);
		}
		
		
	}
}