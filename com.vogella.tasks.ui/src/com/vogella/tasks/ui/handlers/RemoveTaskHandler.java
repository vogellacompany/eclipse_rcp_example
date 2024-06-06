
package com.vogella.tasks.ui.handlers;

import java.util.List;

import jakarta.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;

public class RemoveTaskHandler {
	@Execute
	public void execute(TaskService taskService, @Named(IServiceConstants.ACTIVE_SELECTION) List<Task> tasks) {
		if (!tasks.isEmpty()) {
			taskService.delete(tasks.get(0).getId());
		}
	}

	@CanExecute
	public boolean execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Task> tasks) {
		if (tasks == null || tasks.isEmpty()) {
			return false;
		}
		return true;
	}

}