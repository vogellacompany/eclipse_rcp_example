package com.vogella.e4.todo.contribute;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;

@SuppressWarnings("restriction")
public class MenuProcessor {
	
	// I get this via the parameter 
	// of the process definition
	@Inject
	@Named("org.eclipse.ui.file.menu")
	private MMenu menu;
	@Execute
	public void execute() {
//		System.out.println("Starting processor");
		// Remove the old exit menu entry
		if (menu != null && menu.getChildren() != null) {
			List<MMenuElement> list = new ArrayList<MMenuElement>();
			for (MMenuElement element : menu.getChildren()) {
				System.out.println(element);
				
				// Separaters have no label hence we
				// need to check for null
				if (element.getLabel() != null) {
					if (element.getLabel().contains("Exit")) {
						list.add(element);
					}
				}
			}
			menu.getChildren().removeAll(list);
		}

		// Now add a new menu entry
		MDirectMenuItem menuItem = MMenuFactory.INSTANCE.
				createDirectMenuItem();
		menuItem.setLabel("Another Exit");
		menuItem.setContributionURI("bundleclass://"
				+ "com.example.e4.rcp.todo.contribute/"
				+ "de.vogella.e4.todo.contribute.handler.ExitHandlerWithCheck");
		menu.getChildren().add(menuItem);
	}
}
