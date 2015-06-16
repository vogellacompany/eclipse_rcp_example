 
package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class RestorePerspectiveHandler {

	@Execute
	public void execute(EModelService modelService, MWindow window,
			EPartService partService) {
		MPerspective activePerspective = modelService
				.getActivePerspective(window);
		MUIElement findSnippet = modelService.findSnippet(window,
				activePerspective.getElementId());
		MElementContainer<MUIElement> parent = activePerspective.getParent();
		modelService.removePerspectiveModel(activePerspective, window);
		parent.getChildren().add(findSnippet);
		partService.switchPerspective((MPerspective) findSnippet);
	}
	
	
	@CanExecute
	public boolean canExecute(EModelService modelService, MWindow window) {
		MPerspective activePerspective = modelService
				.getActivePerspective(window);
		return modelService.findSnippet(window,
				activePerspective.getElementId()) != null;
	}
		
}