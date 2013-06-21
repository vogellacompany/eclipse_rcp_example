package com.example.e4.rcp.todo.service.internal;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

import com.example.e4.rcp.todo.model.ITodoService;

public class TodoServiceContextFunction extends ContextFunction {
	@Override
	public Object compute(IEclipseContext context) {
		System.out.println("Creating a new service");
		ITodoService model = ContextInjectionFactory.make(MyTodoServiceImpl.class,
				context);
		MApplication application = context.get(MApplication.class);
		IEclipseContext ctx = application.getContext();
		ctx.set(ITodoService.class, model);
		return model;
	}
}
