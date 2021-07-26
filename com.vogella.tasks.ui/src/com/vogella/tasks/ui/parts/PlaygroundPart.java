package com.vogella.tasks.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class PlaygroundPart {
	private Text text;
	private Browser browser;
	private Text target;


	@Inject
	MPart part;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));


		Text modelWidget = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		modelWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		modelWidget.setText("Moin");
		modelWidget.addModifyListener(e -> {
			part.setDirty(true);
			System.out.println("This must be saved by the user!!!");
		});

		
	}

	@Persist
	public void saveItReallyReally() {
		// TODO really do the saving
		part.setDirty(false);
	}


}