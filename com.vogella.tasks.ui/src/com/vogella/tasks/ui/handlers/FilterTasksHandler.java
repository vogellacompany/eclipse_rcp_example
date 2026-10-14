package com.vogella.tasks.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.vogella.tasks.ui.parts.TaskStateFilter;

import jakarta.inject.Named;

public class FilterTasksHandler {
	@Execute
	public void execute(@Named("com.vogella.tasks.ui.commandparameter.filter") String filter,
			@Named(IServiceConstants.ACTIVE_PART) MPart part) {
		part.getContext().set(TaskStateFilter.CONTEXT_KEY, filter);
	}
}
