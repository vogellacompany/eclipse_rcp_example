package com.example.e4.rcp.todo.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.example.e4.rcp.todo.i18n.Messages;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.parts.TodoDetailsPart;

public class TodoWizardPage1 extends WizardPage {

	private Todo todo;
	private Messages messages;

	public TodoWizardPage1(Todo todo, Messages messages) {
		super("page1");
		this.todo = todo;
		this.messages = messages;
		setTitle("New Todo");
		setDescription("Enter the todo data");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		// We reuse the part and
		// inject the values
		TodoDetailsPart part = new TodoDetailsPart();
		part.createControls(container, messages);
		part.setTodo(todo);
		
		setControl(container);
	}

	
}
