package com.example.e4.rcp.todo.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;

import com.example.e4.rcp.todo.model.Todo;

public class EditHandler {
	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Todo todo) {
		System.out.println("Hello Good Morning");
		
	}
}
