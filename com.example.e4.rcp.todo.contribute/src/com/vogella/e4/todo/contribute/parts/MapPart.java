package com.vogella.e4.todo.contribute.parts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

public class MapPart {
	private Text text;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		try {
			new Label(parent, SWT.NONE);
			
			text = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.SEARCH | SWT.CANCEL);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			new Label(parent, SWT.NONE);
			Browser browser = new Browser(parent, SWT.NONE);
			browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			browser.setUrl("http://maps.google.de/maps?q="
					+ URLEncoder.encode("Hamburg", "UTF-8")
					+ "&output=embed");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
}
