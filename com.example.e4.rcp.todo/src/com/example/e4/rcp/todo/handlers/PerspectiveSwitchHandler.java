package com.example.e4.rcp.todo.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class PerspectiveSwitchHandler {
	@Execute
	public void switchPersepctive(MPerspective activePerspective,
			MApplication app, EPartService partService,
			EModelService modelService, 
			@Named("perspective_parameter") String perspectiveId) {
		 System.out.println(perspectiveId);
		List<MPerspective> perspectives = modelService.findElements(app, perspectiveId,
				MPerspective.class, null);
		// Assume you have only two perspectives and you always
		// switch between them
		if (perspectives.size()>0){
			partService.switchPerspective(perspectives.get(0));
		}
	}
}
