package com.example.e4.filebrowser.services;

import java.net.URI;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class EditorService {

	public void createPart(MApplication application, EPartService partService,
			EModelService modelService, URI uri) {
		MUIElement find = modelService.find("editorarea", application);
		MPart part = partService
				.createPart("com.example.e4.rcp.todo.partdescriptor.fileeditor");
		part.setLabel("New Dynamic Part");

		// If multiple parts of this type is allowed
		// in the application model,
		// then the provided part will be shown
		// and returned
		partService.showPart(part, PartState.ACTIVATE);
	}
}
