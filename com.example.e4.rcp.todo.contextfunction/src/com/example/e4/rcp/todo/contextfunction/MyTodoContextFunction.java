package com.example.e4.rcp.todo.contextfunction;

import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;

import com.example.e4.rcp.todo.model.Todo;

public class MyTodoContextFunction implements IContextFunction {

	@Override
	public Object compute(IEclipseContext context, String key) {
		System.out.println("ContextFunction called");
		Object object = context.get("id");
		if (object!=null) {
			
		}
		return new Todo();
	}

}
