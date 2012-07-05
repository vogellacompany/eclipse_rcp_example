package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TodoOverviewPart {
	// Declare a field label, required for @Focus
	Label label;
	@PostConstruct
	public void createControls(Composite parent) {
	label = new Label(parent,SWT.NONE);
	}

}
