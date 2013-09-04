package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class DynamicPartHandlerCode {
	// Used as reference
	@Execute
	public void execute(MApplication application, EPartService partService,
			EModelService modelService) {

		// create new part
		MPart mPart = modelService.createModelElement(MPart.class);
		mPart.setLabel("Testing");
		mPart.setElementId("newid");
		mPart.setContributionURI("bundleclass://com.example.e4.rcp.todo/"
				+ "com.example.e4.rcp.todo.ui.parts.DynamicPart");
		partService.showPart(mPart, PartState.ACTIVATE);
	}
}
