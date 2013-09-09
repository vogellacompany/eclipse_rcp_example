package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class TestHandler {
	@Execute
	public void execute(final Shell shell,  MWindow window) {
		IWindowCloseHandler handler = new IWindowCloseHandler() {
			@Override
			public boolean close(MWindow window) {
				return MessageDialog.openConfirm(shell, "Close",
						"You will loose data. Really close?");
			}
		};
		window.getContext().set(IWindowCloseHandler.class, handler);
	}

}
