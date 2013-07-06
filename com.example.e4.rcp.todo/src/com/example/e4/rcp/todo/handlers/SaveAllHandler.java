package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.example.e4.rcp.todo.events.MyEventConstants;

public class SaveAllHandler {
	@Execute
	public void execute(EPartService service, IEventBroker broker) {
		service.saveAll(false);
		broker.post(MyEventConstants.TOPIC_TODO_UPDATE, "saved");
	}

	@CanExecute
	boolean canExecute(@Optional MWindow window) {
		if (window != null) {
			IEclipseContext context = window.getContext();
			if (context != null) {
				EPartService partService = context.get(EPartService.class);
				if (partService != null) {
					return !partService.getDirtyParts().isEmpty();
				}
			}
		}
		return false;
	}

}
