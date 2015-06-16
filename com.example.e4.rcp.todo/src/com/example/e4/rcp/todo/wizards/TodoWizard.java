package com.example.e4.rcp.todo.wizards;

import javax.inject.Inject;

import org.eclipse.jface.wizard.Wizard;

public class TodoWizard extends Wizard {

	@Inject
	TodoWizardPage1 page1;
	@Inject
	TodoWizardPage2 page2;
	
	
	@Inject
	public TodoWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		addPage(page1);
		addPage(page2);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
}
