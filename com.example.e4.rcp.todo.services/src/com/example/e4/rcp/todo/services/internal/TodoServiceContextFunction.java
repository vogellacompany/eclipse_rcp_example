package com.example.e4.rcp.todo.services.internal;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.osgi.service.component.annotations.Component;

import com.example.e4.rcp.todo.model.ITodoService;

@Component(service = IContextFunction.class, property = "service.context.key=com.example.e4.rcp.todo.model.ITodoService")
public class TodoServiceContextFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		ITodoService impl = ContextInjectionFactory.make(MyTodoServiceImpl.class, context);
		MApplication application = context.get(MApplication.class);
		application.getContext().set(ITodoService.class, impl);
		return impl;
	}
}












