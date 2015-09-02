package com.example.e4.rcp.todo.wizards;

import javax.inject.Inject;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.example.e4.rcp.todo.i18n.MessagesRegistry;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.parts.TodoDetailsPart;

public class TodoWizardPage1 extends WizardPage {

	
	private Todo todo;
	private MessagesRegistry messagesRegistry;
	
	@Inject
	public TodoWizardPage1(Todo todo, MessagesRegistry messagesRegistry) {
		super("page1");
		this.todo = todo;
		this.messagesRegistry = messagesRegistry;
		setTitle("New Todo");
		setDescription("Enter the todo data");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		// we could also create this class via DI but 
		// in this example we stay with the next operator
		TodoDetailsPart part = new TodoDetailsPart();
		part.createControls(container, messagesRegistry);
		part.setTodo(todo);
		
		setControl(container);
	}

	
}
