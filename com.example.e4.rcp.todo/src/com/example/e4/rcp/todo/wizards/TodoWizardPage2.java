package com.example.e4.rcp.todo.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class TodoWizardPage2 extends WizardPage {

	public TodoWizardPage2() {
		super("wizardPage");
		setTitle("Validate");
		setDescription("Check to create the todo item");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(1, true);
		container.setLayout(layout);
		final Button button = new Button(container, SWT.CHECK);
		button.setText("Todo data has been valided");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageComplete(button.getSelection());
			}
		});
		setPageComplete(false);
		setControl(container);
	}

}
