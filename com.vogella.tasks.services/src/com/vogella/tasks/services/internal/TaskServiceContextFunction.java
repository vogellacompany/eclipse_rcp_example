package com.vogella.tasks.services.internal;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.osgi.service.component.annotations.Component;

import com.vogella.tasks.model.TaskService;

// TODO register this thing for the context key com.vogella.tasks.model.TaskService

@Component(service = IContextFunction.class, property = "service.context.key=com.vogella.tasks.model.TaskService")
public class TaskServiceContextFunction extends ContextFunction {

	
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		var make = ContextInjectionFactory.make(TransientTaskServiceImpl.class, context);
		MApplication mApplication = context.get(MApplication.class);
		mApplication.getContext().set(TaskService.class, make);
		return make;
	}
}
