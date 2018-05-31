package com.example.e4.rcp.todo.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.example.e4.rcp.todo.model.ITodoService;
import com.example.e4.rcp.todo.model.Todo;

public class RemoveTodoHandler {
	@Execute
	public void execute(
			ITodoService todoService,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Todo> todos) {
		if (todos != null) {
			todos.forEach(t -> todoService.deleteTodo(t.getId()));
		} 
	}
	
	@CanExecute
	public boolean canIt(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) List<Todo> todos) {
		if (todos==null) {
			return false;
		}
		return todos.size()>0;
	}
}
