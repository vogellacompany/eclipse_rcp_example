package com.example.e4.rcp.todo.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class DynamicPartHandler {
	// Used as reference
	private final String STACK_ID = "com.example.e4.rcp.todo.partstack.bottom";
	@Execute
	public void execute(MApplication application, EPartService partService, EModelService modelService, Shell shell) {
		// create part based on part descriptor
		MPart part = partService.createPart("com.example.e4.rcp.todo.partdescriptor.dynamic");

		// search for the part stack to add the part to
		// this means you must have a part stack with in ID in your application model
		List<MPartStack> stacks = modelService.findElements(application, STACK_ID,
				MPartStack.class, null);
		if (stacks.size() < 1) {
			MessageDialog.openError(shell, "Error ", 
					"Part stack not found. Is the following ID correct?" + STACK_ID);
			return;
		} 
		stacks.get(0).getChildren().add(part);
		// activates the part
		partService.showPart(part, PartState.ACTIVATE);
	}
}
