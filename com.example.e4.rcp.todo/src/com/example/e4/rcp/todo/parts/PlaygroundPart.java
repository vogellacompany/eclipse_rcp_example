package com.example.e4.rcp.todo.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PlaygroundPart {
	private Text text;
	private Browser browser;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		text = new Text(parent, SWT.BORDER);
		text.setMessage("Enter City");
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button button = new Button(parent, SWT.PUSH);
		button.setText("Search");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Commented out for LINUX
//				String city = text.getText();
//				if (city.isEmpty()) {
//					return;
//				}
//				try {
//					browser.setUrl("https://www.google.com/maps/place/"
//							+ URLEncoder.encode(city, "UTF-8")
//							+ "/&output=embed");
//				} catch (UnsupportedEncodingException e1) {
//					e1.printStackTrace();
//				}
			}
		});
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
				false));
		label.setText("BROWSER CODE COMMENT out in PlaygroundPart.java to avoid problems with Linux. If you not using Linux please remove the comments in this class.");
		
		// Commented out for LINUX
//		browser = new Browser(parent, SWT.NONE);
//		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

	}

	@Focus
	public void onFocus() {
		text.setFocus();
	}
}