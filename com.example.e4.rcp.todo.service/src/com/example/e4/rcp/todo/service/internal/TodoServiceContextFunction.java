package com.example.e4.rcp.todo.service.internal;
 
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
 
import com.example.e4.rcp.todo.model.ITodoService;
 
public class TodoServiceContextFunction extends
		org.eclipse.e4.core.contexts.ContextFunction {
 
	@Override
	public Object compute(IEclipseContext context) {
		System.out.println("Creating a new service");
		ITodoService service = ContextInjectionFactory.make(
				MyTodoServiceImpl.class, context);
		MApplication application = context.get(MApplication.class);
		IEclipseContext ctx = application.getContext();
		ctx.set(ITodoService.class, service);
		return service;
	}
}