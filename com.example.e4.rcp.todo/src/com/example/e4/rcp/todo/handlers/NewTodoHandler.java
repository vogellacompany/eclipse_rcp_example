package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.wizards.TodoWizard;

public class NewTodoHandler {
	@Execute
	public void execute(Shell shell, ITodoModel model, IEventBroker broker ) {
		Todo todo = new Todo();
		todo.setSummary("Databinding is the best ever!!");
		WizardDialog dialog = new WizardDialog(shell, new TodoWizard(todo));
		if (dialog.open()== WizardDialog.OK) {
			model.saveTodo(todo);
			// asynchronously
			broker.post(MyEventConstants.TOPIC_TODO_DATA_UPDATE_NEW, "New data");
		}
	}
}
