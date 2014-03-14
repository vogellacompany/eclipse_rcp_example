package com.vogella.e4.selectionservice.service;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;

public class MySelectionService {

	@Inject
	IEclipseContext ctx;

	public void setSelection(Object obj) {
		ctx.set("myselection", obj);
	}
}
