package com.vogella.tasks.ui.toolcontrols;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SearchToolControl {
	
	@PostConstruct
	public void createGui(Composite parent) {
		Text text = new Text(parent, SWT.SEARCH | SWT.CANCEL | SWT.BORDER);
		text.setMessage("Search");
		
	}
}