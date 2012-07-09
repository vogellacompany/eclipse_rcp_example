
package de.vogella.e4.todo.contribute.handler;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.window.Window;

import de.vogella.e4.todo.contribute.dialogs.ExitDialog;

public class ExitHandlerWithCheck {
	@Execute
	public void execute(IEclipseContext context, IWorkbench workbench) {
		ExitDialog dialog = ContextInjectionFactory.
				make(ExitDialog.class, context);
		dialog.create();
		if (dialog.open() == Window.OK) {
			workbench.close();
		}
	}
}
