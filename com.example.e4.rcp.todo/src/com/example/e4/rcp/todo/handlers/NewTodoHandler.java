package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.wizards.TodoWizard;

public class NewTodoHandler {
	@Execute
	public void execute(Shell shell, ITodoModel model) {
		Todo todo = new Todo();
		WizardDialog dialog = new WizardDialog(shell, new TodoWizard(todo));
		dialog.open();
	}

}
