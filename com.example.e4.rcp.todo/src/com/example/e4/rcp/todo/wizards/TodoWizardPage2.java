package com.example.e4.rcp.todo.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TodoWizardPage2 extends WizardPage {

	private boolean checked = false;

	/**
	 * Create the wizard.
	 */
	public TodoWizardPage2() {
		super("wizardPage");
		setTitle("Validate");
		setDescription("Check to create the todo item");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, true);
		container.setLayout(layout);
		Label label = new Label(container, SWT.NONE);
		label.setText("Create the todo");
		Button button = new Button(container, SWT.CHECK);
		button.setText("Check");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checked = ((Button) e.getSource()).getSelection();
				TodoWizard wizard = (TodoWizard) getWizard();
				wizard.finish = checked;
				// the following updates the button
				// it calls anFinish() in the wizard
				wizard.getContainer().updateButtons();
			}
		});
		setControl(container);
	}

	public boolean isChecked() {
		return checked;
	}

}
