package com.example.e4.rcp.todo.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class RemoveTodoHandler {
	@Execute
	public void execute(ITodoModel model, @Named(IServiceConstants.ACTIVE_SELECTION) Todo todo) {
		model.deleteTodo(todo.getId());
	}

}
