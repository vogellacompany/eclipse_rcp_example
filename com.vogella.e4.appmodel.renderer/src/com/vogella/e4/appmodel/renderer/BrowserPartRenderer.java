package com.vogella.e4.appmodel.renderer;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import extensions.BrowserPart;

public class BrowserPartRenderer extends SWTPartRenderer {

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		BrowserPart part = (BrowserPart) element;
		String uri = part.getUri();
		final Composite mapComposite = new Composite((Composite) parent,
				SWT.NONE);
		System.out.println(parent.getClass());
		mapComposite.setLayout(new GridLayout(1, false));
		final Browser browser = new Browser(mapComposite, SWT.NONE);
		browser.setUrl(uri);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		browser.setLayoutData(data);
		return mapComposite;
	}
}