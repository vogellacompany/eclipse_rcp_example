package com.example.e4.rcp.todo.menu;

import java.util.Date;
import java.util.List;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class DynamicMenuEntries {
	private String bundleprefix = "bundleclass://com.example.e4.rcp.todo/";
	private String classname = "com.example.e4.rcp.todo.handlers.PrintMessageHandler";
	

	@AboutToShow
	public void aboutToShow(List<MMenuElement> items, EModelService modelService) {
		for (int i = 0; i < 10; i++) {
			MDirectMenuItem dynamicItem = modelService
					.createModelElement(MDirectMenuItem.class);
			dynamicItem.setLabel("Dynamic Menu Item (" + new Date() + ")");
			dynamicItem
					.setContributionURI(bundleprefix+classname);
			items.add(dynamicItem);
		}
	}
}
