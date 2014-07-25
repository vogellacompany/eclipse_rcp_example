package com.example.e4.rcp.todo.wizards;

import javax.inject.Inject;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.example.e4.rcp.todo.i18n.Messages;
import com.example.e4.rcp.todo.model.Todo;

public class TodoWizard extends Wizard {

	private Todo todo;
	boolean finish = false;
	private Messages messages;
	
	@Inject
	public TodoWizard(Todo todo, Messages messages) {
		this.todo = todo;
		this.messages = messages;
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		addPage(new TodoWizardPage1(todo, messages));
		addPage(new TodoWizardPage2());
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public boolean canFinish() {
		return finish;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return super.getNextPage(page);
	}
	
	
	
}
