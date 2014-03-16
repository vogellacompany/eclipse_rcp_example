package com.vogella.e4.selectionservice.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.vogella.e4.selectionservice.service.MySelectionService;

public class ListenerPart {

	private Label labelSelection;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		labelSelection = new Label(parent, SWT.NONE);
		labelSelection.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		labelSelection.setText("Initial test");
	}

	@Inject
	public void setSelection(
			@Optional @Named(MySelectionService.MYSELECTION) String selection) {
		if (labelSelection != null && !labelSelection.isDisposed()) {
			labelSelection.setText(selection);
		}
	}

}