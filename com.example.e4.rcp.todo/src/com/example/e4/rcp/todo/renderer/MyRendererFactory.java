package com.example.e4.rcp.todo.renderer;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

public class MyRendererFactory extends WorkbenchRendererFactory {
	private MyStackRenderer mapRenderer;

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {
		if (uiElement instanceof MPartStack) {
			if (mapRenderer == null) {
				mapRenderer = new MyStackRenderer();
				super.initRenderer(mapRenderer);
			}
			return mapRenderer;
		}
		return super.getRenderer(uiElement, parent);
	}
}
