package com.example.e4.rcp.todo.renderer;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

public class MyRendererFactory extends WorkbenchRendererFactory {
	private BrowserRenderer mapRenderer;

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {
		if (uiElement instanceof MPart) {
			if (mapRenderer == null) {
				mapRenderer = new BrowserRenderer();
				super.initRenderer(mapRenderer);
			}
			return mapRenderer;
		}
		return super.getRenderer(uiElement, parent);
	}
}
