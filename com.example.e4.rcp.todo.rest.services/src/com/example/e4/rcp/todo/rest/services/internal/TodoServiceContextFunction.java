package com.example.e4.rcp.todo.rest.services.internal;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.osgi.service.component.annotations.Component;

import com.example.e4.rcp.todo.model.ITodoService;

@Component(service = IContextFunction.class, property = "service.context.key=com.example.e4.rcp.todo.model.ITodoService")
public class TodoServiceContextFunction implements IContextFunction {
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		ITodoService todoService = ContextInjectionFactory.make(MyRestfulTodoServiceImpl.class, context);

		MApplication app = context.get(MApplication.class);
		IEclipseContext appCtx = app.getContext();
		appCtx.set(ITodoService.class, todoService);

		return todoService;
	}
}
