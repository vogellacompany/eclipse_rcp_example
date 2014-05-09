package com.example.e4.rcp.todo.handlers;


import java.util.Iterator;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationElement;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.Selector;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class FindAllToolbarsHandler {

	@Execute
	public void execute(EModelService modelService, MApplication application) {
		 List<MToolBar> toolbarElements = modelService.findElements(application,
					MToolBar.class, 
					EModelService.ANYWHERE | EModelService.IN_PART, new Selector() {
						@Override
						public boolean select(MApplicationElement element) {
							return (element instanceof MToolBar);
						}
					});
		 System.out.println(toolbarElements.size());
		 for (MToolBar mToolBar : toolbarElements) {
			 System.out.println(mToolBar);
		}
		
	}

}
