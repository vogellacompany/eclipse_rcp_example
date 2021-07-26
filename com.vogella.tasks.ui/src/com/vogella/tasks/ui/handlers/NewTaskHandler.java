package com.vogella.tasks.ui.handlers;

import java.time.LocalDate;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.vogella.tasks.model.Task;
import com.vogella.tasks.model.TaskService;
import com.vogella.tasks.ui.wizards.TaskWizard;

public class NewTaskHandler {
    @Execute
	public void execute(Shell shell, TaskService taskService, IEventBroker broker) {
        // use -1 to indicate a not existing id
        Task task = new Task(-1);
        task.setDueDate(LocalDate.now());
		var dialog = new WizardDialog(shell, new TaskWizard(task));
        if (dialog.open() == Window.OK) {
            // call service to save task object
            taskService.update(task);
        }

    }
}