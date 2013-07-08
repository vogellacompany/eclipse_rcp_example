package com.example.e4.renderer.swt;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

public class MyRendererFactory extends WorkbenchRendererFactory {

	private MyStackRenderer stackRenderer;
	private MyPartRenderer partRenderer;

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {

		if (uiElement instanceof MPart) {
			if (partRenderer == null) {
				partRenderer = new MyPartRenderer();
				super.initRenderer(partRenderer);
			}
			return partRenderer;
		} else
		if (uiElement instanceof MPartStack) {
			if (stackRenderer == null) {
				stackRenderer = new MyStackRenderer();
				super.initRenderer(stackRenderer);
			}
			return stackRenderer;
		}
		return super.getRenderer(uiElement, parent);

	}

}
