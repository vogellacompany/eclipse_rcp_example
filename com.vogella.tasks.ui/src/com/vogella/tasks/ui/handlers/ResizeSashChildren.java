 package com.vogella.tasks.ui.handlers;

import org.eclipse.core.runtime.ILog;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;


public class ResizeSashChildren {
	
	@Execute
	public void execute(EModelService modelService, MWindow window) {
		// this code assume you main sash container uses the com.vogella.tasks.ui.partsashcontainer.main id
		MUIElement muiElement = modelService.find("com.vogella.tasks.ui.partsashcontainer.main", window);
		if (muiElement == null) {
			ILog.get().error("Could not find the main part sash container, check that you use the correct ID in your model");
			return;
		}
		if (muiElement instanceof MPartSashContainer container) {
			// we only handle the case in which we have two direct children
			if (container.getChildren().size()==2) {
				container.getChildren().get(0).setContainerData("300");
				container.getChildren().get(1).setContainerData("700");
			}
		}
	}
		
}