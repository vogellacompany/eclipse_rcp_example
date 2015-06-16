package com.vogella.e4.appmodel.renderer;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

import extensions.BrowserPart;

public class MyRendererFactory extends WorkbenchRendererFactory {

	private BrowserPartRenderer browserPartRenderer;

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {

		if (uiElement instanceof BrowserPart) {
			if (browserPartRenderer == null) {
				browserPartRenderer = new BrowserPartRenderer();
				super.initRenderer(browserPartRenderer);
			}
			return browserPartRenderer;
		}

		return super.getRenderer(uiElement, parent);
	}

}
