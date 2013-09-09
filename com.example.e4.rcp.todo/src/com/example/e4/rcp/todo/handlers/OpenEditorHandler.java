package com.example.e4.rcp.todo.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import com.example.e4.rcp.todo.model.Todo;

public class OpenEditorHandler {
	@Execute
	public void execute(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Todo todo,
			MApplication application, EModelService modelService,
			EPartService partService) {

		// sanity check
		if (todo == null) {
			return;
		}
		String id = String.valueOf(todo.getId());

		// maybe the editor is already open?
		List<MPart> parts = (List<MPart>) partService.getParts();

		// if the editor is open show it
		for (MPart mPart : parts) {
			String currentId = mPart.getPersistedState().get(Todo.FIELD_ID);
			if (currentId != null && currentId.equals(id)) {
				partService.showPart(mPart, PartState.ACTIVATE);
				return;
			}
		}
		
		// editor was not open, create it and show it

		// create an Input part
		MPart part = modelService.createModelElement(MPart.class);

		// Pointing to the contributing class
		part.setContributionURI("bundleclass://com.example.e4.rcp.todo/com.example.e4.rcp.todo.ui.parts.EditorPart");
		part.getPersistedState().put(Todo.FIELD_ID, id);

		// create a nice label for the part header
		String header = "ID:" + id + " " + todo.getSummary();
		part.setLabel(header);
		part.setElementId(id);
		part.setCloseable(true);

		// add it an existing stack
		MPartStack stack = (MPartStack) modelService.find(
				"com.example.e4.rcp.todo.partstack.bottom", application);
		stack.getChildren().add(part);
		partService.showPart(part, PartState.ACTIVATE);

	}
}
