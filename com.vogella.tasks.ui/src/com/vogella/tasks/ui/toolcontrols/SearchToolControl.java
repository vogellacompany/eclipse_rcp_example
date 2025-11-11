package com.vogella.tasks.ui.toolcontrols;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import jakarta.annotation.PostConstruct;

public class SearchToolControl {
	
	@PostConstruct
	public void createGui(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		Text text = new Text(comp, SWT.SEARCH | SWT.CANCEL | SWT.ICON_SEARCH | SWT.BORDER);
		text.setMessage("Search");
		GridDataFactory.fillDefaults().hint(130, SWT.DEFAULT).applyTo(text);
	}
}