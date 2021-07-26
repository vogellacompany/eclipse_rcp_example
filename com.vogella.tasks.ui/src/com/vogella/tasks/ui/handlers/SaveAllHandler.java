 
package com.vogella.tasks.ui.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class SaveAllHandler {
	@Execute
	public void execute(EPartService partService) {
		partService.saveAll(false);
	}

	@CanExecute
	public boolean canSave(EPartService partService) {
		return !partService.getDirtyParts().isEmpty();
	}
		
}