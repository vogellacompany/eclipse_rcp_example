package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Button;

public class TodoDetailsPart {
	private Text text;
	private Text text_1;

	@PostConstruct
	public void createControls(Composite parent) {
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginRight = 10;
		gl_parent.marginLeft = 10;
		gl_parent.horizontalSpacing = 10;
		gl_parent.marginWidth = 0;
		parent.setLayout(gl_parent);
		
		Label lblSummary = new Label(parent, SWT.NONE);
		lblSummary.setText("Summary");
		
		text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setText("Description");
		
		text_1 = new Text(parent, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_text_1.heightHint = 122;
		text_1.setLayoutData(gd_text_1);
		
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText("Due Date");
		
		DateTime dateTime = new DateTime(parent, SWT.BORDER);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		new Label(parent, SWT.NONE);
		
		Button btnDone = new Button(parent, SWT.CHECK);
		btnDone.setText("Done");
	}
}
