package com.vogella.e4.selectionservice.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class SwitchPerspectiveHandler {
	@Execute
	public void execute(MWindow window, EPartService partService,
			EModelService modelService) {
		// assumes you have only two perspectives
		List<MPerspective> perspectives = modelService.findElements(window,
				null, MPerspective.class, null);
		if (perspectives.size() != 2) {
			System.out.println("works only for exactly two perspectives");
		}
		
		MPerspective activePerspective = modelService
				.getActivePerspective(window);
		if (activePerspective.equals(perspectives.get(0))) {
			partService.switchPerspective(perspectives.get(1));
		} else {
			partService.switchPerspective(perspectives.get(0));
		}
	}
}