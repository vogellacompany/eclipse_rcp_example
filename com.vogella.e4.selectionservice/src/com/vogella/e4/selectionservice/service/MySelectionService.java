package com.vogella.e4.selectionservice.service;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;

public class MySelectionService {

	public static final String MYSELECTION = "myselection";
	@Inject
	IEclipseContext ctx;

	public void setSelection(Object obj) {
		ctx.set(MYSELECTION, obj);
	}
}
