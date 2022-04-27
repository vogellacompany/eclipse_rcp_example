
package com.vogella.tasks.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

public class CopyHandler {
	@Execute
	public void execute() {
		System.out.println("Copy triggered from the overview part");
	}

}