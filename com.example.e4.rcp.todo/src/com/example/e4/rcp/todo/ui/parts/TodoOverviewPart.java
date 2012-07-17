package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.example.e4.rcp.todo.model.ITodoModel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TodoOverviewPart {

	private Button btnNewButton;
	private Label lblNewLabel;

	@PostConstruct
	public void createControls(Composite parent, ITodoModel model) {
		System.out.println(model.getTodos().size());
		parent.setLayout(new GridLayout(2, false));
		
		btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblNewLabel.setText("Number of Todo items: 7");
			}
		});
		
		btnNewButton.setText("Load Data");
		
		lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("Number of Todo Items: 0");
	}
	
	@Focus
	private void setFocus() {
		btnNewButton.setFocus();
	}
	
}
