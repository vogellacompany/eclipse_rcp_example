package com.example.e4.rcp.todo.creatable;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import com.example.e4.rcp.todo.model.ITodoService;

@Creatable
public class YourObject {
	// constructor
	@Inject
	public YourObject(Dependent depend, ITodoService service) {
		// placeholder
	}
}
