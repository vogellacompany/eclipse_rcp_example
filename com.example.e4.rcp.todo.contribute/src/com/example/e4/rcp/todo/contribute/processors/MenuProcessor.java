package com.example.e4.rcp.todo.contribute.processors;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.example.e4.rcp.todo.contribute.handlers.ExitHandlerWithCheck;

public class MenuProcessor {

	// the menu is injected based on the parameter
	// defined in the extension point
	@Inject
	@Named("org.eclipse.ui.file.menu")
	private MMenu menu;

	@Execute
	public void execute(EModelService modelService) {
		// remove the old exit menu entry
		if (!menu.getChildren().isEmpty()) {
			List<MMenuElement> list = new ArrayList<>();
			for (MMenuElement element : menu.getChildren()) {
				// use ID instead of label as label is later translated
				if (element.getElementId() != null) {
					if (element.getElementId().contains("exit")) {
						list.add(element);
					}
				}
			}
			menu.getChildren().removeAll(list);
		}

		// now add a new menu entry
		MDirectMenuItem menuItem = modelService.createModelElement(MDirectMenuItem.class);
		menuItem.setLabel("Another Exit");
		menuItem.setContributionURI("bundleclass://"
				+ "com.example.e4.rcp.todo.contribute/"
				+ ExitHandlerWithCheck.class.getName());
		menu.getChildren().add(menuItem);
	}
}
