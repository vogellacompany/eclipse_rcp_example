package com.example.e4.rcp.todo.ownannotation.internal;

import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.eclipse.e4.core.di.suppliers.IObjectDescriptor;
import org.eclipse.e4.core.di.suppliers.IRequestor;

import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.ownannotation.DirectTodo;

public class DirectTodoObjectSupplier extends ExtendedObjectSupplier {
	
	@Inject
	private ITodoService todoService;
	
	@Override
	public Object get(IObjectDescriptor descriptor, IRequestor requestor, boolean track, boolean group) {
		// get the DirectTodo from the IObjectDescriptor 
		DirectTodo uniqueTodo = descriptor.getQualifier(DirectTodo.class);
		
		// get the id from the DirectTodo (default is 0)
		long id = uniqueTodo.id();
		
		// get the Todo, which has the given id or null, if there is no Todo with the given id
		Optional<Todo> todo = todoService.getTodo(id);
		
		return todo;
	}
}