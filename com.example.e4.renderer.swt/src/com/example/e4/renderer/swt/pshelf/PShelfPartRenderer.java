package com.example.e4.renderer.swt.pshelf;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer;
import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

public class PShelfPartRenderer extends ContributedPartRenderer {
	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (!(element instanceof MPart) || !(parent instanceof PShelf))
			return null;

		PShelf parentWidget = (PShelf) parent;
		Widget newWidget = null;
		final MPart part = (MPart) element;
		
		MElementContainer<MUIElement> parent2 = part.getParent();
		int indexOf = parent2.getChildren().indexOf(part);

		Composite body = parentWidget.getItems()[indexOf].getBody();

		final Composite newComposite = new Composite(body, SWT.NONE) {

			/**
			 * Field to determine whether we are currently in the midst of
			 * granting focus to the part.
			 */
			private boolean beingFocused = false;

			@Override
			public boolean setFocus() {
				if (!beingFocused) {
					try {
						// we are currently asking the part to take focus
						beingFocused = true;

						// delegate an attempt to set the focus here to the
						// part's implementation (if there is one)
						Object object = part.getObject();
						if (object != null && isEnabled()) {
							IPresentationEngine pe = part.getContext().get(IPresentationEngine.class);
							pe.focusGui(part);
							return true;
						}
						return super.setFocus();
					} finally {
						// we are done, unset our flag
						beingFocused = false;
					}
				}

				// already being focused, likely some strange recursive call,
				// just return
				return true;
			}
		};

		newComposite.setLayout(new FillLayout(SWT.VERTICAL));

		newWidget = newComposite;
		bindWidget(element, newWidget);

		// Create a context for this part
		IEclipseContext localContext = part.getContext();
		localContext.set(Composite.class, newComposite);

		IContributionFactory contributionFactory = localContext.get(IContributionFactory.class);
		Object newPart = contributionFactory.create(part.getContributionURI(), localContext);
		part.setObject(newPart);

		return newWidget;
	}
}
