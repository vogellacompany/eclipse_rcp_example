package com.example.e4.rcp.todo.handlers;

import java.util.Date;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Shell;

import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.wizards.TodoWizard;
import com.example.e4.rcp.todo.wizards.TodoWizardPage1;
import com.example.e4.rcp.todo.wizards.TodoWizardPage2;

public class NewTodoHandler {
	@Execute
	public void execute(Shell shell, ITodoService model, IEclipseContext ctx) {
		// create new context
		IEclipseContext wizardCtx = ctx.createChild();
		
		// create todo and store in context
		// use -1 to indicate a not existing id
		Todo todo = new Todo(-1);
		todo.setDueDate(new Date());
		wizardCtx.set(Todo.class, todo);

		// create WizardPages via CIF
		TodoWizardPage1 page1 = ContextInjectionFactory.make(TodoWizardPage1.class, wizardCtx);
		wizardCtx.set(TodoWizardPage1.class, page1);
		// no context needed for the creation
		TodoWizardPage2 page2 = ContextInjectionFactory.make(TodoWizardPage2.class, null);
		wizardCtx.set(TodoWizardPage2.class, page2);
		
		TodoWizard wizard = ContextInjectionFactory.make(TodoWizard.class, wizardCtx);
		 
		WizardDialog dialog = new WizardDialog(shell, wizard);
		if (dialog.open()== WizardDialog.OK) {
			model.saveTodo(todo);
		}
	}
}
