package com.vogella.tasks.ui.wizards;

import org.eclipse.jface.widgets.WidgetFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CreateTaskPage2 extends WizardPage {

    public CreateTaskPage2() {
        super("wizardPage");
        setTitle("Validate");
        setDescription("Check to create the task item");
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, true);
        container.setLayout(layout);
        Button button = WidgetFactory.button(SWT.CHECK).text("Data has been validated").create(container);
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