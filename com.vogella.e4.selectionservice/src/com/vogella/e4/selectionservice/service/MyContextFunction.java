package com.vogella.e4.selectionservice.service;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

public class MyContextFunction extends ContextFunction {
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		MPerspective mPerspective = context.get(MPerspective.class);

		// no perspective found
		if (mPerspective == null) {
			System.out.println("Null set to the context");
			return null;
		}

		IEclipseContext ctx = mPerspective.getContext();
		MySelectionService service = ContextInjectionFactory.make(
				MySelectionService.class, ctx);
		ctx.set(MySelectionService.class.getName(),service);

		return service;
	}
}
