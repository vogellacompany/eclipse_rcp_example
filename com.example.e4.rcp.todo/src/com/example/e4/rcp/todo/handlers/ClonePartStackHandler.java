package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class ClonePartStackHandler {
	@Execute
	public void execute(EModelService modelService, MApplication app) {
		
		MPartSashContainer partSash = (MPartSashContainer) modelService.find("com.example.e4.rcp.todo.partsashcontainer.left", app);
		MPartStack element = (MPartStack) modelService.cloneSnippet(app, "com.example.e4.rcp.todo.partstack.snippet", null);
		partSash.getChildren().add(element);
	} 
}
