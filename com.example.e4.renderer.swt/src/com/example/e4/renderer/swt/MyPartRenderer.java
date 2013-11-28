package com.example.e4.renderer.swt;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class MyPartRenderer extends SWTPartRenderer {

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		final Composite mapComposite = new Composite((Composite) parent,
				SWT.NONE);
		System.out.println(parent.getClass());
		mapComposite.setLayout(new GridLayout(1, false));
		final Browser browser = new Browser(mapComposite, SWT.NONE);
		browser.setUrl("http://www.vogella.com");
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		browser.setLayoutData(data);
		return mapComposite;
	}
}
