package com.example.e4.rcp.todo.wizards;

import javax.inject.Inject;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.example.e4.rcp.todo.i18n.Messages;
import com.example.e4.rcp.todo.model.Todo;

public class TodoWizard extends Wizard {

	boolean finish = false;
	
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

	@Override
	public boolean canFinish() {
		return finish;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return super.getNextPage(page);
	}
	
	
	
}
