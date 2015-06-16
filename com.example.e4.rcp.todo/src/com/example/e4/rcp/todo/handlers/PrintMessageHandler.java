package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

public class PrintMessageHandler {
	
	@Execute
	public void printMessage() {
		System.out.println("message printed....");
	}
}
